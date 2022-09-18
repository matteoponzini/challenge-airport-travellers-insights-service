package ai.faire.challenge.airport.api;

import ai.faire.challenge.airport.model.Insights;
import ai.faire.challenge.airport.service.AirportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class AirportController {

  private final AirportService airportService;

  public AirportController(AirportService airportService) {
    this.airportService = airportService;
  }

  @PostMapping(value = "/airport-insights", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Insights> saveTrip(@RequestParam String airportCode,
                                           @RequestParam
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(airportService.insights(airportCode, date));
  }

}
