package farguito.sarlanga.tournament;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Ortivactus;
import farguito.sarlanga.tournament.cards.Punch;
import farguito.sarlanga.tournament.cards.Sapurai;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

@RestController
public class CombatController {

	private CombatSystem system;
	
	//test HARCODEO RABIOSO
	@GetMapping("/iniciar")
	public String iniciar() {
		List<Team> teams = new ArrayList<>();

		//equipo 1
		List<Character> teamOneChars = new ArrayList<>();
		List<Action> sapuraiActions = new ArrayList<>();
		sapuraiActions.add(new Punch());
		teamOneChars.add(new Character(1, new Sapurai(), sapuraiActions));
		Team one = new Team(teamOneChars);

		//equipo 2
		List<Character> teamTwoChars = new ArrayList<>();
		List<Action> ortivactusActions = new ArrayList<>();
		ortivactusActions.add(new Punch()); //aca deberian ir las cartas nomas, no funcionalidad hay que separar action en dos como Character / Criature
		teamTwoChars.add(new Character(2, new Ortivactus(), ortivactusActions));
		Team two = new Team(teamTwoChars);
		
		teams.add(one);
		teams.add(two);
		
		this.system = new CombatSystem(teams);

		return "iniciar ok";		
	}
	
	@GetMapping("/golpear")
	public String golpear() {
		this.system.getTeams().get(1).getCharacters().get(0);
		Action punch = new Punch();
		this.system.prepareAction(
				punch, this.system.getTeams().get(1).getCharacters().get(0));
		this.system.executeAction(punch);
		
		this.system.applyEffects();
		
		return "golpear ok";
	}
}