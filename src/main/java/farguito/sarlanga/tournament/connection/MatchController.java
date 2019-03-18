package farguito.sarlanga.tournament.connection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.CardFactory;

@CrossOrigin
@RestController
@RequestMapping("match")
public class MatchController {

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private CardFactory cardFactory;

	@GetMapping
	public DefoldResponse online() {
		DefoldResponse response = new DefoldResponse("matchs_online_response");

		response.put("online", matchService.size());		
		
		return response;		
	}
	
	@PostMapping
	public DefoldResponse create(@RequestBody DefoldRequest request) {
		DefoldResponse response = new DefoldResponse("create_match_response");
		
		Match match = matchService.create((int) request.get("essence"), cardFactory.getCards());
				
		response.put("match_id", match.getId());
		response.put("cards", match.getCards());
		
		return response;
	}
	
	
	@PostMapping("is-in-match")
	public DefoldResponse inMatch(@RequestBody Map<String, Object> request) {
		DefoldResponse response = new DefoldResponse("is_in_match_response");
		
		response.put("in_match", matchService.isInMatch((String) request.get("account_id")));
		
		return response;
	}
	
	
	
}
