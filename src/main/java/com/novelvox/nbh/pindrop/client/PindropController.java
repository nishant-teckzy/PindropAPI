package com.novelvox.nbh.pindrop.client;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.novelvox.nbh.commons.Util;
import com.novelvox.nbh.pindrop.client.models.Risk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novelvox.nbh.pindrop.client.models.AuthFeedbackReq;
import com.novelvox.nbh.pindrop.client.models.AuthRes;
import com.novelvox.nbh.pindrop.client.models.ServiceResponse;
import com.novelvox.nbh.pindrop.client.models.ServiceResponse.Status;
import com.novelvox.nbh.pindrop.client.service.PindropService;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/pindrop")
public class PindropController {

	private static final String TAG = "Pindrop_Controller";

	@Autowired
	private PindropService pindropService;

	@GetMapping(path = "/test")
	public ResponseEntity<String> test() {
		log.info("{}: Invoked /test endpoint", TAG);
		return ResponseEntity.ok("It Workss");
	}

	@GetMapping("/attributes")
	public ResponseEntity getAttributeDefinitions() {
		log.info("{}: Invoked /attributes endpoint", TAG);
		try {
			String attrs = pindropService.getAttributeDefinitions();
			log.info("Returning attribute definitions.");
			return ResponseEntity.ok().build();
		} catch (FeignException e) {
			log.error("{}: Failed to invoke /attributes : {}", TAG, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("{}: Failed to invoke /attributes: {}", TAG, e);
			throw e;
		}
	}

	@GetMapping("/call/transfer/{interactionId}/{destination}")
	public ResponseEntity transferCall(@PathVariable String interactionId, @PathVariable String destination) {
		log.info("{}: Invoked /call/transfer/ endpoint with interaction ID: {} to destination: {}", TAG, interactionId,
				destination);
		try {
			pindropService.transferCall(interactionId, destination);
			log.info("Call successfully transferred.");
			return ResponseEntity.ok().build();
		} catch (FeignException e) {
			log.error("{}: Failed to invoke /call/transfer/: {}", TAG, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("{}: Failed to invoke /call/transfer/: {}", TAG, e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/questions")
	public ResponseEntity getQuestions(@RequestParam String risk_color){
		log.info("{}: Invoked /questions endpoint with risk_color: {}", TAG,risk_color);
		ServiceResponse response = null;
		try {
			List<String> quesList= switch (Risk.getRisk(risk_color)) {
				case RED:
					yield Util.getRandomQuestions(4);
				case ORANGE:
					yield Util.getRandomQuestions(3);
				case YELLOW:
					yield Util.getRandomQuestions(2);
				case GREEN:
					yield Util.getRandomQuestions(1);
			};

			response = new ServiceResponse(Status.ZERO, quesList);
			log.info("{}: Random Question List Size:{}",TAG,quesList.size());
			return ResponseEntity.ok(response);

		}catch (Exception e) {
			log.error("{}: Failed to invoke /questions : {}", TAG, e);
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getMessage());

		}

	}

	@GetMapping("/authenticate")
	public ServiceResponse authenticate(@RequestParam String interaction_id,
			@RequestParam Optional<String> account_alias) {
		log.info("{}: Invoked /authenticate endpoint with interaction ID: {} and account alias: {}", TAG,
				interaction_id, account_alias);
		ServiceResponse response = null;
		try {
			AuthRes authRes;
			if (account_alias.isEmpty() || account_alias.get().trim().isEmpty()) {
				authRes = pindropService.authenticate(interaction_id);
			} else {
				authRes = pindropService.authenticate(interaction_id, account_alias.get());
			}

			response = new ServiceResponse(Status.ONE, authRes);
			return log.traceExit(response);
		} catch (FeignException e) {
			log.error("{}: Failed to invoke /authenticate : {}", TAG, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("{}: Failed to invoke /authenticate : {}", TAG, e);
			e.printStackTrace();
			return log.traceExit(response);
		}
	}

	@GetMapping("/account_interactions/feedback")
	public ResponseEntity sendAuthFeedback(@RequestParam String interactionId, @RequestParam String accountAlias,
			@RequestParam String externalVerification) {
		log.info(
				"{}: Invoked /account_interactions/feedback endpoint with interactionId: {} ,accountAlias:{},externalVerification:{}  ",
				TAG, interactionId, accountAlias, externalVerification);
		try {

			AuthFeedbackReq authFeedbackReqBody = new AuthFeedbackReq(interactionId, accountAlias,
					externalVerification);
			pindropService.sendAuthFeedback(authFeedbackReqBody);
			log.info("{}: Feedback sent successfully.",TAG);
			return ResponseEntity.ok().build();
		} catch (FeignException e) {
			log.error("{}: Failed to invoke /account_interactions/feedback : {}", TAG, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("{}: Failed to invoke /account_interactions/feedback : {}", TAG, e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
