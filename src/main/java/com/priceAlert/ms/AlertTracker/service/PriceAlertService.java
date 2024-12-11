package com.priceAlert.ms.AlertTracker.service;

import com.priceAlert.ms.AlertTracker.dto.CreateAlertRequest;
import com.priceAlert.ms.AlertTracker.exception.InvalidUrlException;
import com.priceAlert.ms.AlertTracker.exception.ScraperFailureException;
import com.priceAlert.ms.AlertTracker.exception.UnsupportedMarketplaceException;
import com.priceAlert.ms.AlertTracker.models.PriceAlert;
import com.priceAlert.ms.AlertTracker.repository.PriceAlertRepository;
import com.priceAlert.ms.AlertTracker.util.MarketplaceUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // Validate the URL using scraper service
        boolean isValid;
        try {
            isValid = scraperValidationService.validateUrl(request.getUrl());
        } catch (Exception e) {
            throw new ScraperFailureException("The scraper service failed to process the given URL.");
        }

        if (!isValid) {
            throw new InvalidUrlException("The provided URL is invalid or does not link to a valid product.");
        }

        // Create and save PriceAlert
        PriceAlert alert = new PriceAlert();
        alert.setId(UUID.randomUUID().toString());
        alert.setUserId(request.getUserId());
        alert.setName(request.getAlertName());
        alert.setProductUrl(request.getUrl());
        alert.setTargetPrice(request.getTargetPrice());
        alert.setMarketplace(marketplace);
        alert.setTargetReached(false);
        alert.setProductId(scraperValidationService.getDummyProductId(request.getUrl()));

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

        return repository.save(existingAlert);
    }
}
