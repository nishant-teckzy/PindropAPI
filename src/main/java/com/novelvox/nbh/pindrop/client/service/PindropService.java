package com.novelvox.nbh.pindrop.client.service;

import com.novelvox.nbh.pindrop.client.configs.PindropClient;
import com.novelvox.nbh.pindrop.client.models.AuthFeedbackReq;
import com.novelvox.nbh.pindrop.client.models.AuthRes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Log4j2
@Service
public class PindropService {

	private final PindropClient pindropClient;
	private final URI callControllURl;

	@Autowired
	public PindropService(PindropClient genesysClient, @Value("${pindrop.call_control.url}") URI callControllURl) {
		this.pindropClient = genesysClient;
		this.callControllURl = callControllURl;
	}

	public String getAttributeDefinitions() {
		log.info("Fetching attribute definitions...");
		try {
			String attrs = pindropClient.getAttributeDef();
			log.info("Successfully fetched attribute definitions.");
			return attrs;
		} catch (Exception e) {
			log.error("Error fetching attribute definitions: {}", e.getMessage());
			throw e;
		}
	}

	public void transferCall(String interactionId, String destination) {
		log.info("Transferring call with interaction ID: {} to destination: {}", interactionId, destination);
		try {
			pindropClient.callTransfer(callControllURl, interactionId, destination);
		} catch (Exception e) {
			log.error("Error transferring call: {}", e.getMessage());
			throw e;
		}
	}

	public AuthRes authenticate(String interactionId, String accountAlias) {
		log.info("Authenticating with interaction ID: {} and account alias: {}", interactionId, accountAlias);
		try {
			return pindropClient.authenticate(interactionId, accountAlias).getData();
		} catch (Exception e) {
			log.error("Error during authentication: {}", e.getMessage());
			throw e;
		}
	}

	public AuthRes authenticate(String interactionId) {
		log.info("Authenticating with interaction ID: {}", interactionId);
		try {
			AuthRes authRes = pindropClient.authenticateWithoutAccountAlias(interactionId).getData();

			log.info("AuthRes| {}", authRes.getAuthentication_result());

			return authRes;

		} catch (Exception e) {
			log.error("Error during authentication: {}", e.getMessage());
			throw e;
		}
	}

	public void sendAuthFeedback(AuthFeedbackReq authFeedbackReq) {
		log.info("Sending authentication feedback...");
		try {
			pindropClient.authFeedback(authFeedbackReq);
		} catch (Exception e) {
			log.error("Error sending authentication feedback: {}", e.getMessage());
			throw e;
		}
	}
}
