package com.novelvox.nbh;

import com.novelvox.nbh.commons.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.novelvox.nbh.pindrop.client.models.Risk;


@EnableFeignClients
@SpringBootApplication
public class NvPindropClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NvPindropClientApplication.class, args);
//		Util.getRandomQuestions(5);
	}
	@Value("${genesys.otp.fromAddress}") String fromAddress;
	@Override
	public void run(String... args) throws Exception {

		System.out.println(fromAddress);
		
	}
}
