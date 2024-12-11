package com.priceAlert.ms.AlertTracker.service;

import org.springframework.stereotype.Service;

@Service
public class ScraperValidationService {

    public boolean validateUrl(String url) {
        // For testing, always return true
        return true;
    }

    public String getDummyProductId(String url) {
        // Return a dummy product ID for testing
        return "DUMMY_PRODUCT_ID";
    }
}
