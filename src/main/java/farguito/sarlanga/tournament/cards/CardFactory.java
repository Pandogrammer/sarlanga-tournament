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
import farguito.sarlanga.tournament.cards.creatures.Lombrisable;
import farguito.sarlanga.tournament.cards.creatures.Ortivactus;
import farguito.sarlanga.tournament.cards.creatures.Peludientes;
import farguito.sarlanga.tournament.cards.creatures.Sapurai;

public class CardFactory {

	private List<Card> cards = new ArrayList<>();
		
	@PostConstruct
	public void init() {
		this.cards.add(new CreatureCard(this.cards.size(), 1, new Ortivactus(), "HIGH HP, LOW SPEED."));
		this.cards.add(new CreatureCard(this.cards.size(), 1, new Sapurai(), "HIGH SPEED."));
		this.cards.add(new CreatureCard(this.cards.size(), 1, new Peludientes(), "HIGH ATTACK."));
		this.cards.add(new CreatureCard(this.cards.size(), 1, new Lombrisable(), "LOW HP, VERY HIGH SPEED."));
		
		this.cards.add(new ActionCard(this.cards.size(), 1, new Punch()));
		this.cards.add(new ActionCard(this.cards.size(), 2, new PoisonSpit()));	
		this.cards.add(new ActionCard(this.cards.size(), 2, new BattleCry()));	
		this.cards.add(new ActionCard(this.cards.size(), 2, new Ensnare()));	
		this.cards.add(new ActionCard(this.cards.size(), 2, new Cleave()));				
	}

	public List<Card> getCards() {
		return cards;
	}

	public List<Card> getCreatures(){
		return cards.stream().filter(c -> c instanceof CreatureCard).collect(Collectors.toList());
	}
	
	public List<Card> getActions(){
		return cards.stream().filter(c ->  c instanceof ActionCard).collect(Collectors.toList());
	}
	
	
	
	
}
