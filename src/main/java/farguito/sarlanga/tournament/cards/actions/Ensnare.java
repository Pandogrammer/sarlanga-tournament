package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;
import farguito.sarlanga.tournament.combat.effects.immediate.Fatigation;
import farguito.sarlanga.tournament.combat.effects.immediate.SpeedBonus;

public class Ensnare extends Action {
	
	private int fatigue = 120; 
	
	private int fatigation = 35;
	private int speedBonus = -1;
	private float damageModifier = 0.45f; 
	
	public Ensnare() {
		this.setName("Ensnare");
		this.setTarget(Target.OBJECTIVE);
		this.setFatigue(fatigue);
		this.setMelee(false);
	}
	
	public List<Effect> execute(){
		int damage = (int) ((this.getActor().getAttack() + this.getActor().getAttackBonus()) * damageModifier);
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(damage, o));
			ef.add(new SpeedBonus(speedBonus, o));
			ef.add(new Fatigation(fatigation, o));
		});
		
		return ef;
	}

}