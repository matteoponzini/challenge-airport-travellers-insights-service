package ai.faire.challenge.airport;

import ai.faire.challenge.airport.model.Insights;
import ai.faire.challenge.airport.model.Trend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationTest")
class AirportTravellersInsightsServiceApplicationIntegrationTest {


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
  void airportInsightsOkAndSomeData() {
    String url = baseUri + "/airport-insights?airportCode={airportCode}&date={date}";
    Map<String, String> parameters = Map.of(
      "airportCode", "JFK", "date", "2020-01-01"
    );
    var response = restTemplate.postForEntity(url, null, Insights.class, parameters);
    Insights insightsBody = response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(insightsBody).isNotNull();
    assertThat(insightsBody.airportCode()).isEqualTo(parameters.get("airportCode"));
    assertThat(insightsBody.date()).isEqualTo(parameters.get("date"));
  }


  @Test
  void airportAirportTrendOkAndSomeData() {
    String url = baseUri + "/airport-trend?airportCode=JFK&startDate=2020-12-10&endDate=2020-12-10";
    var response = restTemplate.getForEntity(url, Trend[].class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody())[0].date()).isEqualTo("2020-12-10");
  }

}
