package farguito.sarlanga.tournament.cards;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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


		Properties prop = new Properties();
		
		try {
			prop.load(CardFactory.class.getResourceAsStream("/creature_cards.properties"));

			System.out.println(prop.getProperty("ortivactus.description"));
			this.cards.add(new CreatureCard(this.cards.size(), 3, new Ortivactus()
					, prop.getProperty("ortivactus.description"), prop.getProperty("ortivactus.lore")));
			this.cards.add(new CreatureCard(this.cards.size(), 3, new Sapurai()
					, prop.getProperty("sapurai.description"), prop.getProperty("sapurai.lore")));
			this.cards.add(new CreatureCard(this.cards.size(), 3, new Peludientes()
					, prop.getProperty("peludientes.description"), prop.getProperty("peludientes.lore")));
			this.cards.add(new CreatureCard(this.cards.size(), 3, new Lombrisable()
					, prop.getProperty("lombrisable.description"), prop.getProperty("lombrisable.lore")));

			prop.load(CardFactory.class.getResourceAsStream("/action_cards.properties"));
			
			this.cards.add(new ActionCard(this.cards.size(), 1, new Punch(), prop.getProperty("punch.description")));
			this.cards.add(new ActionCard(this.cards.size(), 1, new PoisonSpit(), prop.getProperty("poison_spit.description")));	
			this.cards.add(new ActionCard(this.cards.size(), 1, new BattleCry(), prop.getProperty("battle_cry.description")));	
			this.cards.add(new ActionCard(this.cards.size(), 1, new Ensnare(), prop.getProperty("ensnare.description")));	
			this.cards.add(new ActionCard(this.cards.size(), 1, new Cleave(), prop.getProperty("cleave.description")));
					
		} catch (Exception e) {
			e.printStackTrace();
		}
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
