package farguito.sarlanga.tournament.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.DefoldResponse;
import farguito.sarlanga.tournament.connection.TeamDTO;
import farguito.sarlanga.tournament.connection.TeamValidationException;

@CrossOrigin
@RestController
@RequestMapping("team")
public class TeamController {

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private CardFactory cardFactory;
	
	@PostMapping("confirm")
	public DefoldResponse confirm(@RequestBody Map request) {
		DefoldResponse response = new DefoldResponse("confirm_team_response");
		
		List<Map<String, Object>> teamMap = (List<Map<String, Object>>) request.get("team");
		Integer essence = (Integer) request.get("essence");
		String accountId = (String) request.get("account_id");
		boolean versus = (Boolean) request.get("versus");
		
		TeamDTO team =  new TeamDTO();
		team.setOwner(accountId);
		
		try {
			for(int i = 0; i < teamMap.size(); i++) {
	    		team.addCharacter((int) teamMap.get(i).get("line")
						 ,(int) teamMap.get(i).get("position")
						 , cardFactory.getCriatures().get((int) teamMap.get(i).get("card_id")));
	    		
	    		List<Integer> actions =  (List<Integer>) teamMap.get(i).get("actions");
	    		
	    		for(int j = 0; j < actions.size(); j++)
	    			team.getCharacter(i+1).addAction(cardFactory.getActions().get(actions.get(j)));
			}
			
			try{
				team.validate(essence);
			
				if(versus) {
					matchService.addToQueue(essence, team);
				} else {
					matchService.createIAMatch(essence, team);
				}
				response.put("success", true);
			} catch (TeamValidationException e) {
				response.put("reason", e.getMessage());		
				response.put("success", false);	
			}
		} catch (Exception e) {
			response.put("reason", "");		
			response.put("success", false);				
		}
		
		
		return response;
	}
	
}
