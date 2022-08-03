package com.log.level.controller;


import com.log.level.service.LogService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RestController
@RequestMapping(value = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

    @Autowired
    private LogService service;

    @GetMapping
    @SneakyThrows
    @ResponseStatus(HttpStatus.OK)
    public void log(@RequestHeader(value = "customer") String customer) {

        var initDate = LocalDateTime.now();

        MDC.put("customer", customer);

        log.info("INFO - Start: {}.", initDate);
        log.debug("DEBUG - Start: {}.", initDate);
        log.warn("WARN - Start: {}.", initDate);
        log.trace("TRACE - Start: {}.", initDate);

        TimeUnit.SECONDS.sleep(2);

        log.info("INFO - End: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
        log.debug("DEBUG - End: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
        log.warn("WARN - End: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
        log.trace("TRACE - End: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void log(@RequestParam(value = "timeout") long timeout, @RequestParam(value = "sleepTime") long sleepTime) {

        var initDate = LocalDateTime.now();

        log.info("Start: {}. Timeout: {}, SleepTime:{}", initDate, timeout, sleepTime);

        try {

            service.call(timeout, sleepTime);

            log.info("End: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
        } catch (Exception ex) {
            log.error("End: {}, Error: {}", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms", ex.getMessage());
        }
    }

    @PostMapping("/timeout")
    @ResponseStatus(HttpStatus.OK)
    public void logWithTimeout(@RequestParam(value = "timeout") long timeout, @RequestParam(value = "sleepTime") long sleepTime) {

        var initDate = LocalDateTime.now();

        log.info("Início: {}. Timeout: {}, SleepTime:{}", initDate, timeout, sleepTime);

        try {

            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.submit(() -> {
                service.call(timeout, sleepTime);
            }).get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            log.error("Processamento: {}, Error: {}", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms", ex);
        }

        log.info("Tempo total de processamento: {}. ", ChronoUnit.MILLIS.between(initDate, LocalDateTime.now()) + "ms");
    }

    @PostMapping("/purchase")
    @SneakyThrows
    @ResponseStatus(HttpStatus.OK)
    public void purchase(@RequestHeader(value = "customer") int customer) {

        //Adicionando na mão apenas nesse registro
        log.info("INFO - start - Purchase", kv("customer", customer));

       // MDC.put("customer_id", String.valueOf(customer));
        service.purchase(customer);


        log.info("INFO - end - Purchase");

    }

}
