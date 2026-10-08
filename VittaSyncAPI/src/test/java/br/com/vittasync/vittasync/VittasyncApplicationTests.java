package br.com.vittasync.vittasync;


import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;


@SpringBootTest
class VittasyncApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertThat(context).isNotNull();
	}

	@Test
	void testApplicationBeanPresente() {
		assertThat(context.containsBean("vittasyncApplication")).isTrue();
	}

	@Test
	void testMainIniciaAplicacao() {
		try (MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {
			String[] args = new String[]{"--teste"};

			VittasyncApplication.main(args);

			spring.verify(() -> SpringApplication.run(VittasyncApplication.class, args));
		}
	}
}
