package farguito.sarlanga.tournament.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

public class Match {

	private int id;
	private int teamNumber = 1;
	private String owner;
	private List<String> players = new ArrayList<>();
	private int essence;
	private CombatSystem system;
	private Map<Integer, Card> cards = new LinkedHashMap<>();
	private List<Team> teams = new ArrayList<>();
	private Map<String, TeamDTO> player_teamDto = new HashMap<>();
	private Map<String, Integer> player_teamNumber = new HashMap<>();
	
	private String state;
	
	public Match(int essence, List<Card> cards) {
		this.state = "WAITING";
		this.essence = essence;
		
		//generacion random iria aca
		for(Card c : cards) {
			this.cards.put(c.getId(), c);
		}
	}
	
	public void start() {
		this.state = "PLAYING";
		this.system = new CombatSystem(teams);		
	}
	
	public void addTeam(String accountId, Team team) {
		//hacer la validacion mas piola
		if(!this.player_teamNumber.containsKey(accountId)) {
			team.setTeamNumber(this.teamNumber);		
			team.getCharacters().stream().forEach(c -> { c.setTeam(this.teamNumber); });
			team.setOwner(accountId);
			this.teams.add(team);
			this.player_teamNumber.put(accountId, this.teamNumber);
			this.teamNumber++;
		}			
	}
	
	public void addPlayer(String accountId) {
		this.players.add(accountId);
		this.player_teamDto.put(accountId, new TeamDTO());
	}
	
	public void deletePlayer(String accountId) {
		this.players.remove(accountId);
		if(this.player_teamNumber.containsKey(accountId)) {
			this.teams.remove(this.player_teamNumber.get(accountId).intValue());
			this.player_teamNumber.remove(accountId);
			this.player_teamDto.remove(accountId);
		}
	}

	public Integer getPlayerTeamNumber(String accountId) {
		return this.player_teamNumber.get(accountId);
	}	
	
	public TeamDTO getPlayerTeamDTO(String accountId) {
		return this.player_teamDto.get(accountId);
	}
	
	public int getEssence() {
		return essence;
	}		
	public Map<Integer, Card> getCards() {
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
