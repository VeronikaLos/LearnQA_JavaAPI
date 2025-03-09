package CRUD_User;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    // тест, который проверяет, что авторизованный пользователь НЕ может получить все параметры в ответ на запрос о другом
    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/3",
                        this.header,
                        this.cookie);

        System.out.println(responseCheckAuth.asString());
        Assertions.assertJsonHasField(responseCheckAuth, "username");
        Assertions.assertJsonHasNotField(responseCheckAuth, "firstName");
        Assertions.assertJsonHasNotField(responseCheckAuth, "lastName");
        Assertions.assertJsonHasNotField(responseCheckAuth, "email");
    }

}
