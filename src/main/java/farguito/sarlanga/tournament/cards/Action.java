package farguito.sarlanga.tournament.cards;

import java.util.List;

import farguito.sarlanga.tournament.combat.Character;

public abstract class Action {
	
	private Character actor;
	private List<Character> objectives;

	//public abstract boolean validate();
	
	public abstract List<Effect> execute();
	
	public abstract String message();
	
	public Character getActor() {
		return actor;
	}
	public void setActor(Character actor) {
		this.actor = actor;
	}
	public List<Character> getObjectives() {
		return objectives;
	}
	public void setObjectives(List<Character> objectives) {
		this.objectives = objectives;
	}
	

}
