package com.robin.ws.webserviceexample.controller;

import com.robin.ws.webserviceexample.models.Event;
import com.robin.ws.webserviceexample.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CalendarController {

    final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/freeTimes")
    public List<Event> getAvailableTimes() {
        return calendarService.getAvailableTimes();
    }


}
