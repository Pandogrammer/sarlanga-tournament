package farguito.sarlanga.tournament.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.connection.Match;

@Service
@ApplicationScope
public class MatchService {

	private int matchId = 0;
	private Map<Integer, Match> matchs = new HashMap<>();
	private Map<String, Integer> account_match = new HashMap<>();
	
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
}
