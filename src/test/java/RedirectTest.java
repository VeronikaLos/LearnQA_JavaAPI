import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RedirectTest {

    @Test
    public void testGetRestAssuredRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        response.prettyPrint();
        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }

    @Test
    public void testGetRestAssuredRedirect2() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int redirectCount = 0;
        final int maxRedirects = 10;

        while (redirectCount < maxRedirects) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();

            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                System.out.println("Окончательный URL: " + url);
                break;
            }
            url = response.getHeader("Location");
            System.out.println(url);
            redirectCount++;
        }

        System.out.println(redirectCount);
    }

}
