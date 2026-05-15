package br.com.vittasync.vittasync;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class VittasyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(VittasyncApplication.class, args);
	}

}
