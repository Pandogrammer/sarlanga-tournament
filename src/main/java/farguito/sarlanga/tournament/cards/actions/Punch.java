package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;

public class Punch extends Action {
	
	private int fatigue = 60; 
	
	private float damageModifier = 1; 
	
	public Punch() {
		this.setName("Punch");
		this.setTarget(Target.OBJECTIVE);
		this.setFatigue(fatigue);
		this.setMelee(true);
	}
	
	public List<Effect> execute(){
		int damage = (int) ((this.getActor().getAttack() + this.getActor().getAttackBonus()) * damageModifier);
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(damage, o));
		});
		
		return ef;
	}

}
