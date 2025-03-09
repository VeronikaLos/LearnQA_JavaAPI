package CRUD_User;

import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Feature("Delete User")
public class UserDeleteTest extends BaseTestCase {

    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String url = "https://playground.learnqa.ru/api/";
    String url2 = "https://playground.learnqa.ru/api_dev/";

    @Test
    @Severity(CRITICAL)
    public void testDeleteWrongUser() {
        //Login as User 2
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url + "user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // запрос на Delete
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(url + "user/2", header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Severity(NORMAL)
    public void testDeleteUser() {
        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestJson(url + "user/", userData);
        String userId = responseCreateAuth.getString("id");

        // LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url + "user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // Delete
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(url + "user/" + userId, header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        Response responseCheckUser = apiCoreRequests
                .makeGetRequest(
                        url + "user/" + userId,
                        this.header,
                        this.cookie);
        Assertions.assertResponseCodeEquals(responseCheckUser, 404);
        Assertions.assertResponseTextEquals(responseCheckUser, "User not found");
    }

    @Test
    @Issue("Jira-12354")
    public void testDeleteUserByAnotherUser() {
        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest(url + "user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);

        // LOGIN as GENERATED USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url + "user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // Delete user with ID = 120
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(url + "user/120", header, cookie);

        //Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error", "This user can only delete their own account.");
    }
}
