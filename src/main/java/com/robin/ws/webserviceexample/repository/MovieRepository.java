package com.robin.ws.webserviceexample.repository;

import com.robin.ws.webserviceexample.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, String> {
    MovieEntity findMovieEntityByTitle(String title);
    MovieEntity findMovieEntityByImdbID(String id);
}
