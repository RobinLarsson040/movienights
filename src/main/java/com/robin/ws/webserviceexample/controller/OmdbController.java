package com.robin.ws.webserviceexample.controller;

import com.robin.ws.webserviceexample.entity.MovieEntity;
import com.robin.ws.webserviceexample.models.MovieList;
import com.robin.ws.webserviceexample.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
public class OmdbController {

    final MovieRepository movieRepository;
    @Value("${omdb-url}")
    private String OMDB_URL;

    @Autowired
    public OmdbController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @GetMapping("movieById")
    public MovieEntity getMovieById(@RequestParam String id) {
        RestTemplate restTemplate = new RestTemplate();
        MovieEntity movieEntity = movieRepository.findMovieEntityByImdbID(id);
        MovieEntity searchResult;

        if (movieEntity != null) {
            return movieEntity;
        } else {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(OMDB_URL)
                    .queryParam("i", id);

            searchResult = restTemplate.getForObject(builder.toUriString(), MovieEntity.class);
            if (searchResult == null) {
                ////MOVIE ID NOT FOUND
            } else {
                movieRepository.save(searchResult);
            }
        }
        return searchResult;
    }


    @GetMapping("movieByTitle")
    public List<MovieEntity> getMovieByTitle(@RequestParam String title) {
        RestTemplate restTemplate = new RestTemplate();
        MovieEntity movieEntity = movieRepository.findMovieEntityByTitle(title);
        if (movieEntity == null) {
            //no movies found////////////////////////////
        }
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(OMDB_URL)
                .queryParam("s", title);

        MovieList searchResults = restTemplate.getForObject(builder.toUriString(), MovieList.class);
        List<MovieEntity> movieList = searchResults.getSearch();

        return movieList;
    }


}
