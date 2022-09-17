package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TripRetrieveTest {


  private TripRetrieve tripRetrieve;

  @BeforeEach
  void init() {
    tripRetrieve = new TripRetrieve();
  }

  @Test
  void saveWithTripRecordSucceed(){
    var tripExpected = new Trip("JFK", "LIN", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    var tripSaved = tripRetrieve.saveOrUpdate(tripExpected);
    assertEquals(tripExpected, tripSaved);
  }

  @Test
  void saveShouldFailWhenTripIsNull(){
    var error = assertThrows(IllegalArgumentException.class, () -> tripRetrieve.saveOrUpdate(null));
    assertEquals(error.getMessage(),"Trip must not be null");
  }


  @Test
  void removeWithTripRecordSucceed(){
    var tripExpected = new Trip("JFK", "LIN", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    tripRetrieve.saveOrUpdate(tripExpected);
    assertTrue(tripRetrieve.remove(tripExpected));
  }

  @Test
  void removeWithTripListsEmptyIsFalse(){
    var tripExpected = new Trip("JFK", "LIN", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) );
    assertFalse(tripRetrieve.remove(tripExpected));
  }

  @Test
  void removeShouldFailWhenTripIsNull(){
    var error = assertThrows(IllegalArgumentException.class, () -> tripRetrieve.remove(null));
    assertEquals(error.getMessage(),"Trip must not be null");
  }

  @Test
  void getAll() {
    var tripsExpected = IntStream.range(0, 5)
      .mapToObj((i)->new Trip("JFK", "LIN", LocalDate.of(2020,10,12),LocalDate.of(2020,10,15) ))
      .collect(Collectors.toList());
    tripsExpected.forEach(trip ->tripRetrieve.saveOrUpdate(trip));
    assertEquals(tripsExpected,tripRetrieve.getAll());
  }
}
