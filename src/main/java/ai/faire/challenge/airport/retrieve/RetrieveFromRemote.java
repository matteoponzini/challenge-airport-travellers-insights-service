package ai.faire.challenge.airport.retrieve;

import com.amadeus.exceptions.ResponseException;

public interface RetrieveFromRemote<T, E> {
  T call(E e) throws ResponseException;
}
