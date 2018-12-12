package farguito.sarlanga.tournament.cards;

import farguito.sarlanga.tournament.combat.Character;

public abstract class Effect {

	private Character objective;
	
	public abstract void execute();
	
	public Character getObjective() {
		return objective;
	}

	public void setObjective(Character objective) {
		this.objective = objective;
	}
	
	
}
