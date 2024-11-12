package com.novelvox.nbh.commons;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.springframework.util.ResourceUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Util {

	private Util() {
		throw new IllegalStateException("Util Class cannot be cast");
	}

	private static Random random = new Random();



	/**
	 * @Author Nishant Tiwari
	 * @param quesSize
	 * @return
	 * @throws IOException
	 */
	public static List<String> getRandomQuestions(int quesSize) {
		List<String> randQues = new ArrayList<>();
		List<String> questions= new ArrayList<>();
		try {
			File file = ResourceUtils.getFile("classpath:questions.dat");
			questions = Files.readAllLines(file.toPath());
			int[] arr = random.ints(0, questions.size() -1)
					.distinct()
					.limit(quesSize)
					.toArray();

			for (int i : arr) {
				randQues.add(questions.get(i));
			}

		} catch (IOException e) {
			log.atError().withThrowable(e).log("Error Occurred| {}", e.getMessage());
		}
		System.out.println("UTILS: Random Question List : "+randQues);
		return randQues;
	}

}
