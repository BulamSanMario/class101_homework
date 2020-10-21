package net.class101.homework1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Homework1Application {

	public static void main(String[] args) {
	    SpringApplication app = new SpringApplication(Homework1Application.class);
        app.run(args);
	}

}
