package CRUD_User;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Feature("Get User data")
public class UserGetTest extends BaseTestCase {

    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String url = "https://playground.learnqa.ru/api/";
    String url2 = "https://playground.learnqa.ru/api_dev/";


    @Test
    @Description("This test verify that one user can't get data about another user")
    @Tag ("Regression")
    public void testGetUserDetailsAboutAnotherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url + "user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        url + "user/3",
                        this.header,
                        this.cookie);

        System.out.println(responseCheckAuth.asString());
        Assertions.assertJsonHasField(responseCheckAuth, "username");
        Assertions.assertJsonHasNotField(responseCheckAuth, "firstName");
        Assertions.assertJsonHasNotField(responseCheckAuth, "lastName");
        Assertions.assertJsonHasNotField(responseCheckAuth, "email");
    }

}
