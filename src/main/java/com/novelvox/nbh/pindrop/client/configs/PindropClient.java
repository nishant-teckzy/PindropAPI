package com.novelvox.nbh.pindrop.client.configs;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.novelvox.nbh.pindrop.client.models.AuthFeedbackReq;
import com.novelvox.nbh.pindrop.client.models.AuthRes;
import com.novelvox.nbh.pindrop.client.models.AuthResWrapper;

@FeignClient(name = "Pindrop", configuration = PindropFeignConfig.class)
public interface PindropClient {

	@GetMapping("attribute_definition")
	public String getAttributeDef();

	@PutMapping("call/transfer/{interactionId}/{destination}")
	public void callTransfer(URI baseUrl, @PathVariable String interactionId, @PathVariable String destination);

	@PostMapping("authenticate")
	public AuthResWrapper authenticate(@RequestParam String interaction_id, @RequestParam String account_alias);

	@PostMapping("authenticate")
	public AuthResWrapper authenticateWithoutAccountAlias(@RequestParam String interaction_id);

	@PostMapping(value = "account_interactions/feedback", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void authFeedback(AuthFeedbackReq authFeedbackReq);

}
