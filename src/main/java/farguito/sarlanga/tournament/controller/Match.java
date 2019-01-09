package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

public class Match {

	int essence;
	CombatSystem system;
	List<Card> cards = new ArrayList<>();
	List<Team> teams = new ArrayList<>();
	
	String state;
	
	public Match(int essence, List<Card> cards) {
		this.state = "WAITING";
		this.essence = essence;
		
		//generacion random iria aca
		this.cards.addAll(cards);		
	}
	
	public void iniciar() {
		this.state = "PLAYING";
		this.system = new CombatSystem(teams);		
	}
	
	public void addTeam(List<Character> teamCharacters) {
		this.teams.add(new Team(this.teams.size(), teamCharacters));
	}
	
	public int getEssence() {
		return essence;
	}		
	public List<Card> getCards() {
		return cards;
	}	
	public List<Team> getTeams() {
		return teams;
	}
	public CombatSystem getSystem() {
		return system;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}