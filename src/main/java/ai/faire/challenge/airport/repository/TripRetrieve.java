package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TripRetrieve implements TripRepository {

  private final List<Trip> trips = new LinkedList<>();

  @Override
  public Trip saveOrUpdate(Trip trip) {
    var iniSize = trips.size();
    if (trip == null) {
      throw new IllegalArgumentException("Trip must not be null");
    }
    trips.add(trip);
    if (trips.size() > iniSize) {
      return trip;
    } else {
      throw new UnknownError("something went wrong during saving");
    }
  }

  @Override
  public boolean remove(Trip trip) {
    if (trip == null) {
      throw new IllegalArgumentException("Trip must not be null");
    }
    return trips.remove(trip);
  }

  @Override
  public List<Trip> getAll() {
    if (trips.isEmpty()) {
      return Collections.emptyList();
    }
    return new ArrayList<>(trips);
  }

  @Override
  public Trip search(Trip trip) {
    return null;
  }
}
