package CRUD_User;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.getRandomName;

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotovexample.com");
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithMissedOneParameter(String condition) {
        Map<String, String> userData = new HashMap<>();
        if (condition.equals("email")) {
            userData.put("password", "123");
            userData.put("username", "learnqa");
            userData.put("firstName", "learnqa");
            userData.put("lastName", "learnqa");
            Response responseForCheck = apiCoreRequests.makePostRequest(
                    "https://playground.learnqa.ru/api/user/",
                    userData);
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: email");
            Assertions.assertResponseCodeEquals(responseForCheck, 400);
        } else if (condition.equals("password")) {
            userData.put("email", "ivanov@example.com");
            userData.put("username", "learnqa");
            userData.put("firstName", "learnqa");
            userData.put("lastName", "learnqa");
            Response responseForCheck = apiCoreRequests.makePostRequest(
                    "https://playground.learnqa.ru/api/user/",
                    userData);
            Assertions.assertResponseCodeEquals(responseForCheck, 400);
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: password");
        } else if (condition.equals("username")) {
            userData.put("email", "ivanov@example.com");
            userData.put("password", "123");
            userData.put("firstName", "learnqa");
            userData.put("lastName", "learnqa");
            Response responseForCheck = apiCoreRequests.makePostRequest(
                    "https://playground.learnqa.ru/api/user/",
                    userData);
            Assertions.assertResponseCodeEquals(responseForCheck, 400);
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: username");
        } else if (condition.equals("firstName")) {
            userData.put("email", "ivanov@example.com");
            userData.put("password", "123");
            userData.put("username", "learnqa");
            userData.put("lastName", "learnqa");
            Response responseForCheck = apiCoreRequests.makePostRequest(
                    "https://playground.learnqa.ru/api/user/",
                    userData);
            Assertions.assertResponseCodeEquals(responseForCheck, 400);
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: firstName");
        } else if (condition.equals("lastName")) {
            userData.put("email", "ivanov@example.com");
            userData.put("password", "123");
            userData.put("username", "learnqa");
            userData.put("firstName", "learnqa");
            Response responseForCheck = apiCoreRequests.makePostRequest(
                    "https://playground.learnqa.ru/api/user/",
                    userData);
            Assertions.assertResponseCodeEquals(responseForCheck, 400);
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: lastName");
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
    }

    @Test
    public void testCreateUserWithShortName() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "Ivanov@example.com");
        userData.put("password", "123");
        userData.put("username", "a");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    public void testCreateUserWithLongName() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "Ivanov@example.com");
        userData.put("password", "123");
        userData.put("username", getRandomName(251));
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }


}
