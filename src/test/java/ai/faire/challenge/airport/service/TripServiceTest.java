package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TripServiceTest {

  @Autowired
  private TripService tripService;


  @Test
  void saveShouldFailWhenTripIsNull(){
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(null));
    assertEquals(error.getMessage(),"Trip must not be null");
  }

  @Test
  void saveShouldFailWhenOriginAirportCodeIsNull(){
    var trip = new Trip(null, "LIN", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"Origin airport code must not be null or empty");
  }

  @Test
  void saveShouldFailWhenOriginAirportCodeIsEmptyString(){
    var trip = new Trip("", "LIN", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"Origin airport code must not be null or empty");
  }

  @Test
  void saveShouldFailWhenDestinationAirportCodeIsNull(){
    var trip = new Trip("JFK", null, LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"Destination airport code must not be null or empty");
  }

  @Test
  void saveShouldFailWhenDestinationAirportCodeIsEmptyString(){
    var trip = new Trip("JFK", "", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"Destination airport code must not be null or empty");
  }

  @Test
  void saveShouldFailWhenDepartureDateIsNull(){
    var trip = new Trip("JFK", "LIN", null,LocalDate.of(2020,10,15) );
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"Departure date must not be null");
  }

  @Test
  void saveShouldFailWhenReturnDateIsNull(){
    var trip = new Trip("JFK", "LIN", LocalDate.of(2020,10,12),null);
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"Return date must not be null");
  }

  @Test
  void saveShouldFailWhenDepartureDateIsAfterReturnDate(){
    var trip = new Trip("JFK", "LIN", LocalDate.of(2020,11,12),LocalDate.of(2020,10,15));
    var error = assertThrows(IllegalArgumentException.class, () -> tripService.saveOrUpdate(trip));
    assertEquals(error.getMessage(),"The departure date cannot be after the return date");
  }

}
