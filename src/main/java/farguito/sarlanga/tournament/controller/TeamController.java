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

@CrossOrigin
@RestController
@RequestMapping("team")
public class TeamController {

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private CardFactory cardFactory;
	
	@PostMapping("validate")
	public DefoldResponse validate(@RequestBody Map request) {
		DefoldResponse response = new DefoldResponse("team_validate_response");
		
		List<Map<String, Object>> teamMap = (List<Map<String, Object>>) request.get("team");
		Integer essence = (Integer) request.get("essence");
		String accountId = (String) request.get("account_id");
		boolean iaMatch = (Boolean) request.get("ia_match");
		
		TeamDTO team =  new TeamDTO();
		team.setOwner(accountId);
		
		for(int i = 0; i < teamMap.size(); i++) {
    		team.addCharacter((int) teamMap.get(i).get("line")
					 ,(int) teamMap.get(i).get("position")
					 , cardFactory.getCriatures().get((int) teamMap.get(i).get("card_id")));
    		
    		List<Integer> actions =  (List<Integer>) teamMap.get(i).get("actions");
    		
    		for(int j = 0; j < actions.size(); j++)
    			team.getCharacter(i+1).addAction(cardFactory.getActions().get(actions.get(j)));
		}
		
		boolean valid = team.validate(essence);
		
		if(valid) {
			if(iaMatch) {
				matchService.createIAMatch(essence, team);
			} else {
				matchService.addToQueue(essence, team);
			}
		}
		
		response.put("success", valid);
		
		return response;
	}
	
}
