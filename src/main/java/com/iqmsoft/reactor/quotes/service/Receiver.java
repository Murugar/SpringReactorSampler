package com.iqmsoft.reactor.quotes.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iqmsoft.reactor.quotes.resource.QuoteResource;

import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
@Slf4j
@Service
public class Receiver implements Consumer<Event<Integer>> {

    private final RestTemplate restTemplate = new RestTemplate();

    private final CountDownLatch latch;

    @Override
    public void accept(final Event<Integer> event) {
        QuoteResource quoteResource = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
        log.info("Quote {}: {}", event.getData(), quoteResource.getValue().getQuote());
        latch.countDown();
    }

}