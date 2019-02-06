package farguito.sarlanga.tournament.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.connection.DefoldResponse;

@RestController
@RequestMapping("account")
public class AccountController {

	@PostMapping
	public DefoldResponse generate() {
		DefoldResponse response = new DefoldResponse("generate_account_response");
		
		String newAccountId = UUID.randomUUID().toString().replaceAll("-", "");
		
		response.put("account_id", newAccountId);
		
		return response;
	}
	
}
