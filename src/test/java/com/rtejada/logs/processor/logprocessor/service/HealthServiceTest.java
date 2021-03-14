package com.rtejada.logs.processor.logprocessor.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.IOException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthServiceTest {

  private RestHighLevelClient client;
  private HealthService service;

  @BeforeEach
  public void setUp() {
    client = mock(RestHighLevelClient.class);
    service = new HealthService(client);
  }

  @Test
  public void shouldReturnTrueWhenElasticSearchIsHealthy() throws IOException {
    when(client.ping(RequestOptions.DEFAULT)).thenReturn(true);

    assertTrue(service.isHealthy());
  }

  @Test
  public void shouldReturnFalseWhenElasticSearchIsUnhealthy() throws IOException {
    when(client.ping(RequestOptions.DEFAULT)).thenReturn(false);

    assertFalse(service.isHealthy());
  }


  @Test
  public void shouldReturnFalseWhenPingFailed() throws IOException {
    when(client.ping(RequestOptions.DEFAULT)).thenThrow(IOException.class);

    assertFalse(service.isHealthy());
  }
}