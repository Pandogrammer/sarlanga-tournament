package farguito.sarlanga.tournament.cards;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.Effect;

public abstract class Action {
	
	private int fatigue;
	private Target target;
	private String name;
	private String description;
	private boolean melee;
	
	private Character actor;
	private List<Character> objectives;
	
	public abstract List<Effect> execute();

	public String message() {
		return this.getActor().getName()+" used "+this.name+".";
	}
	
	@JsonIgnore
	public Character getActor() {
		return actor;
	}
	public void setActor(Character actor) {
		this.actor = actor;
	}

	@JsonIgnore
	public List<Character> getObjectives() {
		return objectives;
	}
	public void setObjectives(List<Character> objectives) {
		this.objectives = objectives;
	}
	public int getFatigue() {
		return fatigue;
	}
	public void setFatigue(int fatigue) {
		this.fatigue = fatigue;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isMelee() {
		return melee;
	}
	public void setMelee(boolean melee) {
		this.melee = melee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
