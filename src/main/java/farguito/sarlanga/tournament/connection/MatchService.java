package farguito.sarlanga.tournament.connection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.cards.CardFactory;

public class MatchService {

	public static String IA = "IA";
	private CardFactory cards;
	
	private int matchId = 0;
	private Map<Integer, Match> matchs = new HashMap<>();
	private Map<String, Integer> account_match = new HashMap<>();
	
	private Map<String, Integer> account_queue = new HashMap<>();
	private Map<Integer, Map<String, TeamDTO>> queue_team = new HashMap<>();
	
	public MatchService(CardFactory cardFactory) {
		this.cards = cardFactory;
	}
	
	public Integer size() {
		return matchs.size();
	}
	
	public void remove(Integer id) {
		for(String accountId : matchs.get(id).getPlayers()){
			this.account_match.remove(accountId);
		}
		matchs.remove(id);
	}
	
	public Match create(int essence) {
		return this.create(essence, cards.getCards());
	}

	public Match create(int essence, List<Card> cards) {
		Match match = new Match(essence, cards);
		int id = nextId();
		match.setId(id);

		this.matchs.put(id, match);

		return match;
	}
	
	public boolean start(Integer id) {
		Match m = get(id);
		if(m.start()) {
			m.getPlayers().stream().forEach(p -> {
				this.account_match.put(p, id);
			});
			
			return true;
		}
		return false;
	}
	
	public Match get(Integer id) {
		return this.matchs.get(id);
	}
	
	public Match get(String accountId) {
		return this.matchs.get(this.account_match.get(accountId));
	}

	private int nextId() {
		if (matchId > 1000000000)
			matchId = 0;

		matchId++;

		return this.matchId;
	}

	public void addToQueue(Integer essence, TeamDTO team) {
		String accountId = team.getOwner();
		this.account_queue.put(accountId, essence);

		Map<String, TeamDTO> teams;
		if(this.queue_team.containsKey(essence)) {
			teams = this.queue_team.get(essence);
	    } else {
			teams = new LinkedHashMap<>();
			this.queue_team.put(essence, teams);
		}
		
		teams.put(accountId, team);		
	}
	
	public void removeFromQueue(String accountId) {
		if(this.account_queue.containsKey(accountId)) {
			Integer queue = this.account_queue.get(accountId);
							
			this.queue_team.get(queue).remove(accountId);
			this.account_queue.remove(accountId);
		}
	}

	public boolean queueReady(String accountId) {
		Integer queue = this.account_queue.get(accountId);
		
		if(this.queue_team.get(queue).size() > 1) {
			Match match = create(queue);

			int numberOfTeams = 2;
			
			TeamDTO team = this.queue_team.get(queue).get(accountId);	
			
			for(int i = 0; i < numberOfTeams; i++) {
				removeFromQueue(team.getOwner());
				match.addTeam(team);
				match.confirmTeam(team.getOwner());
				
				if(this.queue_team.get(queue).values().iterator().hasNext())
					team = this.queue_team.get(queue).values().iterator().next();			
			}
			
			this.start(match.getId());
			
			return true;
		}
		return false;
		
	}

	public void createIAMatch(Integer essence, TeamDTO team) {
		Match match = create(essence);
		
		match.addTeam(team);
		match.confirmTeam(team.getOwner());
		
		TeamDTO iaTeam = iaTeam();
		match.addTeam(iaTeam);
		match.confirmTeam(iaTeam.getOwner());
		
		this.start(match.getId());		
	}
	
	private TeamDTO iaTeam() {
		TeamDTO team = new TeamDTO();
		team.setOwner(MatchService.IA);
		
		team.addCharacter(1, 1, cards.getCreatures().get(3));
		team.addCharacter(1, 2, cards.getCreatures().get(3));
		team.addCharacter(2, 2, cards.getCreatures().get(3));
		team.addCharacter(1, 3, cards.getCreatures().get(3));
		
		team.getCharacter(1).addAction(cards.getActions().get(0));
		team.getCharacter(2).addAction(cards.getActions().get(0));
		team.getCharacter(3).addAction(cards.getActions().get(0));
		team.getCharacter(4).addAction(cards.getActions().get(0));
		
		return team;
	}

	public boolean isInMatch(String accountId) {
		return account_match.containsKey(accountId);		
	}
}
