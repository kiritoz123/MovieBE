package com.hcmute.myanime.repository;

import com.hcmute.myanime.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Query( "select o from CategoryEntity o where id in :ids" )
    Collection<CategoryEntity> findByIds(@Param("ids") List<Integer> inventoryIdList);
    @Query(value = "select count(series) from MovieSeriesEntity series join " +
            "series.movieByMovieId movie join " +
            "movie.categoryEntityCollection categoryMovie where categoryMovie.id = :categoryId")
    Long countSeriesByCategoryId(@Param("categoryId") int categoryId);
    //View
    @Query(value = "select * from hcmutemyanime.listcategoryview", nativeQuery = true)
    List<CategoryEntity> findAllByView();
    //Call by stored procedures

}
