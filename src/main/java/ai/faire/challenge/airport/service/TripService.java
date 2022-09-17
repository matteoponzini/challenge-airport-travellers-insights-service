package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRetrieve;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TripService {

  private final TripRetrieve tripRetrieve;

  public TripService(TripRetrieve tripRetrieve) {
    this.tripRetrieve = tripRetrieve;
  }

  public Trip saveOrUpdate(Trip trip) {
    isValidTripOrError(trip);
    return tripRetrieve.saveOrUpdate(trip);
  }

  public boolean remove(Trip trip) {
    isValidTripOrError(trip);
    return tripRetrieve.remove(trip);
  }

  public List<Trip> getAll() {
    return tripRetrieve.getAll();
  }

  private void isValidTripOrError(Trip trip) {
    if (trip == null) {
      throw new IllegalArgumentException("Trip must not be null");
    }
    if (!StringUtils.hasText(trip.getDestinationAirportCode())) {
      throw new IllegalArgumentException("Destination airport code must not be null or empty");
    }
    if (!StringUtils.hasText(trip.getOriginAirportCode())) {
      throw new IllegalArgumentException("Origin airport code must not be null or empty");
    }
    if (trip.getDepartureDate() == null) {
      throw new IllegalArgumentException("Departure date must not be null");
    }
    if (trip.getReturnDate() == null) {
      throw new IllegalArgumentException("Return date must not be null");
    }
    if (trip.getDepartureDate().isAfter(trip.getReturnDate())) {
      throw new IllegalArgumentException("The departure date cannot be after the return date");
    }
  }

}
