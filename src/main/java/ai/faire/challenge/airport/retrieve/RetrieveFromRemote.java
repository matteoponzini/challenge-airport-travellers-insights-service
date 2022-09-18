package ai.faire.challenge.airport.retrieve;

import java.util.Optional;

public interface RetrieveFromRemote<T, E> {
  Optional<T> call(E parameters);
}
