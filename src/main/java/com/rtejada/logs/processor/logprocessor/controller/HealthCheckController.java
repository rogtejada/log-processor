package com.rtejada.logs.processor.logprocessor.controller;

import com.rtejada.logs.processor.logprocessor.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheckController {

  private HealthService healthService;

  public HealthCheckController(HealthService healthService) {
    this.healthService = healthService;
  }

  @GetMapping
  public Boolean healthcheck() {
    return healthService.isHealthy();
  }

}
