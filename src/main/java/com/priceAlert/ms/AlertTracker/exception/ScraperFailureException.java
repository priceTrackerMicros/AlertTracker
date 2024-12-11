package com.priceAlert.ms.AlertTracker.exception;

public class ScraperFailureException extends RuntimeException {
    public ScraperFailureException(String message) {
        super(message);
    }
}
