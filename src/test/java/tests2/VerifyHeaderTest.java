package tests2;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyHeaderTest {

    @Test
    public void testVerifyHeader() {
        Response responseGetHeader = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers headers = responseGetHeader.getHeaders();
        assertTrue (headers.hasHeaderWithName("x-secret-homework-header"), "Response doesn't have 'x-secret-homework-header' header");
        String headerValue = headers.getValue("x-secret-homework-header");
        assertEquals("Some secret value",headerValue);
    }
}
