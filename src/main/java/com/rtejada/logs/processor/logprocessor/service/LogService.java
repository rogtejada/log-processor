package com.rtejada.logs.processor.logprocessor.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.rtejada.logs.processor.logprocessor.repository.LogRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LogService {

  private LogRepository logRepository;

  public LogService(LogRepository logRepository) {
    this.logRepository = logRepository;
  }

  public Map<String, Long> getTopPagesByCountry(final String country) {
    if (country == null || country.isEmpty()) {
      return Collections.emptyMap();
    }

    return logRepository.getTopPagesByCountry(country);
  }

  public Map<String, Long> getTopPages() {
    return logRepository.getTopPages();
  }

  public LocalDate getTopDate() {

    return new Date(logRepository.getTopTimestamp())
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

  public Map<String, Long> getTopPagesByDate(LocalDate date, Boolean isAscending) {

    if (date == null) {
      return Collections.emptyMap();
    }

    long millisecondsStart = date.atTime(LocalTime.MIN).toInstant(ZoneOffset.UTC).toEpochMilli();

    long millisecondsEnd = date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC).toEpochMilli();

    if (isAscending == null) {
      isAscending = true;
    }

    final Comparator<Long> comparator = isAscending ? Comparator.reverseOrder() :
                                        Comparator.naturalOrder();

    return logRepository.getPagesBetweenDates(millisecondsStart, millisecondsEnd)
        .stream()
        .collect(groupingBy(Function.identity(), counting()))
        .entrySet()
        .stream()
        .limit(5)
        .sorted(Map.Entry.comparingByValue(comparator))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                  LinkedHashMap::new));
  }
}
