package com.novelvox.nbh.pindrop.client.models;

import java.util.ArrayList;
import java.util.List;

import com.novelvox.nbh.commons.Util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class AuthRes {
	private String account_alias;
	private String fraud_score;
	private ArrayList<String> fraud_risk_reasons;
	private String authentication_score;
	private ArrayList<String> authentication_reasons;
	private String authentication_result;
	private String request_id;

	@Getter(AccessLevel.NONE)
	private String questions;

	public List<String> getQuestions() {

		String alertLevel = this.authentication_reasons.get(0);

		return switch (Risk.getRisk(alertLevel)) {
		case RED:
			yield Util.getRandomQuestions(4);
		case ORANGE:
			yield Util.getRandomQuestions(3);
		case YELLOW:
			yield Util.getRandomQuestions(2);
		case GREEN:
			yield Util.getRandomQuestions(1);
		};

	}

}
