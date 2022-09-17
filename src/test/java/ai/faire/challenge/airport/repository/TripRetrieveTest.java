package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
  void remove() {

  }

  @Test
  void testRemove() {
  }

  @Test
  void getAll() {
  }

  @Test
  void search() {
  }

  @Test
  void testSearch() {
  }
}
