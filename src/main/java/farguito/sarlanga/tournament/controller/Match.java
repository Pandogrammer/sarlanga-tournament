package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

public class Match {

	private int teamNumber = 1;
	private String owner;
	private List<String> players = new ArrayList<>();
	private int essence;
	private CombatSystem system;
	private List<Card> cards = new ArrayList<>();
	private List<Team> teams = new ArrayList<>();
	private Map<String, TeamDTO> player_team = new HashMap<>();
	
	private String state;
	
	public Match(int essence, List<Card> cards) {
		this.state = "WAITING";
		this.essence = essence;
		
		//generacion random iria aca
		this.cards.addAll(cards);		
	}
	
	public void start() {
		this.state = "PLAYING";
		this.system = new CombatSystem(teams);		
	}
	
	public void addTeam(String accountId, List<Character> teamCharacters) {
		Team newTeam = new Team(teamCharacters);
		newTeam.setTeamNumber(this.teamNumber);		this.teamNumber++;		
		newTeam.setOwner(accountId);
		this.teams.add(newTeam);
	}
	
	public void addPlayer(String accountId) {
		this.players.add(accountId);
		this.player_team.put(accountId, new TeamDTO());
	}
	
	public void deletePlayer(String accountId) {
		this.players.remove(accountId);
		if(this.player_team.containsKey(accountId)) {
			this.teams.remove(this.player_team.get(accountId));
			this.player_team.remove(accountId);
		}
	}
	
	public TeamDTO getPlayerTeam(String accountId) {
		return this.player_team.get(accountId);
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
}
