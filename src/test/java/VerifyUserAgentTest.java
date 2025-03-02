import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyUserAgentTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
    })
    public void testVerifyUserAgent(String userAgent) {
        JsonPath response = RestAssured
                .given()
                .header("user-agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        String platform = response.getString("platform");
        String browser = response.getString("browser");
        String device = response.getString("device");

        String excpectedPlatform = null;
        String excpectedBrowser = null;
        String excpectedDevice = null;

        if (userAgent.contains("Mobile")) {
            excpectedPlatform = "Mobile";
        } else if (userAgent.contains("Googlebot")) {
            excpectedPlatform = "Googlebot";
        } else if (userAgent.contains("Windows")) {
            excpectedPlatform = "Web";
        }

        if (userAgent.contains("CriOS") || userAgent.contains("Chrome")) {
            excpectedBrowser = "Chrome";
        } else if (userAgent.contains("Version")) {
            excpectedBrowser = "No";
        } else if (userAgent.contains("Googlebot")) {
            excpectedBrowser = "Unknown";
        }

        if (userAgent.contains("Android")) {
            excpectedDevice = "Android";
        } else if (userAgent.contains("OS 13_2")) {
            excpectedDevice = "iOS";
        } else if (userAgent.contains("iPhone")) {
            excpectedDevice = "iPhone";
        } else if (userAgent.contains("Windows")) {
            excpectedDevice = "No";
        } else if (userAgent.contains("Googlebot")) {
            excpectedDevice = "Unknown";
        }

        assertEquals(excpectedPlatform, platform, "The platform is not expected");
        assertEquals(excpectedBrowser, browser, "The browser is not expected");
        assertEquals(excpectedDevice, device, "The device is not expected");
    }

}
