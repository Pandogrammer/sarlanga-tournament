package farguito.sarlanga.tournament.combat.effects.immediate;

import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;

public class SpeedBonus extends ImmediateEffect {
	
	public SpeedBonus(int value, Character objective) {
		this.setObjective(objective);
		this.value = value;
	}
	
	public void execute() {
		this.getObjective().modifySpeedBonus(value);
	}
	
	public String message() {		
		return this.getObjective().getName() + "'s speed bonus changed by "+this.value;
	}
	
}
