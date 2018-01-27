package com.iqmsoft.reactor.quotes.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.iqmsoft.reactor.quotes.service.Receiver;

import reactor.Environment;
import reactor.bus.EventBus;

import java.util.concurrent.CountDownLatch;

import static reactor.bus.selector.Selectors.$;

@Configuration
public class ReactorConfiguration {

    @Bean
    public CountDownLatch countDownLatch(@Value("${reactor.numberOfQuotes}") final int numberOfQuotes) {
        return new CountDownLatch(numberOfQuotes);
    }

    @Bean
    public Environment environment() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    public EventBus eventBus(final Environment environment, final Receiver receiver, @Value("${reactor.channel}") final String channel) {
        EventBus eventBus = EventBus.create(environment, Environment.THREAD_POOL);
        eventBus.on($(channel), receiver);
        return eventBus;
    }

}