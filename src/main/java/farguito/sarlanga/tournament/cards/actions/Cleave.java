package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;

public class Cleave extends Action {
	
	private int fatigue = 100; 
	
	private float damageModifier = 0.75f; 
	
	public Cleave() {
		this.setTarget(Target.LINE);
		this.setFatigue(fatigue);
		this.setMelee(true);

		this.setName("Cleave");
		this.setDescription("Inflicts damage in a line.");
	}
	
	public List<Effect> execute(){
		int damage = (int) (this.getActor().getAttack() * damageModifier);
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(damage, o));
		});
		
		return ef;
	}

}