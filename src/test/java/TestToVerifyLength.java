import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestToVerifyLength {

    @Test
    public void testVerifyLength() {
        String hello = "Hello, world";

        assertTrue(hello.length() > 15, "Incorrect length");
    }
}
