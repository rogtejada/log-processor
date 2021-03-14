package com.rtejada.logs.processor.logprocessor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rtejada.logs.processor.logprocessor.repository.LogRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogServiceTest {

  private LogRepository logRepository;
  private LogService service;

  @BeforeEach
  public void setUp() {
    logRepository = mock(LogRepository.class);
    service = new LogService(logRepository);
  }

  @Test
  public void shouldReturnTopPagesByCountry() {
    final Map<String, Long> mockResult = new HashMap<>();
    mockResult.put("/products", 5L);
    mockResult.put("/cars", 3L);
    mockResult.put("/houses", 2L);

    when(logRepository.getTopPagesByCountry("Brazil")).thenReturn(mockResult);

    Map<String, Long> result = service.getTopPagesByCountry("Brazil");

    assertEquals(mockResult, result);

    verify(logRepository).getTopPagesByCountry("Brazil");
  }

  @Test
  public void shouldReturnEmptyListWhenCountryIsNull() {
    assertTrue(service.getTopPagesByCountry(null).isEmpty());

    verify(logRepository, never()).getTopPagesByCountry(any());
  }

  @Test
  public void shouldReturnEmptyListWhenCountryIsEmpty() {
    assertTrue(service.getTopPagesByCountry("").isEmpty());

    verify(logRepository, never()).getTopPagesByCountry(any());
  }

  @Test
  public void shouldReturnTopPages() {
    final Map<String, Long> mockResult = new HashMap<>();
    mockResult.put("/products", 5L);
    mockResult.put("/cars", 3L);
    mockResult.put("/houses", 2L);

    when(logRepository.getTopPages()).thenReturn(mockResult);

    Map<String, Long> result = service.getTopPages();

    assertEquals(mockResult, result);

    verify(logRepository).getTopPages();
  }

  @Test
  public void shouldReturnTopDates() {
    Long timestamp = 1615690290090L;

    when(logRepository.getTopTimestamp()).thenReturn(timestamp);

    LocalDate result = service.getTopDate();

    assertEquals(LocalDate.of(2021, 3, 13), result);

    verify(logRepository).getTopTimestamp();
  }

  @Test
  public void shouldGetTopPagesByDate() {
    final List<String> mockResult = Arrays.asList("/products", "/products", "/houses");

    LocalDate date = LocalDate.of(2021, 3, 13);
    when(logRepository.getPagesBetweenDates(1615593600000L, 1615679999999L)).thenReturn(mockResult);

    Map<String, Long> result = service.getTopPagesByDate(date, true);

    assertEquals(2, result.get("/products"));
    assertEquals(1, result.get("/houses"));
    assertEquals("/products", result.keySet().toArray()[0]);
    assertEquals("/houses", result.keySet().toArray()[1]);

    verify(logRepository).getPagesBetweenDates(1615593600000L, 1615679999999L);
  }

  @Test
  public void shouldGetTopPagesByDateWithtoutAscendingInfo() {
    final List<String> mockResult = Arrays.asList("/products", "/products", "/houses");

    LocalDate date = LocalDate.of(2021, 3, 13);
    when(logRepository.getPagesBetweenDates(1615593600000L, 1615679999999L)).thenReturn(mockResult);

    Map<String, Long> result = service.getTopPagesByDate(date, null);

    assertEquals(2, result.get("/products"));
    assertEquals(1, result.get("/houses"));
    assertEquals("/products", result.keySet().toArray()[0]);
    assertEquals("/houses", result.keySet().toArray()[1]);

    verify(logRepository).getPagesBetweenDates(1615593600000L, 1615679999999L);
  }

  @Test
  public void shouldGetTopPagesByDateReverseOrder() {
    final List<String> mockResult = Arrays.asList("/products", "/products", "/houses");

    LocalDate date = LocalDate.of(2021, 3, 13);
    when(logRepository.getPagesBetweenDates(1615593600000L, 1615679999999L)).thenReturn(mockResult);

    Map<String, Long> result = service.getTopPagesByDate(date, false);

    assertEquals(2, result.get("/products"));
    assertEquals(1, result.get("/houses"));
    assertEquals("/products", result.keySet().toArray()[1]);
    assertEquals("/houses", result.keySet().toArray()[0]);

    verify(logRepository).getPagesBetweenDates(1615593600000L, 1615679999999L);
  }

  @Test
  public void shouldGetTopPagesByDateLimiting5pages() {
    final List<String> mockResult = Arrays.asList("/products", "/cars", "/houses", "/bikes",
                                                  "/bags", "/clothes", "/eletronics");

    LocalDate date = LocalDate.of(2021, 3, 13);
    when(logRepository.getPagesBetweenDates(1615593600000L, 1615679999999L)).thenReturn(mockResult);

    Map<String, Long> result = service.getTopPagesByDate(date, true);

    assertEquals(5, result.size());

    verify(logRepository).getPagesBetweenDates(1615593600000L, 1615679999999L);
  }

  @Test
  public void shouldReturnEmptyCollectionWhenNoDateProvided() {
    assertTrue(service.getTopPagesByDate(null, true).isEmpty());
  }
}