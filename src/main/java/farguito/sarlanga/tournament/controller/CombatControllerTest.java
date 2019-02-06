package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

@RestController
@RequestMapping("test")
public class CombatControllerTest {

	private CombatSystem system;
	private CardFactory cards = new CardFactory();
	
	@GetMapping("start")
	private void init() {
		List<Team> teams = new ArrayList<>();
		
		teams.add(buildTeam(1));
		teams.add(buildTeam(3));
		
		this.system = new CombatSystem(teams);		
	}

	@GetMapping("action/{team}/{id}")
	private void action(@PathVariable int team, @PathVariable int id) {		
		if(this.system.getWinningTeam() == -1) {
			Action action = this.system.getActiveCharacter().getActions().get(0);
			this.system.prepareAction(action, this.system.getTeams().get(team).getCharacters().get(id));
			if(this.system.validateObjectives(action)) {
				this.system.executeAction(action);
				this.system.nextTurn();
			} else {
				System.out.println("Target incorrecto.");
			}					
		} 
	}	

	@GetMapping("info")
	private void info() {
		Character character = this.system.getActiveCharacter();
		int line = character.getLine();
		int position = character.getPosition();
		
		System.out.println("Line: "+line+" | Position: "+position+" | Action: "+character.getActions().get(0).getName());
	}
	

	@GetMapping("info/{team}/{id}")
	private void infoChar(@PathVariable int team, @PathVariable int id) {
		Character character = this.system.getTeams().get(team).getCharacters().get(id);
		
		int line = character.getLine();
		int position = character.getPosition();
		
		System.out.println("Name: "+character.getName()+" | Team: "+character.getTeam()+" | Line: "+line+" | Position: "+position);
	}

	
	
	
	private Team buildTeam(int n) {
		TeamDTO team = new TeamDTO();
		team.setTeamNumber(n);
		int j = 1;
		for(int i = n-1; i <= n; i++) {
			team.addCharacter(j, 1, cards.getCriatures().get(i));
			team.getCharacter(j).addAction(cards.getActions().get(i));
			j++;
		}
		return team.create();		
	}
}