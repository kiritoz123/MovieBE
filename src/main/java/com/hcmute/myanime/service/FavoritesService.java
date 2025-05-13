package com.hcmute.myanime.service;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.dto.FavoritesDTO;
import com.hcmute.myanime.exception.ResourceDeleteException;
import com.hcmute.myanime.exception.ResourceNotFoundException;
import com.hcmute.myanime.mapper.FavoritesMapper;
import com.hcmute.myanime.model.FavoritesEntity;
import com.hcmute.myanime.model.MovieSeriesEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.FavoritesRepository;
import com.hcmute.myanime.repository.MovieSeriesRepository;
import com.hcmute.myanime.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritesService {
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MovieSeriesRepository movieSeriesRepository;
    @Autowired
    private ApplicationUserService applicationUserService;

    public boolean saveOrDelete(FavoritesDTO favoritesDTO)
    {
        FavoritesEntity favoritesEntity = FavoritesMapper.toEntity(favoritesDTO);

        Optional<MovieSeriesEntity> movieSeriesRepositoryOptional = movieSeriesRepository.findById(favoritesDTO.getMovieSeriesId());

        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userLoggedIn = usersRepository.findByUsername(usernameLoggedIn);

        if(!movieSeriesRepositoryOptional.isPresent() || !userLoggedIn.isPresent()) {
            return false;
        }

        // Kiểm tra xem user đã favorites movie series đó hay chưa
        FavoritesEntity favoritesEntityChecked = favoritesRepository.findByMovieSeriesIdAndUserId(favoritesDTO.getMovieSeriesId(), userLoggedIn.get().getId());
        if(favoritesEntityChecked == null)
        {
            MovieSeriesEntity movieSeriesEntity = movieSeriesRepositoryOptional.get();
            UsersEntity usersEntity = userLoggedIn.get();

            favoritesEntity.setMovieSeries(movieSeriesEntity);
            favoritesEntity.setUser(usersEntity);

            try
            {
                favoritesRepository.save(favoritesEntity);
                return true;
            }
            catch (Exception ex)
            {
                return false;
            }
        } else {         // Neu favorites movie series da dc save thi xoa khoi db
            deleteById(favoritesEntityChecked.getId());
            throw new ResourceDeleteException("Favorite deleted successfully");
        }
    }

    public FavoritesEntity findByMovieSeriesIdAndUserLoggingId(int movieSeriesId) {
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userLoggedIn = usersRepository.findByUsername(usernameLoggedIn);
        FavoritesEntity favoritesEntity = favoritesRepository.findByMovieSeriesIdAndUserId(movieSeriesId, userLoggedIn.get().getId());
        return favoritesEntity;
    }

    public boolean deleteById(int favoritesID) {
        try {
            String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
            Optional<UsersEntity> userLoggedIn = usersRepository.findByUsername(usernameLoggedIn);
            if(!userLoggedIn.isPresent())
                return false;
            UsersEntity usersEntity = userLoggedIn.get();

            Optional<FavoritesEntity> favoritesEntityOptional = favoritesRepository.findById(favoritesID);
            if(!favoritesEntityOptional.isPresent())
                return false;
            FavoritesEntity favoritesEntity = favoritesEntityOptional.get();

            System.out.println(favoritesEntity.getUser().getId() + "/" + usersEntity.getId() + "/" + favoritesID);

            if(favoritesEntity.getUser().getId() == usersEntity.getId()) {
                favoritesRepository.deleteById(favoritesID);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<FavoritesEntity> findByUserLogin(String usernameLoggedIn) {
        UsersEntity userLogin = usersRepository.findByUsername(usernameLoggedIn).get();
        return userLogin.getFavoritesEntityCollection().stream().toList();
    }
}
