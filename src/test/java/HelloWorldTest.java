import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HelloWorldTest {

    @Test
    public void testHelloWorld() {
        // в переменной response будет хранится инфа об ответе на респонс
        Response response = RestAssured
                // создаем Get Запрос на адрес апи
                .get("https://playground.learnqa.ru/api/hello")
                // просим RestAssured вернуть нам результат
                .andReturn();
        // распечатывает текст ответа в удобном формате
        // это Json в котором будет содержаться приветственная строка
        response.prettyPrint();
    }
}