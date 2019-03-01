package farguito.sarlanga.tournament.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.DefoldResponse;
import farguito.sarlanga.tournament.connection.Match;

@CrossOrigin
@RestController
@RequestMapping("match")
public class MatchController {

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private CardFactory cardFactory;
	
	
	@PostMapping
	public DefoldResponse create(@RequestBody DefoldRequest request) {
		DefoldResponse response = new DefoldResponse("create_match_response");
		
		Match match = matchService.create((int) request.get("essence"), cardFactory.getCards());
				
		response.put("match_id", match.getId());
		response.put("cards", match.getCards());
		
		return response;
	}
	
	
}
