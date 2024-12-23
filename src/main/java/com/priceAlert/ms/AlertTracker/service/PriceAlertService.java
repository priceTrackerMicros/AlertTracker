package com.priceAlert.ms.AlertTracker.service;

import com.priceAlert.ms.AlertTracker.dto.CreateAlertRequest;
import com.priceAlert.ms.AlertTracker.exception.ScraperFailureException;
import com.priceAlert.ms.AlertTracker.exception.UnsupportedMarketplaceException;
import com.priceAlert.ms.AlertTracker.models.PriceAlert;
import com.priceAlert.ms.AlertTracker.repository.PriceAlertRepository;
import com.priceAlert.ms.AlertTracker.util.MarketplaceUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PriceAlertService {

    private final PriceAlertRepository repository;
    private final ScraperValidationService scraperValidationService;

    public PriceAlertService(PriceAlertRepository repository, ScraperValidationService scraperValidationService) {
        this.repository = repository;
        this.scraperValidationService = scraperValidationService;
    }

    public PriceAlert createAlert(CreateAlertRequest request) {
        // Validate marketplace
        String marketplace;
        try {
            marketplace = MarketplaceUtils.getMarketplace(request.getUrl());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedMarketplaceException("The marketplace for the given URL is not supported.");
        }

        // Fetch product details from the scraper (combines validation and data fetching)
        Map<String, Object> scraperResponse;
        try {
            scraperResponse = scraperValidationService.fetchProductDetails(request.getUrl());
        } catch (IllegalArgumentException e) {
            throw new ScraperFailureException(e.getMessage());
        }

        String productId = (String) scraperResponse.get("productId");
        BigDecimal currentPrice = BigDecimal.valueOf((Double) scraperResponse.get("price"));

        // Create and save PriceAlert
        PriceAlert alert = new PriceAlert();
        alert.setId(UUID.randomUUID().toString());
        alert.setPhoneNumber(request.getPhoneNumber());
        alert.setName(request.getAlertName());
        alert.setProductUrl(request.getUrl());
        alert.setTargetPrice(request.getTargetPrice());
        alert.setCurrentPrice(currentPrice);
        alert.setMarketplace(marketplace);
        alert.setTargetReached(currentPrice.compareTo(request.getTargetPrice()) <= 0);
        alert.setProductId(productId);

        return repository.save(alert);
    }

    // Retrieve all alerts
    public List<PriceAlert> getAllAlerts() {
        return repository.findAll();
    }

    // Retrieve a specific alert by ID
    public PriceAlert getAlertById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PriceAlert with ID " + id + " not found."));
    }

    // Delete an alert by ID
    public void deleteAlert(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("PriceAlert with ID " + id + " not found.");
        }
        repository.deleteById(id);
    }

    // Update an alert
    public PriceAlert updateAlert(String id, PriceAlert updatedAlert) {
        PriceAlert existingAlert = getAlertById(id);

        existingAlert.setName(updatedAlert.getName());
        existingAlert.setTargetPrice(updatedAlert.getTargetPrice());
        existingAlert.setProductUrl(updatedAlert.getProductUrl());
        existingAlert.setTargetReached(updatedAlert.isTargetReached());
        existingAlert.setMarketplace(updatedAlert.getMarketplace());
        existingAlert.setCurrentPrice(updatedAlert.getCurrentPrice()); // Update current price if needed

        return repository.save(existingAlert);
    }
}
