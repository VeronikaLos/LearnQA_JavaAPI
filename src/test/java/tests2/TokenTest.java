package tests2;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TokenTest {

    @Test
    public void testToken() throws InterruptedException {
        String baseUrl = "https://playground.learnqa.ru/ajax/api/longtime_job";

        // 1. Получаем токен и время ожидания
        JsonPath response = sendGetRequest(baseUrl);

        response.prettyPrint();
        String token = response.get("token");
        int seconds = response.get("seconds");
        if (token == null) {
            throw new RuntimeException("Токен не получен!");
        }

        // 2. Проверяем статус до завершения задачи
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        JsonPath response2 = sendGetRequest(baseUrl, params);
        response2.prettyPrint();
        String status = response2.get("status");

        // 3. Если задача не готова — ждем
        if (status.equals("Job is NOT ready")) {
            Thread.sleep(seconds * 1000L);
        } else {
            System.out.println("Incorrect Status");
        }

        // 4. Проверяем результат после ожидания
        String result;
        do {
            JsonPath response3 = sendGetRequest(baseUrl, params);
            response3.prettyPrint();
            result = response3.get("result");
        } while (result != null && status.equals("Job is ready"));
    }

    private JsonPath sendGetRequest(String url, Map<String, String> params) {
        return RestAssured.given().queryParams(params).get(url).jsonPath();
    }

    private JsonPath sendGetRequest(String url) {
        return RestAssured.given().get(url).jsonPath();
    }


}
