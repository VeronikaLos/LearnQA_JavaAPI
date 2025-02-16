import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class GetJsonTest {

    @Test
    public void testGetTextFromJson() {

        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String secondAnswer = response.get("messages.message[1]");
        System.out.println(secondAnswer);
    }
}
