package com.hcmute.myanime.service;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.common.GlobalVariable;
import com.hcmute.myanime.dto.CommentUserDTO;
import com.hcmute.myanime.dto.MovieSeriesDTO;
import com.hcmute.myanime.exception.BadRequestException;
import com.hcmute.myanime.mapper.MovieSeriesMapper;
import com.hcmute.myanime.model.*;
import com.hcmute.myanime.repository.CommentsRepository;
import com.hcmute.myanime.repository.EpisodeRepository;
import com.hcmute.myanime.repository.MovieSeriesRepository;
import com.hcmute.myanime.repository.UsersRepository;
import com.hcmute.myanime.utils.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private MovieSeriesRepository movieSeriesRepository;
    @Autowired
    private MovieSeriesService movieSeriesService;

    public List<CommentEntity> findByEpisodeId(int episodeId, String page, String limit) {
        limit = (limit == null || limit.equals("")
                || !PagingUtil.isNumber(limit) || Long.parseLong(limit) < 0) ? GlobalVariable.DEFAULT_LIMIT : limit;

        page = (!PagingUtil.isValidPage(page)) ? GlobalVariable.DEFAULT_PAGE : page;
        Pageable pageable = PageRequest.of((Integer.parseInt(page)), Integer.parseInt(limit));
        //
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(episodeId);
        if(!episodeEntityOptional.isPresent()) {
            throw new BadRequestException("Can not find comments with episode id: " + episodeId);
        }
        EpisodeEntity episodeEntity = episodeEntityOptional.get();
//        List<CommentEntity> commentEntityList = episodeEntity.getCommentsById().stream().toList(); //jpa
        List<CommentEntity> commentEntityList = commentsRepository
                .getByEpisodeIdPageable_sp(episodeEntity.getId(), pageable.getPageNumber(), pageable.getPageSize());
        return commentEntityList;
    }

    public CommentEntity save(CommentUserDTO commentUserDTO) {
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userLoggedIn = usersRepository.findByUsername(usernameLoggedIn);
        Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(commentUserDTO.getEpisodeId());
        if(!episodeEntityOptional.isPresent() || !userLoggedIn.isPresent()) {
            throw new BadRequestException("Can not comment episode");
        }
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentUserDTO.getContent());
        commentEntity.setEpisodeByEpisodeId(episodeEntityOptional.get());
        Timestamp createAt = new Timestamp(System.currentTimeMillis());
        System.out.println(createAt);
        commentEntity.setCreateAt(createAt);
        commentEntity.setUsersByUserId(userLoggedIn.get());
        try {
            return commentsRepository.save(commentEntity); //jpa
//            return commentsRepository.insertOrUpdateComment(
//                    0,
//                    commentEntity.getContent(),
//                    commentEntity.getEpisodeByEpisodeId().getId(),
//                    commentEntity.getUsersByUserId().getId()
//            ); //Using stored procedure
        } catch (Exception ex) {
            throw new BadRequestException("Can not save comment for user: " + usernameLoggedIn);
        }
    }

    public boolean deleteById(int commentId) {
        try {
            String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
            Optional<UsersEntity> userLoggedIn = usersRepository.findByUsername(usernameLoggedIn);
            if(!userLoggedIn.isPresent())
                return false;
            UsersEntity usersEntity = userLoggedIn.get();

            Optional<CommentEntity> commentEntityOptional = commentsRepository.findById(commentId);
            if(!commentEntityOptional.isPresent())
                return false;
            CommentEntity commentEntity = commentEntityOptional.get();

            if(commentEntity.getUsersByUserId().getId() == usersEntity.getId())
            {
                commentsRepository.deleteById(commentId);
                return true;
            }
            return false;

        } catch (Exception ex) {
            return false;
        }
    }

    public Long totalCommentByEpisodeEntity(EpisodeEntity episodeEntity) {
        return commentsRepository.countByEpisodeByEpisodeId(episodeEntity);
    }

    public Long totalCommentByMovieSeriesEntity(MovieSeriesEntity movieSeriesEntity) {
        Long totalComment = Long.valueOf(0);
        for (EpisodeEntity item : movieSeriesEntity.getEpisodesById()) {
            totalComment += totalCommentByEpisodeEntity(item);
        }
        return totalComment;
    }

    public List<MovieSeriesDTO> findSeriesCommentRecent(int limit)
    {
        List<MovieSeriesDTO> movieSeriesDTOList = new ArrayList<>();
//        List<Object[]> episodeIDCommentRecentWithLimit = commentsRepository.getEpisodeIDCommentRecentWithLimit(limit); //Use spring jpa
        List<Object[]> episodeIDCommentRecentWithLimit = commentsRepository.getEpisodeIDCommentRecentWithLimitByStoredProcedures(limit);// use stored procedures
        episodeIDCommentRecentWithLimit.forEach(obj -> {
            MovieSeriesDTO movieSeriesDTO = new MovieSeriesDTO();
            movieSeriesDTO.setId((int)obj[0]);
            movieSeriesDTO.setName((String) obj[1]);
            movieSeriesDTO.setDescription((String) obj[2]);
            movieSeriesDTO.setDateAired((Timestamp) obj[3]);
            movieSeriesDTO.setTotalEpisode((int) obj[4]);
            movieSeriesDTO.setImage((String) obj[5]);
            movieSeriesDTO.setCreateAt((Timestamp) obj[6]);
            movieSeriesDTO.setMovieId((int)obj[7]);
            movieSeriesDTOList.add(movieSeriesDTO);
        });

        System.out.println(movieSeriesDTOList.size());
        return movieSeriesDTOList;
    }

//    public List<MovieSeriesDTO> findSeriesCommentRecent(int limit)
//    {
//        List<Integer> listEpisodeCommentRecent = commentsRepository.getEpisodeIDCommentRecentWithLimit(PageRequest.of(0, 50));
//
//        List<Integer> listIdEpTemp = new ArrayList<>();
//        listEpisodeCommentRecent.forEach(id->{
//            if(listIdEpTemp.size() >= limit)
//                return;
//            if(listIdEpTemp.contains(id))
//                return;
//            listIdEpTemp.add(id);
//        });
//
//        List<MovieSeriesDTO> movieSeriesDTOList = new ArrayList<>();
//        listIdEpTemp.forEach(epID-> {
//            Optional<EpisodeEntity> episodeEntityOptional = episodeRepository.findById(epID);
//            if(episodeEntityOptional.isPresent()) {
//                EpisodeEntity episodeEntity = episodeEntityOptional.get();
//                MovieSeriesEntity movieSeriesEntity = episodeEntity.getMovieSeriesBySeriesId();
//                Long viewOfSeries = movieSeriesService.totalViewByMovieSeriesEntity(movieSeriesEntity);
//                movieSeriesDTOList.add(MovieSeriesMapper.toDTO(movieSeriesEntity, viewOfSeries));
//            }
//        });
//
//        return movieSeriesDTOList;
//    }
}
