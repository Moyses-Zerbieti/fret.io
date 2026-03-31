package com.fret.io.auth_service;

import com.fret.io.auth_service.config.TestMailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.context.annotation.Import(TestMailConfig.class)
class AuthServiceApplicationTests {

	@Test
	void contextLoads() {
	}
}