package farguito.sarlanga.tournament.combat.effects;

import farguito.sarlanga.tournament.combat.Character;

public abstract class Effect {
	
	private Character objective;
	
	public Character getObjective() {
		return objective;
	}

	public void setObjective(Character objective) {
		this.objective = objective;
	}
	
	
}
