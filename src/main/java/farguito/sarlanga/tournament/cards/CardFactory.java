package farguito.sarlanga.tournament.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import farguito.sarlanga.tournament.cards.actions.BattleCry;
import farguito.sarlanga.tournament.cards.actions.Cleave;
import farguito.sarlanga.tournament.cards.actions.Ensnare;
import farguito.sarlanga.tournament.cards.actions.PoisonSpit;
import farguito.sarlanga.tournament.cards.actions.Punch;
import farguito.sarlanga.tournament.cards.criatures.Lombrisable;
import farguito.sarlanga.tournament.cards.criatures.Ortivactus;
import farguito.sarlanga.tournament.cards.criatures.Peludientes;
import farguito.sarlanga.tournament.cards.criatures.Sapurai;

public class CardFactory {

	private List<Card> cards = new ArrayList<>();
	
	public CardFactory() {
		this.cards.add(new Card(this.cards.size(), 1, "Ortivactus", Ortivactus.class,  "Criature"));
		this.cards.add(new Card(this.cards.size(), 1, "Sapurai", Sapurai.class, "Criature"));
		this.cards.add(new Card(this.cards.size(), 1, "Peludientes", Peludientes.class, "Criature"));
		this.cards.add(new Card(this.cards.size(), 1, "Lombrisable", Lombrisable.class, "Criature"));
		
		this.cards.add(new Card(this.cards.size(), 1, "Punch", Punch.class, "Action"));
		this.cards.add(new Card(this.cards.size(), 2, "Poison Spit", PoisonSpit.class, "Action"));	
		this.cards.add(new Card(this.cards.size(), 2, "Battle Cry", BattleCry.class, "Action"));	
		this.cards.add(new Card(this.cards.size(), 2, "Ensnare", Ensnare.class, "Action"));	
		this.cards.add(new Card(this.cards.size(), 2, "Cleave", Cleave.class, "Action"));		
		
	}

	public List<Card> getCards() {
		return cards;
	}

	public List<Card> getCriatures(){
		return cards.stream().filter(c -> c.getType().equals("Criature")).collect(Collectors.toList());
	}
	
	public List<Card> getActions(){
		return cards.stream().filter(c -> c.getType().equals("Action")).collect(Collectors.toList());
	}
	
	
	
	
}
