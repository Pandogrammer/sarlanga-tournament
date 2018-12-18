package farguito.sarlanga.tournament.cards.effects.immediate;

import farguito.sarlanga.tournament.cards.ImmediateEffect;
import farguito.sarlanga.tournament.combat.Character;

public class Damage extends ImmediateEffect {
	
	private int value;
	
	public Damage(int value, Character objective) {
		this.setObjective(objective);
		this.value = value;
	}
	
	public void execute() {
		this.getObjective().damage(value);
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}	
	
	public String message() {		
		return this.getObjective().getName() + " received "+this.value+" damage.";
	}

}
