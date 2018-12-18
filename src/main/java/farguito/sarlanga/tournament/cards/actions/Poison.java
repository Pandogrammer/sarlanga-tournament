package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Effect;
import farguito.sarlanga.tournament.cards.effects.Damage;

public class Poison extends Action {
	
	private int fatigue = 160; 
	
	public List<Effect> execute(){
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(this.getActor().getAttack(), o));
		});
		
		this.getActor().fatigate(fatigue);
		
		System.out.println(this.getActor().getName()+" used PUNCH");
		return ef;
	}
}