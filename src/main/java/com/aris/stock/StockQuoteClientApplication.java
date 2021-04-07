package com.aris.stock;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockQuoteClientApplication {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(StockQuoteClientApplication.class, args);
		System.in.read();
	}

}
