package com.priceAlert.ms.AlertTracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    @GetMapping("/klk")
    public String hello() {
        return "klk";
    }

}
