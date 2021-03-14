package com.rtejada.logs.processor.logprocessor.controller;

import com.rtejada.logs.processor.logprocessor.service.LogService;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class LogController {

  private final LogService logService;

  public LogController(LogService logService) {
    this.logService = logService;
  }

  @GetMapping("/top-pages")
  public Map<String, Long> getTopPages() {
    return logService.getTopPages();
  }

  @GetMapping("/top-pages/country/{country}")
  public Map<String, Long> getTopPagesByCountry(@PathVariable("country") String country) {
    return logService.getTopPagesByCountry(country);
  }

  @GetMapping("/top-pages/date/{date}")
  public Map<String, Long> getTopPagesByDate(@PathVariable("date") String date,
                                             @RequestParam(
                                                 value = "isAscending",
                                                 defaultValue = "true") Boolean isAscending) {
    return logService.getTopPagesByDate(LocalDate.parse(date), isAscending);
  }

  @GetMapping("/top-date")
  public LocalDate getTopDate() {
    return logService.getTopDate();
  }

}
