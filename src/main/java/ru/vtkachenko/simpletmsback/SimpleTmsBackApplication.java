package ru.vtkachenko.simpletmsback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SimpleTmsBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleTmsBackApplication.class, args);
	}

}
