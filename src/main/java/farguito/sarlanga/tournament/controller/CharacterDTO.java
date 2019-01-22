package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Card;

public class CharacterDTO {

	private int id;
	private Card character;
	private List<Card> actions = new ArrayList<>();
	
	
	public CharacterDTO() {}
	
	
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
}
