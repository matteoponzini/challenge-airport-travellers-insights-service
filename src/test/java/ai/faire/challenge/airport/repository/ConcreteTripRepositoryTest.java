package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ConcreteTripRepositoryTest {


  private ConcreteTripRepository concreteTripRepository;

  @BeforeEach
  void init() {
    concreteTripRepository = new ConcreteTripRepository();
  }

  @Test
  void saveWithTripRecordSucceed() {
    var tripExpected = new Trip("JFK", "LIN", LocalDate.of(2020, 10, 12), LocalDate.of(2020, 10, 15));
    var tripSaved = concreteTripRepository.saveOrUpdate(tripExpected);
    assertEquals(tripExpected, tripSaved);
  }

  @Test
  void saveShouldFailWhenTripIsNull() {
    var error = assertThrows(IllegalArgumentException.class, () -> concreteTripRepository.saveOrUpdate(null));
    assertEquals(error.getMessage(), "Trip must not be null");
  }


  @Test
  void removeWithTripRecordSucceed() {
    var tripExpected = new Trip("JFK", "LIN", LocalDate.of(2020, 10, 12), LocalDate.of(2020, 10, 15));
    concreteTripRepository.saveOrUpdate(tripExpected);
    assertTrue(concreteTripRepository.remove(tripExpected));
  }

  @Test
  void removeWithTripListsEmptyIsFalse() {
    var tripExpected = new Trip("JFK", "LIN", LocalDate.of(2020, 10, 12), LocalDate.of(2020, 10, 15));
    assertFalse(concreteTripRepository.remove(tripExpected));
  }

  @Test
  void removeShouldFailWhenTripIsNull() {
    var error = assertThrows(IllegalArgumentException.class, () -> concreteTripRepository.remove(null));
    assertEquals(error.getMessage(), "Trip must not be null");
  }

  @Test
  void getAll() {
    var tripsExpected = IntStream.range(0, 5)
      .mapToObj((i) -> new Trip("JFK", "LIN", LocalDate.of(2020, 10, 12), LocalDate.of(2020, 10, 15)))
      .collect(Collectors.toList());
    tripsExpected.forEach(trip -> concreteTripRepository.saveOrUpdate(trip));
    assertEquals(tripsExpected, concreteTripRepository.getAll());
  }
}
