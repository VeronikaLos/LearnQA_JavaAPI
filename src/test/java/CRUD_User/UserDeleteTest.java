package CRUD_User;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testDeleteWrongUser() {
        //Login as User 2
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // запрос на Delete
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    public void testDeleteUser() {
        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        // LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // Delete
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        Response responseCheckUser = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie);
        Assertions.assertResponseCodeEquals(responseCheckUser, 404);
        Assertions.assertResponseTextEquals(responseCheckUser, "User not found");
    }

    @Test
    public void testDeleteUserByAnotherUser() {
        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);

        // LOGIN as GENERATED USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // Delete user with ID = 120
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/120", header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error", "This user can only delete their own account.");
    }
}
