package com.hcmute.myanime.service;

import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.EpisodeDTO;
import com.hcmute.myanime.dto.StatisticsEpisodeDTO;
import com.hcmute.myanime.dto.StatisticsMovieSeriesDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.MovieSeriesMapper;
import com.hcmute.myanime.model.EpisodeEntity;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.model.ViewStatisticsEntity;
import com.hcmute.myanime.repository.EpisodeRepository;
import com.hcmute.myanime.repository.MovieSeriesRepository;
import com.hcmute.myanime.repository.ViewStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private MovieSeriesRepository movieSeriesRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ViewStatisticsRepository viewStatisticsRepository;
    @Autowired
    private DigitalOceanSpaceService digitalOceanSpaceService;

    public List<EpisodeEntity> findBySeriesId(int seriesId){
        if(!movieSeriesRepository.findById(seriesId).isPresent()){
            throw new BadRequestException("Series not found");
        }
        List<EpisodeEntity> episodeEntityList = episodeRepository.findBySeriesId(seriesId); //call with jpa
//        List<EpisodeEntity> episodeEntityList = episodeRepository.findBySeriesId_StoredProcedure(seriesId); //call with procedure
        return episodeEntityList;
    }

    public EpisodeEntity findById(int episodeId) {
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
        if(!episodeEntityOptional.isPresent()) {
            throw new BadRequestException("Episode not found");
        }
        return episodeEntityOptional.get();
    }
    @Transactional(rollbackFor = Exception.class)
    public EpisodeEntity save(EpisodeDTO episodeDTO, int seriesId) {
        Optional<MovieSeriesEntity> movieSeriesEntityOptional = movieSeriesRepository.findById(seriesId);
        if(!movieSeriesEntityOptional.isPresent()) {
            throw new BadRequestException("Series id not found");
        }
        MovieSeriesEntity movieSeriesEntity = movieSeriesEntityOptional.get();
        EpisodeEntity newEpisodeEntity = new EpisodeEntity();
        newEpisodeEntity.setMovieSeriesBySeriesId(movieSeriesEntity);
        newEpisodeEntity.setTitle(episodeDTO.getTitle());
        newEpisodeEntity.setPremiumRequired(episodeDTO.getPremiumRequired());
        newEpisodeEntity.setNumEpisodes(episodeDTO.getNumEpisodes());
        try {
            EpisodeEntity savedEntity = episodeRepository.save(newEpisodeEntity);
            return savedEntity;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Async
    public void uploadSourceFileToCloudinary(byte[] fileByteArray, int episodeId) {
        try {
            Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
            if(!episodeEntityOptional.isPresent()) {
                throw new BadRequestException("Episode id not found");
            }
            EpisodeEntity episodeEntity = episodeEntityOptional.get();
            String urlSource = cloudinaryService.uploadFile(
                    fileByteArray,
                    String.valueOf(episodeId),
                    "MyAnimeProject_TLCN" + "/" + "episode");
            System.out.println("url CD " + urlSource);
            if(!urlSource.equals("-1")) {
                episodeEntity.setResource(urlSource);
                String resourcePublicId = "MyAnimeProject_TLCN" + "/" + "episode" + "/" + episodeEntity.getId();
                episodeEntity.setResourcePublicId(resourcePublicId);
                episodeRepository.save(episodeEntity);
            }
        } catch (Exception ex) {
            throw new BadRequestException("Episode id " + episodeId + "add to server cloudinary fail");
        }
    }
    @Async
    public void uploadSourceFileToDigitalOcean(InputStream fileInputStream, String fileContentType, int episodeId) {
        try {
            Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
            if(!episodeEntityOptional.isPresent()) {
                throw new BadRequestException("Episode id not found");
            }
            EpisodeEntity episodeEntity = episodeEntityOptional.get();
            String urlSource = digitalOceanSpaceService.uploadFile(
                    fileInputStream,
                    fileContentType,
                    episodeId + "." + "mp4",
                    "movie_series/" + episodeEntity.getMovieSeriesBySeriesId().getId()  + "/episode");
            System.out.println("url DO " + urlSource);
            if(!urlSource.equals("-1")) {
                episodeEntity.setResourceDo(urlSource);
                episodeRepository.save(episodeEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException("Episode id" + episodeId + "add to server DO fail");
        }
    }


    public boolean updateByEpisodeId(int episodeId, EpisodeDTO episodeDTO, MultipartFile sourceFile, List<String> servers) {
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
        if(!episodeEntityOptional.isPresent()) {
            throw new BadRequestException("Episode id not found");
        }
        EpisodeEntity updateEpisodeEntity = episodeEntityOptional.get();
        updateEpisodeEntity.setTitle(episodeDTO.getTitle());
        updateEpisodeEntity.setPremiumRequired(episodeDTO.getPremiumRequired());
        updateEpisodeEntity.setNumEpisodes(episodeDTO.getNumEpisodes());
        try {
                episodeRepository.save(updateEpisodeEntity);
            //        Update source file
            if(sourceFile != null && sourceFile.getSize() > 0) { //File not send, not update old source
                for (String server : servers) {
                    switch (server) {
                        case "do" -> {
                            try {
                                this.uploadSourceFileToDigitalOcean(sourceFile.getInputStream(), sourceFile.getContentType(), episodeId);
                            } catch (Exception e) {
                                throw new BadRequestException("Episode update success, source DO add fail");
                            }
                        }
                        case "cd" -> {
                            try {
                                this.uploadSourceFileToCloudinary(sourceFile.getBytes(), episodeId);
                            } catch (Exception e) {
                                throw new BadRequestException("Episode update success, source CD add fail");
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean deleteById(int episodeId) {
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
        if(!episodeEntityOptional.isPresent()) {
            return false;
        }
        EpisodeEntity episodeEntity = episodeEntityOptional.get();
        if(episodeEntity.getResourcePublicId() != null) {
            cloudinaryService.deleteFileVideo(episodeEntity.getResourcePublicId());
        }
        if(episodeEntity.getResourceDo() != null) {
            digitalOceanSpaceService.deleteFileVideo("episode" + "/" + episodeId + ".mp4");
        }
        try {
            episodeRepository.deleteById(episodeId);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean increaseView(int episodeId, String ipClient)
    {
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
        if(!episodeEntityOptional.isPresent())
            return false;

        EpisodeEntity episodeEntity = episodeEntityOptional.get();

        List<ViewStatisticsEntity> viewStatisticsEntityList = viewStatisticsRepository.findByIpAddressAndEpisode(ipClient, episodeEntity, PageRequest.of(0, 1));

        viewStatisticsEntityList.forEach((v)->{
            System.out.println(v.getCreateAt().getTime()/1000);
        });

        if(viewStatisticsEntityList.size() == 0)
        {
            ViewStatisticsEntity viewStatisticsEntityInsert = new ViewStatisticsEntity();
            viewStatisticsEntityInsert.setEpisode(episodeEntity);
            viewStatisticsEntityInsert.setIpAddress(ipClient);
            viewStatisticsRepository.save(viewStatisticsEntityInsert);
            episodeEntity.setTotalView(episodeEntity.getTotalView() + 1);
            episodeRepository.save(episodeEntity);
            return true;
        }

        ViewStatisticsEntity viewStatisticsEntity = viewStatisticsEntityList.get(0);
        Timestamp lastTimeView = viewStatisticsEntity.getCreateAt();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        long distanceSecond = (currentTimestamp.getTime() / 1000) - (lastTimeView.getTime() / 1000);

        // The rule is that each view of a client can only be increased at least once every 30 minutes
        if(distanceSecond < GlobalVariable.MINIMUM_SECONDS_VIEW)
            return false;

        ViewStatisticsEntity viewStatisticsEntityInsert = new ViewStatisticsEntity();
        viewStatisticsEntityInsert.setEpisode(episodeEntity);
        viewStatisticsEntityInsert.setIpAddress(ipClient);
        viewStatisticsRepository.save(viewStatisticsEntityInsert);
        episodeEntity.setTotalView(episodeEntity.getTotalView() + 1);
        episodeRepository.save(episodeEntity);
        return true;
    }

    public List<StatisticsEpisodeDTO> getTopEpisodeMostView(int numberOfDay, int size)
    {
        List<StatisticsEpisodeDTO> statisticsEpisodeDTOList = new ArrayList<>();

        List<Object[]> listObject = viewStatisticsRepository.findTopMostViewWithDay(numberOfDay, PageRequest.of(0, size));
        if(listObject.size() == 0)
            return statisticsEpisodeDTOList;

        listObject.forEach((itemObj)->{
            EpisodeEntity episodeEntity = (EpisodeEntity) itemObj[0];
            long statisticsView = (Long) itemObj[1];
            if(episodeEntity != null)
            {
                StatisticsEpisodeDTO statisticsEpisodeDTO = new StatisticsEpisodeDTO(episodeEntity.getId(), episodeEntity.getCreateAt(), episodeEntity.getResource(), episodeEntity.getTitle(), statisticsView, episodeEntity.getMovieSeriesBySeriesId().getId());
                statisticsEpisodeDTOList.add(statisticsEpisodeDTO);
            }
        });

        return statisticsEpisodeDTOList;
    }

    public List<StatisticsEpisodeDTO> getTopEpisodeMostView_StoredProcedures(int numberOfDay, int size)
    {
        List<StatisticsEpisodeDTO> statisticsEpisodeDTOList = new ArrayList<>();

        List<Object[]> listObject = viewStatisticsRepository.findTopMostViewWithDay_StoredProcedures(numberOfDay, size);
        if(listObject.size() == 0)
            return statisticsEpisodeDTOList;

        listObject.forEach((itemObj)->{
            long statisticsView = (long) (int) itemObj[10];
            StatisticsEpisodeDTO statisticsEpisodeDTO =
                    new StatisticsEpisodeDTO(
                            (int) itemObj[0], //episode id
                            (Timestamp) itemObj[1], //episode create at
                            (String) itemObj[4], //episode resource
                            (String) itemObj[7], //episode title
                            statisticsView,
                            (int) itemObj[9]
                    );
            statisticsEpisodeDTOList.add(statisticsEpisodeDTO);
        });

        return statisticsEpisodeDTOList;
    }

    public List<StatisticsMovieSeriesDTO> getTopSeriesMostView(int numberOfDay, int size) {
        List<StatisticsMovieSeriesDTO> statisticsMovieSeriesDTOList = new ArrayList<>();
        List<StatisticsEpisodeDTO> statisticsEpisodeDTOList = this.getTopEpisodeMostView(numberOfDay, 9999);
//        List<StatisticsEpisodeDTO> statisticsEpisodeDTOList = this.getTopEpisodeMostView_StoredProcedures(numberOfDay, 9999);
        if (statisticsEpisodeDTOList.size() == 0)
            return statisticsMovieSeriesDTOList;

        Map<Integer, Long> series = new HashMap<>();
        statisticsEpisodeDTOList.forEach((statisticsEpisodeDTO) -> {
            int movieSeriesId = statisticsEpisodeDTO.getSeriesId();
            long statisticsView = statisticsEpisodeDTO.getStatisticsView();
            if (!series.containsKey(movieSeriesId)) {
                series.put(movieSeriesId, statisticsView);
            } else {
                series.put(movieSeriesId, series.get(movieSeriesId) + statisticsView);
            }
        });

        for (var seri : series.entrySet()) {
            Optional<MovieSeriesEntity> movieSeriesEntityOptional = movieSeriesRepository.findById(seri.getKey());
            if (!movieSeriesEntityOptional.isPresent())
                continue;
            MovieSeriesEntity movieSeriesEntity = movieSeriesEntityOptional.get();

            // Mapping Entity to DTO
            StatisticsMovieSeriesDTO statisticsMovieSeriesDTO = MovieSeriesMapper.toDTO2(movieSeriesEntity, seri.getValue());

            statisticsMovieSeriesDTOList.add(statisticsMovieSeriesDTO);
        }


        // Sorted ASC and Reverse DESC
        List<StatisticsMovieSeriesDTO> list = statisticsMovieSeriesDTOList.stream().sorted(Comparator.comparingLong(StatisticsMovieSeriesDTO::getStatisticsViewTotal)).toList();
        List<StatisticsMovieSeriesDTO> listReverse = new ArrayList<>();
        for(int i = list.size() - 1; i >= 0; i--) {
            listReverse.add(list.get(i));
        }


        // Fix size index out of range
        while (size > listReverse.size())
            size--;

        return listReverse.subList(0, size);
    }

}
