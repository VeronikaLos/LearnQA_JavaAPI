package tests2;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyCookieTest {

    @Test
    public void testVerifyCookie() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        response.prettyPrint();

        List <Cookie> cookies =  response.getDetailedCookies().asList();

        assertTrue (!cookies.isEmpty(), "Cookie isn't received");

        String name = cookies.get(0).getName();
        String value = cookies.get(0).getValue();

        assertEquals ("HomeWork", name, "Incorrect cookie name");
        assertEquals ("hw_value", value, "Incorrect cookie value");


    }
}
