package com.rtejada.logs.processor.logprocessor.repository;

import java.util.List;
import java.util.Map;

public interface LogCustomRepository {

  Map<String, Long> getTopPagesByCountry(final String country);

  Map<String, Long> getTopPages();

  Long getTopTimestamp();

  List<String> getPagesBetweenDates(long millisecondsStart, long millisecondsEnd);
}
