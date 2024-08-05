package com.noobisoftcontrolcenter.needfortoken;

import com.openelements.hedera.spring.EnableHedera;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHedera
public class NeedfortokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeedfortokenApplication.class, args);
	}

}
