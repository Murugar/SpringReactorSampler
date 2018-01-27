package com.iqmsoft.reactor.quotes.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Slf4j
@Service
public class Publisher {

    @NonNull
    private final EventBus eventBus;

    @NonNull
    private final CountDownLatch latch;

    @NonNull
    private final ConfigurableApplicationContext context;

    @Value("${reactor.numberOfQuotes}")
    private int numberOfQuotes;

    @Value("${reactor.channel}")
    private String channel;

    @EventListener
    public void publishQuotes(@SuppressWarnings("unused") ApplicationReadyEvent event) throws InterruptedException {
        LocalTime then = LocalTime.now();
        AtomicInteger counter = new AtomicInteger(1);
        for (int i = 0; i < numberOfQuotes; i++) {
            eventBus.notify(channel, Event.wrap(counter.getAndIncrement()));
        }
        latch.await();
        long elapsed = Duration.between(then, LocalTime.now()).toMillis();
        log.info("Elapsed Time: {} ms", elapsed);
        log.info("Average Time per Quote: {} ms", elapsed / numberOfQuotes);
        context.close();
    }

}