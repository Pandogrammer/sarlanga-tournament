package farguito.sarlanga.tournament.cards;

import java.util.ArrayList;
import java.util.List;

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
