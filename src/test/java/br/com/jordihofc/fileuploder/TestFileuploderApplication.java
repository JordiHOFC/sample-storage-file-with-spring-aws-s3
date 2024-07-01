package br.com.jordihofc.fileuploder;

import org.springframework.boot.SpringApplication;

public class TestFileuploderApplication {

	public static void main(String[] args) {
		SpringApplication.from(FileuploderApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
