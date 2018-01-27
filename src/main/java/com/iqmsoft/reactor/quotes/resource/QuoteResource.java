package com.iqmsoft.reactor.quotes.resource;

import com.iqmsoft.reactor.quotes.dto.QuoteDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteResource {

    private String type;
    private QuoteDTO value;

}