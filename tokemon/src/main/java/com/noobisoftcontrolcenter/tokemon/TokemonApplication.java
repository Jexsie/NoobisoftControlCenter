package com.noobisoftcontrolcenter.tokemon;

import com.openelements.hedera.spring.EnableHedera;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHedera
public class TokemonApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokemonApplication.class, args);
	}

}
