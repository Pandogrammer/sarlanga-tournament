package farguito.sarlanga.tournament.cards.effects.immediate;

import farguito.sarlanga.tournament.cards.ImmediateEffect;
import farguito.sarlanga.tournament.combat.Character;

public class Damage extends ImmediateEffect {
	
	
	public Damage(int value, Character objective) {
		this.setObjective(objective);
		this.value = value;
	}
	
	public void execute() {
		this.getObjective().damage(value);
	}
	
	public String message() {		
		return this.getObjective().getName() + " received "+this.value+" damage.";
	}

}
