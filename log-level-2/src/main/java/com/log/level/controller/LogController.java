package com.log.level.controller;

import com.log.level.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

    @Autowired
    private LogService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String post() {

        log.info("Log - Info");
        log.trace("Log - Trace");
        log.debug("Log - Debug");
        log.error("Log - Error");

        return "ok";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void get() {

        log.info("Log - Info");
        log.trace("Log - Trace");
        log.debug("Log - Debug");
        log.error("Log - Error");
    }

    @GetMapping("/sleep")
    @ResponseStatus(HttpStatus.OK)
    public String getSleep(@RequestParam(value = "time") long time) {

        var initDate = LocalDateTime.now();

        try {

            log.info("Start: {}. SleepTime:{}", initDate, time);
            Thread.sleep(time);

            log.info("End: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
        } catch (Exception ex) {
            log.error("End: {}, Error: {}", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms", ex.getMessage());
        }

        return "ok";
    }

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.OK)
    public void purchase(@RequestHeader(value = "customer") int customer) {
        service.purchase(customer);
    }

}
