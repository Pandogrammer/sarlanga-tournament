package farguito.sarlanga.tournament.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.connection.TeamDTO;

public class MatchService {

	private CardFactory cards;
	
	private int matchId = 0;
	private Map<Integer, Match> matchs = new HashMap<>();
	private Map<String, Integer> account_match = new HashMap<>();
	
	private Map<String, Integer> account_queue = new HashMap<>();
	private Map<Integer, Map<String, TeamDTO>> queue_team = new HashMap<>();
	
	public MatchService(CardFactory cardFactory) {
		this.cards = cardFactory;
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

	public void addToQueue(Integer essence, String accountId, TeamDTO team) {
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
		Integer queue = this.account_queue.get(accountId);
						
		this.queue_team.get(queue).remove(accountId);
		this.account_queue.remove(accountId);
	}

	public void queueReady(String accountId) {
		Integer queue = this.account_queue.get(accountId);
		
		if(this.queue_team.get(queue).size() > 1) {
			TeamDTO firstTeam = this.queue_team.get(queue).get(accountId);			
			removeFromQueue(accountId);
			
			TeamDTO secondTeam = this.queue_team.get(queue).values().iterator().next();
			
			Match match = create(queue);
			//match.addPlayer(firstTeam.getOwner?)
			
		}
		
	}
}
