package com.Api;

import com.Api.service.RabbitMqProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "rabbitmq.queue.name=test-queue")
class ApiApplicationTests {

	@MockitoBean
	private RabbitMqProducer rabbitMqProducer;

	@MockBean
	private JavaMailSender mailSender;




	@Test
	void contextLoads() {
	}

}
