package farguito.sarlanga.tournament.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import farguito.sarlanga.tournament.cards.actions.BattleCry;
import farguito.sarlanga.tournament.cards.actions.Cleave;
import farguito.sarlanga.tournament.cards.actions.Ensnare;
import farguito.sarlanga.tournament.cards.actions.PoisonSpit;
import farguito.sarlanga.tournament.cards.actions.Punch;
import farguito.sarlanga.tournament.cards.criatures.Lombrisable;
import farguito.sarlanga.tournament.cards.criatures.Ortivactus;
import farguito.sarlanga.tournament.cards.criatures.Peludientes;
import farguito.sarlanga.tournament.cards.criatures.Sapurai;

@Service
@ApplicationScope
public class CardFactory {

	public enum CardType {
		ACTION, CRIATURE
	}
	
	private List<Card> cards = new ArrayList<>();
		
	@PostConstruct
	private void init() {
		this.cards.add(new Card(this.cards.size(), 1, "Ortivactus", Ortivactus.class, CardType.CRIATURE, "HIGH HP, LOW SPEED."));
		this.cards.add(new Card(this.cards.size(), 1, "Sapurai", Sapurai.class, CardType.CRIATURE, "HIGH SPEED."));
		this.cards.add(new Card(this.cards.size(), 1, "Peludientes", Peludientes.class, CardType.CRIATURE, "HIGH ATTACK."));
		this.cards.add(new Card(this.cards.size(), 1, "Lombrisable", Lombrisable.class, CardType.CRIATURE, "LOW HP, VERY HIGH SPEED."));
		
		this.cards.add(new Card(this.cards.size(), 1, "Punch", Punch.class, CardType.ACTION, "Inflicts damage in the objective."));
		this.cards.add(new Card(this.cards.size(), 2, "Poison Spit", PoisonSpit.class, CardType.ACTION, "Inflicts damage and poison in the objective."));	
		this.cards.add(new Card(this.cards.size(), 2, "Battle Cry", BattleCry.class, CardType.ACTION, "Boosts user attack bonus."));	
		this.cards.add(new Card(this.cards.size(), 2, "Ensnare", Ensnare.class, CardType.ACTION, "Inflicts damage, slow and fatigation in the objective."));	
		this.cards.add(new Card(this.cards.size(), 2, "Cleave", Cleave.class, CardType.ACTION, "Inflicts damage in a line of objectives."));				
	}

	public List<Card> getCards() {
		return cards;
	}

	public List<Card> getCriatures(){
		return cards.stream().filter(c -> c.getType().equals(CardType.CRIATURE)).collect(Collectors.toList());
	}
	
	public List<Card> getActions(){
		return cards.stream().filter(c -> c.getType().equals(CardType.ACTION)).collect(Collectors.toList());
	}
	
	
	
	
}
