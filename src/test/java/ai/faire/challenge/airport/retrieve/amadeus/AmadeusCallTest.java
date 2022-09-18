package ai.faire.challenge.airport.retrieve.amadeus;

import ai.faire.challenge.airport.model.Trip;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Disabled
class AmadeusCallTest {

  @Autowired
  AmadeusCall amadeusCall;

  @Test
  void callNotRetrieveEmptyObject() {
    var response = amadeusCall.call(new Trip("JFK", "LIN", LocalDate.of(2022, 9, 10), LocalDate.of(2022, 9, 15)));
    assertNotNull(response);
  }


  @Test
  void callRetrieve200() {
    var response = amadeusCall.call(new Trip("JFK", "LIN", LocalDate.of(2022, 9, 10), LocalDate.of(2022, 9, 15)));
    assertEquals(response.get().getResponse().getStatusCode(), 200);
  }
}
