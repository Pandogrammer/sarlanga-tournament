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

	private List<Card> cards = new ArrayList<>();
		
	@PostConstruct
	public void init() {
		this.cards.add(new CriatureCard(this.cards.size(), 1, new Ortivactus(), "HIGH HP, LOW SPEED."));
		this.cards.add(new CriatureCard(this.cards.size(), 1, new Sapurai(), "HIGH SPEED."));
		this.cards.add(new CriatureCard(this.cards.size(), 1, new Peludientes(), "HIGH ATTACK."));
		this.cards.add(new CriatureCard(this.cards.size(), 1, new Lombrisable(), "LOW HP, VERY HIGH SPEED."));
		
		this.cards.add(new ActionCard(this.cards.size(), 1, new Punch(), "Inflicts damage in the objective."));
		this.cards.add(new ActionCard(this.cards.size(), 2, new PoisonSpit(), "Inflicts damage and poison in the objective."));	
		this.cards.add(new ActionCard(this.cards.size(), 2, new BattleCry(), "Boosts user attack bonus."));	
		this.cards.add(new ActionCard(this.cards.size(), 2, new Ensnare(), "Inflicts damage, slow and fatigation in the objective."));	
		this.cards.add(new ActionCard(this.cards.size(), 2, new Cleave(), "Inflicts damage in a line of objectives."));				
	}

	public List<Card> getCards() {
		return cards;
	}

	public List<Card> getCriatures(){
		return cards.stream().filter(c -> c instanceof CriatureCard).collect(Collectors.toList());
	}
	
	public List<Card> getActions(){
		return cards.stream().filter(c ->  c instanceof ActionCard).collect(Collectors.toList());
	}
	
	
	
	
}
