package com.lucca.fileSorter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileSorterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileSorterApplication.class, args);
	}

}
