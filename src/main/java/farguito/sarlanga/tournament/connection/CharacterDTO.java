package farguito.sarlanga.tournament.connection;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.cards.Creature;
import farguito.sarlanga.tournament.combat.Character;

public class CharacterDTO {

	private int id;
	private int line;
	private int position;
	private Card character;
	private List<Card> actions = new ArrayList<>();
	
	
	public CharacterDTO() {}
	
	public Character create() {
		Character character = new Character((Creature) this.character.getObject());
		
		List<Action> actions = new ArrayList<>();
		
		this.actions.stream().forEach(a -> { actions.add((Action) a.getObject()); });
		character.setActions(actions);
		character.setPosition(position);
		character.setLine(line);
		character.setId(id);
		return character;
	}
	
	public boolean containsAction(Card action) {
		return this.actions.contains(action);
	}
	
	public void addAction(Card action) {
		this.actions.add(action);
	}
	
	public void removeAction(Card action) {
		this.actions.remove(action);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Card getCharacter() {
		return character;
	}
	public void setCharacter(Card character) {
		this.character = character;
	}
	public List<Card> getActions() {
		return actions;
	}
	public void setActions(List<Card> actions) {
		this.actions = actions;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
}
