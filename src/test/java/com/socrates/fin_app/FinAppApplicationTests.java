package com.socrates.fin_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FinAppApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            FinAppApplication.main(new String[]{"--spring.profiles.active=test"});
        });
    }

    @Test
    void applicationStartsWithDefaultProfile() {
        assertDoesNotThrow(() -> {
            FinAppApplication.main(new String[]{});
        });
    }

    @Test
    void applicationStartsWithCustomPort() {
        assertDoesNotThrow(() -> {
            FinAppApplication.main(new String[]{"--server.port=8081"});
        });
    }

}
