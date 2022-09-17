package ai.faire.challenge.airport.api;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.service.TripService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("trip")
public class TripController {

  private final TripService tripService;

  public TripController(TripService tripService) {
    this.tripService = tripService;
  }

  @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Trip> saveTrip(@RequestBody Trip trip) {
    return ResponseEntity.ok(tripService.saveOrUpdate(trip));
  }

  @DeleteMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<String> removeTrip(@RequestBody Trip trip) {
    return (tripService.remove(trip)) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<Trip>> getAll() {
    return ResponseEntity.ok(tripService.getAll());
  }
}
