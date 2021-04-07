package com.aris.stock.controller;

import com.aris.stock.model.InputRequest;
import com.aris.stock.model.QuoteRequest;
import com.aris.stock.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@RestController
public class RsocketClientController {
 
    private final RSocketRequester rSocketRequester;
 
    Logger logger = LoggerFactory.getLogger(RsocketClientController.class);
 
    public RsocketClientController(@Autowired RSocketRequester.Builder builder) {
        this.rSocketRequester = builder.tcp("localhost", 7000);
    }
 
    @GetMapping("/stockQuote")
    public Flux<Stock> requestResponse() {
        InputRequest inputRequest = new InputRequest(Arrays.asList(new QuoteRequest("AAPL"), new QuoteRequest("GOOG")));
        logger.info("Send notification for my-request-response: " + inputRequest);
        return rSocketRequester
                .route("stock-quote")
                .data(inputRequest)
                .retrieveFlux(Stock.class);
    }

    @GetMapping("/stockQuoteWithTime")
    public ResponseEntity<Flux<Stock>> requestStream() {
        InputRequest inputRequest = new InputRequest(3, Arrays.asList(new QuoteRequest("AAPL"),
                new QuoteRequest("GOOG")));

        logger.info("Send notification for my-request-stream: " + inputRequest);
        Flux<Stock> notificationFlux = rSocketRequester
                .route("request-stream")
                .data(inputRequest)
                .retrieveFlux(Stock.class);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(notificationFlux);
    }

    @GetMapping("/getStockQuotesWithTimeAndFilters")
    public ResponseEntity<Flux<Stock>> getStockQuotesWithTimeAndFilters() {
        InputRequest inputRequest = new InputRequest(3, Arrays.asList(new QuoteRequest("AAPL", "30"),
                new QuoteRequest("GOOG", "20")));

        logger.info("Send notification for my-request-stream: " + inputRequest);
        Flux<Stock> notificationFlux = rSocketRequester
                .route("getStockQuotesWithTimeAndFilters")
                .data(inputRequest)
                .retrieveFlux(Stock.class);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(notificationFlux);
    }
}