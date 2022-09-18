package ai.faire.challenge.airport;

import ai.faire.challenge.airport.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TripServiceApplicationIntegrationTest {


  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int randomServerPort;

  private URI baseUri;


  @BeforeEach
  void setUp() {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().scheme("http")
      .host("localhost")
      .port(randomServerPort)
      .path("/");
    baseUri = uriBuilder.build().toUri();
  }

  @Test
  void tripAdd() {
    Trip expected = new Trip(
      "JDK",
      "LIN",
      LocalDate.of(2020, 10, 1),
      LocalDate.of(2020, 10, 11)
    );
    var response = restTemplate.postForEntity(baseUri + "/trip", expected, Trip.class);
    Trip tripBody = response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(tripBody).isNotNull();
    assertThat(tripBody.getDepartureDate()).isEqualTo(expected.getDepartureDate());
    assertThat(tripBody.getDestinationAirportCode()).isEqualTo(expected.getDestinationAirportCode());
    assertThat(tripBody.getOriginAirportCode()).isEqualTo(expected.getOriginAirportCode());
    assertThat(tripBody.getReturnDate()).isEqualTo(expected.getReturnDate());
  }

  @Test
  void tripRemove() {
    Trip expected = new Trip(
      "JDK",
      "LIN",
      LocalDate.of(2020, 10, 1),
      LocalDate.of(2020, 10, 11)
    );
    var responseCreate = restTemplate.postForEntity(baseUri + "/trip", expected, Trip.class);
    assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    restTemplate.delete(baseUri + "/trip", expected);
  }


  @Test
  void tripsGetAll() {
    List<Trip> trips = List.of(new Trip(
        "JDK",
        "LIN",
        LocalDate.of(2020, 10, 1),
        LocalDate.of(2020, 10, 11)
      ), new Trip(
        "JDK",
        "LIN",
        LocalDate.of(2020, 10, 1),
        LocalDate.of(2020, 10, 11)
      )
    );
    trips.forEach(trip -> {
      var response = restTemplate.postForEntity(baseUri + "/trip", trip, Trip.class);
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    });

    var remoteTrips = restTemplate.getForEntity(baseUri + "/trip", Trip[].class);
    assertThat(remoteTrips.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(List.of(Objects.requireNonNull(remoteTrips.getBody()))).isEqualTo(trips);
  }

}
