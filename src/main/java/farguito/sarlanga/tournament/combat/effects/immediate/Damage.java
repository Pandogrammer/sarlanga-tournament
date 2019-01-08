package farguito.sarlanga.tournament.combat.effects.immediate;

import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;

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
