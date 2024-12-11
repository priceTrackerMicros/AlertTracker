package com.priceAlert.ms.AlertTracker.controller;

import com.priceAlert.ms.AlertTracker.dto.CreateAlertRequest;
import com.priceAlert.ms.AlertTracker.models.PriceAlert;
import com.priceAlert.ms.AlertTracker.service.PriceAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class PriceAlertController {

    private final PriceAlertService service;

    public PriceAlertController(PriceAlertService service) {
        this.service = service;
    }

    @PostMapping
    public PriceAlert createAlert(@RequestBody CreateAlertRequest request) {
        return service.createAlert(request);
    }

    @GetMapping
    public List<PriceAlert> getAllAlerts() {
        return service.getAllAlerts();
    }

    @GetMapping("/{id}")
    public PriceAlert getAlertById(@PathVariable String id) {
        return service.getAlertById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlert(@PathVariable String id) {
        service.deleteAlert(id);
    }

    @PutMapping("/{id}")
    public PriceAlert updateAlert(@PathVariable String id, @RequestBody PriceAlert updatedAlert) {
        return service.updateAlert(id, updatedAlert);
    }
}
