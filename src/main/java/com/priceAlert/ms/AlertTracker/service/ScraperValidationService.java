package com.priceAlert.ms.AlertTracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ScraperValidationService {

    private final WebClient webClient;

    public ScraperValidationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082/api/scrape").build();
    }

    /**
     * Fetches product details (productId and currentPrice) from the AmazonScraper microservice.
     * If the call fails, it indicates that the URL is invalid or the scraper could not process it.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchProductDetails(String url) {
        try {
            return webClient.post()
                    .bodyValue(Map.of("url", url))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to fetch product details from scraper.", e);
        }
    }
}
