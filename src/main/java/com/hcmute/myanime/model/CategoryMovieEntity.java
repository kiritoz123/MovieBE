package com.hcmute.myanime.model;

import javax.persistence.*;

@Entity
@Table(name = "category_movie", schema = "hcmutemyanime")
public class CategoryMovieEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int movie_id;
    private int category_id;
}
