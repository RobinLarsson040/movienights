package com.robin.ws.webserviceexample.service;

import com.robin.ws.webserviceexample.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;



}
