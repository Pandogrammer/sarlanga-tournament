package farguito.sarlanga.tournament.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.connection.Account;
import farguito.sarlanga.tournament.connection.DefoldResponse;

@RestController
@RequestMapping("account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;

	@PostMapping
	public DefoldResponse generate(@RequestBody String secret) {
		DefoldResponse response = new DefoldResponse("generate_account_response");
		
		Account account = accountService.create(secret);
				
		response.put("account_id", account.getAccountId());
		
		return response;
	}
	
}
