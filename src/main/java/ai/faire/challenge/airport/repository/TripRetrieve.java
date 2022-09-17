package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TripRetrieve implements TripRepository{

  private final List<Trip> trips = new LinkedList<>();

  @Override
  public Trip saveOrUpdate(Trip trip) {
    if(trip == null) throw new IllegalArgumentException("Trip must not be null");
    if(!StringUtils.hasText(trip.getUid())) trip.setUid(UUID.randomUUID().toString());
    if(!StringUtils.hasText(trip.getDestinationAirportCode())) throw new IllegalArgumentException("Destination airport code must not be null or empty");
    if(!StringUtils.hasText(trip.getOriginAirportCode())) throw new IllegalArgumentException("Origin airport code must not be null or empty");
    if(trip.getDepartureDate() == null ) throw new IllegalArgumentException("Departure date must not be null");
    if(trip.getReturnDate() == null) throw new IllegalArgumentException("Return date must not be null");
    if(trip.getDepartureDate().isAfter(trip.getReturnDate())) throw new IllegalArgumentException("The departure date cannot be after the return date");
    trips.add(trip);
    if(trips.contains(trip)) return trip;
    else throw new UnknownError("something went wrong during saving");
  }

  @Override
  public Trip remove(Trip trip) {
    return null;
  }

  @Override
  public Trip remove(String uid) {
    return null;
  }

  @Override
  public List<Trip> getAll() {
    return new ArrayList<>(trips);
  }

  @Override
  public Trip search(String uid) {
    return null;
  }

  @Override
  public Trip search(Trip trip) {
    return null;
  }
}
