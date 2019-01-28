package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.LastingEffect;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;

public class PoisonSpit extends Action {
	
	private int fatigue = 90;
	private int duration = 3;
	private int cooldown = 5;

	private float immediateDamageModifier = 0.5f;
	private float lastingDamageModifier = 0.25f;
	
	public PoisonSpit() {
		this.setTarget(Target.OBJECTIVE);
		this.setFatigue(fatigue);
		this.setMelee(false);

		this.setName("Poison Spit");
		this.setDescription("Inflicts damage and poison in the objective.");
	}
		
	public List<Effect> execute() {
		int lastingDamage = (int) (this.getActor().getAttack() * lastingDamageModifier);
		int damage = (int) (this.getActor().getAttack() * immediateDamageModifier);
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new LastingEffect(duration, cooldown, new Damage(lastingDamage, o)));
			ef.add(new Damage(damage, o));
		});
		
		
		return ef;
	}
	
	
}