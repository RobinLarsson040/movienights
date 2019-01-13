package com.robin.ws.webserviceexample.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.robin.ws.webserviceexample.entity.UserEntity;
import com.robin.ws.webserviceexample.models.Event;
import com.robin.ws.webserviceexample.repository.UserRepository;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CalendarService {
    @Value("${client-id}")
    private String CLIENT_ID;
    @Value("${client-secret}")
    private String CLIENT_SECRET;

    private final UserRepository userRepository;

    @Autowired
    public CalendarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Event> getAvailableTimes() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        List<Event> notAvailableTimes = new ArrayList<>();
        DateTime now = new DateTime(System.currentTimeMillis());
        org.joda.time.DateTime jodaDateMax = new org.joda.time.DateTime(System.currentTimeMillis()).plusDays(30);
        DateTime max = new DateTime(jodaDateMax.getMillis());
        updateAllTokens();

        users.forEach(user -> {
            System.out.println(user.getEmail());
            Events events = null;
            try {
                Calendar calendar = getCalendar(user.getEmail());
                events = calendar.events().list("primary")
                        .setTimeMin(now)
                        .setTimeMax(max)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            events.getItems().forEach(item -> {
                Event event = new Event();
                BeanUtils.copyProperties(item, event);
                event.setStartTime(item.getStart().getDateTime().toString());
                event.setEndTime(item.getEnd().getDateTime().toString());
                notAvailableTimes.add(event);
            });
        });
        return calculateFreeTimes(notAvailableTimes);
    }

    private void updateAllTokens() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        users.forEach(user -> {
            long expiresAt = user.getExparationTime();
            long now = new DateTime(System.currentTimeMillis()).getValue();

            if (hasTokenExpired(expiresAt, now)) {
                GoogleCredential newCredentials = null;
                try {
                    newCredentials = refreshCredentials(user.getRefreshToken());
                } catch (IOException e) {
                    System.out.println("ERROR");
                    e.printStackTrace();
                }
                System.out.println(newCredentials.getAccessToken());
                user.setAccesToken(newCredentials.getAccessToken());
                userRepository.save(user);
            }
        });
    }

    private List<Event> calculateFreeTimes(List<Event> notFreeEvents) {
        List<Event> freeEvents = new ArrayList<>();
        org.joda.time.DateTime now = new org.joda.time.DateTime();

        for (int i = 0; i < 30; i++) {
            Event freeEvent = new Event();
            freeEvent.setStartTime(new org.joda.time.DateTime(
                    now.getYear(),
                    now.getMonthOfYear(),
                    now.getDayOfMonth(),
                    18,
                    0
            ).toString());
            freeEvent.setEndTime(
                    new org.joda.time.DateTime(
                            now.getYear(),
                            now.getMonthOfYear(),
                            now.getDayOfMonth(),
                            22,
                            0
                    ).toString()
            );
            freeEvents.add(freeEvent);
            now = now.plusDays(1);
        }

        notFreeEvents.forEach(event -> {
            int notFreeDay = convertStringToDateTime(event.getStartTime()).getDayOfMonth();
            for (int i = 0; i < freeEvents.size(); i++) {
                int freeDay = convertStringToDateTime(freeEvents.get(i).getStartTime()).getDayOfMonth();
                if (freeDay == notFreeDay) {
                    freeEvents.remove(i);
                }
            }
        });
        return freeEvents;
    }

    public org.joda.time.DateTime convertStringToDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String startTimeString = dateString.substring(0, dateString.indexOf("T"));
        org.joda.time.DateTime date = formatter.parseDateTime(startTimeString);
        return date;
    }


    private Calendar getCalendar(String email) {
        UserEntity user = userRepository.findUserByEmail(email);
        GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccesToken());
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("MovieNights")
                .build();
    }

    private boolean hasTokenExpired(long expiresAt, long now) {
        org.joda.time.DateTime expire = new org.joda.time.DateTime(expiresAt);
        org.joda.time.DateTime current = new org.joda.time.DateTime(now);
        return current.isAfter(expire);
    }

    private GoogleCredential refreshCredentials(String refreshToken) throws IOException {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
                    refreshToken, CLIENT_ID, CLIENT_SECRET)
                    .execute();
            return new GoogleCredential().setAccessToken(response.getAccessToken());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
