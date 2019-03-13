package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.LastingEffect;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;

public class PoisonSpit extends Action {
	
	private int fatigue = 80;
	private int duration = 2;
	private int cooldown = 3;

	private float immediateDamageModifier = 0.5f;
	private float lastingDamageModifier = 0.35f;
	
	public PoisonSpit() {
		this.setName("Poison Spit");
		this.setTarget(Target.OBJECTIVE);
		this.setFatigue(fatigue);
		this.setMelee(false);
	}
		
	public List<Effect> execute() {
		int lastingDamage = (int) ((this.getActor().getAttack() + this.getActor().getAttackBonus()) * lastingDamageModifier);
		int damage = (int) ((this.getActor().getAttack() + this.getActor().getAttackBonus()) * immediateDamageModifier);
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new LastingEffect(duration, cooldown, new Damage(lastingDamage, o)));
			ef.add(new Damage(damage, o));
		});
		
		
		return ef;
	}
	
	
}