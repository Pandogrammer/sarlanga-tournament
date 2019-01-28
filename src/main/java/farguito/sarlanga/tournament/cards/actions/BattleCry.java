package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Target;
import farguito.sarlanga.tournament.combat.effects.ConstantEffect;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.ReactiveEffect;
import farguito.sarlanga.tournament.combat.effects.immediate.AttackBonus;
import farguito.sarlanga.tournament.combat.effects.immediate.Damage;

public class BattleCry extends Action {

	private int fatigue = 120;	
	
	private float attackBonusModifier = 0.3f;
	
	public BattleCry() {
		this.setTarget(Target.SELF);
		this.setFatigue(fatigue);
		this.setMelee(false);
		
		this.setName("Battle Cry");		
		this.setDescription("Boosts user attack bonus.");
	}
	
	@Override
	public List<Effect> execute() {		
		List<Effect> ef = new ArrayList<>();
		
		this.getObjectives().stream().forEach(o -> {
			ef.add(new AttackBonus((int) (o.getAttack() * attackBonusModifier), o));
		});
		
		return ef;
	}


}
