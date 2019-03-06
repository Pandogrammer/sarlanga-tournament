package farguito.sarlanga.tournament.cards;

import farguito.sarlanga.tournament.combat.Target;

public class ActionCard extends Card {

	private int fatigue;
	private Target target;
	private boolean melee;
	
	public ActionCard(int id, int essence, Action action) {
		super();
		this.id = id;
		this.essence = essence;
		this.object = action.getClass();
		this.name = action.getName();
		this.description = action.getDescription();
		
		this.fatigue = action.getFatigue();
		this.target = action.getTarget();
		this.melee = action.isMelee();		
	}

	public int getFatigue() {
		return fatigue;
	}

	public Target getTarget() {
		return target;
	}

	public boolean isMelee() {
		return melee;
	}
	
	
	
	
}
