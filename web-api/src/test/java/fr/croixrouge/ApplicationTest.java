package fr.croixrouge;

import fr.croixrouge.config.InDBMockRepositoryConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({InDBMockRepositoryConfig.class})
public class ApplicationTest {

    @Test
    @DisplayName("Test that the application context loads successfully.")
    void contextLoads() {
        // This test will pass if the application context loads successfully.
    }
}
