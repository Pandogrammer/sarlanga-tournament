package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;

public class Punch extends Action {
	
	private int fatigue = 12; 
	
	private float damageModifier = 1; 
	
	public List<Effect> execute(){
		int damage = (int) (this.getActor().getAttack() * damageModifier);
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(damage, o));
		});
		
		this.getActor().fatigate(fatigue);
		
		return ef;
	}

	public String message() {
		return this.getActor().getName()+" used PUNCH";
	}
}
