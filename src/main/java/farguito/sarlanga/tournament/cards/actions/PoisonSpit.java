package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Effect;
import farguito.sarlanga.tournament.cards.LastingEffect;
import farguito.sarlanga.tournament.cards.effects.immediate.Damage;

public class PoisonSpit extends Action {
	
	private int fatigue = 160;
	private int duration = 3;
	private int cooldown = 4;
		
	public List<Effect> execute() {
		int lastingDamage = this.getActor().getAttack() / 4;
		int damage = this.getActor().getAttack() / 2;
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new LastingEffect(duration, cooldown, new Damage(lastingDamage, o)));
			ef.add(new Damage(damage, o));
		});
		
		this.getActor().fatigate(fatigue);
		
		return ef;
	}
	
	public String message() {
		return this.getActor().getName()+" used POISON SPIT";
	}
	
}