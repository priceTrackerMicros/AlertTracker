package com.priceAlert.ms.AlertTracker.util;

import java.net.URI;
import java.net.URISyntaxException;

public class MarketplaceUtils {

    public static String getMarketplace(String url) throws IllegalArgumentException {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            
            if (host.contains("amazon.")) {
                return "Amazon";
            }
            throw new IllegalArgumentException("Unsupported marketplace for URL: " + url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }
    }
}

