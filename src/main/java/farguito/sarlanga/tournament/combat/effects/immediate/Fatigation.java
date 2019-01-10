package farguito.sarlanga.tournament.combat.effects.immediate;

import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;

public class Fatigation extends ImmediateEffect {
	
	public Fatigation(int value, Character objective) {
		this.setObjective(objective);
		this.value = value;
	}
	
	public void execute() {
		this.getObjective().fatigate(value);
	}
	
	public String message() {		
		return this.getObjective().getName() + "'s fatigue changed by "+this.value;
	}
	
}
