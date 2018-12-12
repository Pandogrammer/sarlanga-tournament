package farguito.sarlanga.tournament.cards.actions;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Effect;
import farguito.sarlanga.tournament.cards.effects.Damage;

public class Punch extends Action {
	
	public List<Effect> execute(){
		List<Effect> ef = new ArrayList<>();
		this.getObjectives().stream().forEach(o -> {
			ef.add(new Damage(this.getActor().getAttack(), o));
		});
		
		
		System.out.println(this.getActor().getName()+" used PUNCH");
		return ef;
	}
}
