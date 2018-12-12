package farguito.sarlanga.tournament.cards;

import farguito.sarlanga.tournament.combat.Character;

public class Damage extends Effect {
	
	private int value;
	
	public Damage(int value, Character objective) {
		this.setObjective(objective);
		this.value = value;
	}
	
	public void execute() {
		this.getObjective().damage(value);
		
		System.out.println(this.getObjective().getName() + " received "+this.value+" damage.");
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}	

}
