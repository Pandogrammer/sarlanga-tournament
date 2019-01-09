package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.effects.ConstantEffect;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.ReactiveEffect;

@Deprecated
public class BattleCry extends Action {

	private int fatigue = 24;
	private int duration = 10;
	private double damageBonus = 1.10;
	
	@Override
	public List<Effect> execute() {
		
		List<Effect> ef = new ArrayList<>();
		
		ReactiveEffect actionDamageBuff = new ReactiveEffect(duration);
		Predicate<Action> sameTeam = (a) -> a.getActor().getTeam() == this.getActor().getTeam();
		actionDamageBuff.addCondition(sameTeam);
		
		ef.add(actionDamageBuff);
		
		this.getActor().fatigate(fatigue);
		
		return ef;
	}

	@Override
	public String message() {
		return this.getActor().getName()+" used BATTLE CRY";
	}

}
