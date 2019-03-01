package farguito.sarlanga.tournament.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

public class Match {

	private int id;
	private int teamNumber = 0;
	
	private String owner;
	private List<String> players = new ArrayList<>();
	private int essence;
	private CombatSystem system;
	private Map<Integer, Card> cards = new LinkedHashMap<>();
	private List<Team> teams = new ArrayList<>();
	private Map<String, TeamDTO> player_teamDTO = new HashMap<>();
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
	
	public boolean start() {
		Collection<TeamDTO> teams = player_teamDTO.values();
		if(!teams.stream().anyMatch(t -> !t.isReady())) {
			this.state = "PLAYING";
			teams.stream().forEach(t -> {
				t.setTeamNumber(nextTeamNumber());
				this.teams.add(t.create());
				this.player_teamNumber.put(t.getOwner(), t.getTeamNumber());
			});
			this.system = new CombatSystem(this.teams);
			
			return true;			
		}		
		return false;
	}

	public boolean confirmTeam(String accountId) {
		if(this.player_teamDTO.containsKey(accountId)) {
			TeamDTO team = this.player_teamDTO.get(accountId);
			if(!team.getCharacters().isEmpty() 
			&& team.validateCharacters() 
			&& team.getEssence() <= this.essence) {
				team.setReady(true);
				return true;		
			}	
		}		
		return false;
	}
	
	public boolean unconfirmTeam(String accountId) {
		if(this.player_teamDTO.containsKey(accountId)) {
			TeamDTO team = this.player_teamDTO.get(accountId);
			team.setReady(false);		
			return true;			
		}		
		return false;
	}
	
	
	
	public void addTeam(String accountId, Team team) {
		//hacer la validacion mas piola
		if(!this.player_teamNumber.containsKey(accountId)) {
			int teamNumber = nextTeamNumber();
			team.setNumber(teamNumber);		
			team.getCharacters().stream().forEach(c -> { c.setTeam(teamNumber); });
			team.setOwner(accountId);			
			this.teams.add(team);
			this.player_teamNumber.put(accountId, teamNumber);
		}			
	}
	
	public TeamDTO addPlayer(String accountId) {
		TeamDTO team = new TeamDTO();
		team.setOwner(accountId);
		this.players.add(accountId);
		this.player_teamDTO.put(accountId, team);
		return team;
	}

	public void addTeam(TeamDTO team) {
		String accountId = team.getOwner();
		this.players.add(accountId);
		this.player_teamDTO.put(accountId, team);		
	}
	
	public void deletePlayer(String accountId) {
		this.players.remove(accountId);
		if(this.player_teamNumber.containsKey(accountId)) {
			this.teams.remove(this.player_teamNumber.get(accountId).intValue());
			this.player_teamNumber.remove(accountId);
			this.player_teamDTO.remove(accountId);
		}
	}
	
	
	public Integer getTeamNumber(String accountId) {
		return this.player_teamNumber.get(accountId);
	}	
	
	public TeamDTO getTeamDTO(String accountId) {
		return this.player_teamDTO.get(accountId);
	}
	
	private Integer nextTeamNumber() {
		this.teamNumber++;
		return this.teamNumber;
	}
	
	public List<String> getPlayers(){
		return this.players;
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
