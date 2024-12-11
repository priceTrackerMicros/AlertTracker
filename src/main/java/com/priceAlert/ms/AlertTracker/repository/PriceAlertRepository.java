package com.priceAlert.ms.AlertTracker.repository;

import com.priceAlert.ms.AlertTracker.models.PriceAlert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PriceAlertRepository extends MongoRepository<PriceAlert, String> {
    List<PriceAlert> findByTargetReached(boolean targetReached);
}


