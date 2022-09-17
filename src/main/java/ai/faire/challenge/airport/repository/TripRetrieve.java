package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TripRetrieve implements TripRepository{

  private final List<Trip> trips = new LinkedList<>();

  @Override
  public Trip saveOrUpdate(Trip trip) {
    if(trip == null) throw new IllegalArgumentException("Trip must not be null");
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
