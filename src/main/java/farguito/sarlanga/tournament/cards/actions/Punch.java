package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Effect;
import farguito.sarlanga.tournament.cards.effects.immediate.Damage;

public class Punch extends Action {
	
	private int fatigue = 100; 
	
	public List<Effect> execute(){
		int damage = this.getActor().getAttack();
		
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(damage, o));
		});
		
		this.getActor().fatigate(fatigue);
		
		System.out.println(this.getActor().getName()+" used PUNCH");
		return ef;
	}
}
