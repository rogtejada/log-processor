package com.rtejada.logs.processor.logprocessor.service;

import java.io.IOException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

  private RestHighLevelClient client;

  public HealthService(RestHighLevelClient client) {
    this.client = client;
  }

  public Boolean isHealthy() {
    try {
      return client.ping(RequestOptions.DEFAULT);
    } catch (IOException e) {
      return false;
    }
  }
}

