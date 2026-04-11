package com.fret.io.api_gateway.api_gateway;

import com.fret.io.api_gateway.api_gateway.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}