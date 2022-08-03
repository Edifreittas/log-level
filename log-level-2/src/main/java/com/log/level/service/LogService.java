package com.log.level.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class LogService {

    @Autowired
    private WebClient webClient;


    public void purchase(int customer) {

        var initDate = LocalDateTime.now();

        log.debug("Log-Level-3 -> initDate: {}", initDate);

        var response = webClient.post()
            .uri("/log/purchase")
            .header("customer", String.valueOf(customer))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.debug("Log-Level-3 -> ResponseTime: {}ms -  ResponseBody: {}", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()), response);
    }

    public void call(long timeout, long sleepTime) {

        try {

            var initDate = LocalDateTime.now();

            log.debug("Log-Level-3 -> timeout: {} - sleepTime: {}", timeout, sleepTime);

            var response = webClient.get()
                .uri("/log/sleep?time=" + sleepTime)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(timeout))
                .block();

            log.debug("Log-Level-2 -> ResponseTime: {}ms -  ResponseBody: {}", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()), response);
        } catch (Exception ex) {
            log.error("Log-Level-2 -> timeout: {} - sleepTime: {}", timeout, sleepTime, ex);
        }
    }

}
