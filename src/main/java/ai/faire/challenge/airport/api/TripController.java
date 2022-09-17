package ai.faire.challenge.airport.api;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/trip")
public class TripController {

  private final TripService tripService;

  public TripController(TripService tripService){
    this.tripService = tripService;
  }

  @PostMapping
  public ResponseEntity<Trip> saveTrip(Trip trip){
    return ResponseEntity.ok(tripService.saveOrUpdate(trip));
  }

}
