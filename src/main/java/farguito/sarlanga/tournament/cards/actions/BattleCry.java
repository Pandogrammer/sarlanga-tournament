package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.ConstantEffect;
import farguito.sarlanga.tournament.cards.Effect;
import farguito.sarlanga.tournament.cards.LastingEffect;
import farguito.sarlanga.tournament.cards.effects.immediate.Damage;

public class BattleCry extends Action {

	private int fatigue = 24;
	private int duration = 10;
	private double damageBonus = 1.10;
	
	@Override
	public List<Effect> execute() {
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new ConstantEffect(duration, cooldown, new Damage(lastingDamage, o)));
			ef.add(new Damage(damage, o));
		});
		
		this.getActor().fatigate(fatigue);
		
		return ef;
	}

	@Override
	public String message() {
		return this.getActor().getName()+" used BATTLE CRY";
	}

}
