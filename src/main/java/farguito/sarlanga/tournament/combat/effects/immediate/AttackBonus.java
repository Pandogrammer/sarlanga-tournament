package farguito.sarlanga.tournament.combat.effects.immediate;

import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;

public class AttackBonus extends ImmediateEffect {
	
	public AttackBonus(int value, Character objective) {
		this.setObjective(objective);
		this.value = value;
	}
	
	public void execute() {
		this.getObjective().modifyAttackBonus(value);
	}
	
	public String message() {		
		return this.getObjective().getName() + "'s attack bonus changed by "+this.value;
	}
	
}
