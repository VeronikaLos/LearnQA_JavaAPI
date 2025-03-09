package CRUD_User;

import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Feature("Edit User")
public class UserEditTest extends BaseTestCase {

    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String url = "https://playground.learnqa.ru/api/";
    String url2 = "https://playground.learnqa.ru/api_dev/";

    String newName = "Changed Name";
    Map<String, String> editData = new HashMap<>();


    @Test
    public void testEditByNonAuthorizedUser() {
        editData.put("firstName", newName);
        // edit
        Response responseEditUser = apiCoreRequests
                .makePutRequestWithoutAuth(url + "user/2", editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Auth token not supplied");
    }

    @Test
    public void testEditAnotherUser() {
        //Login as User 2
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(url + "user/login", authData);

        // вынимаем из ответа Header и Cookie
        header = this.getHeader(responseGetAuth, "x-csrf-token");
        cookie = this.getCookie(responseGetAuth, "auth_sid");

        // Edit user 4
        editData.put("firstName", newName);
        Response responseEditUser = apiCoreRequests
                .makePutRequest(url + "user/4", editData, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    public void testEditEmail() {
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

        // EDIT
        editData.put("email", "vinkotovexample.com");
        Response responseEditUser = apiCoreRequests
                .makePutRequest(url + "user/" + userId, editData, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");
    }

    @Test
    public void testEditFirstName() {
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

        // EDIT
        editData.put("firstName", "a");
        Response responseEditUser = apiCoreRequests
                .makePutRequest(url + "user/" + userId, editData, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "The value for field `firstName` is too short");
    }

}
