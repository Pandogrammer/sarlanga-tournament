package farguito.sarlanga.tournament.cards.actions;

import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Effect;

public class BattleCry extends Action {

	private int fatigue = 18;
	private int duration = 10;
	private double damageBonus = 1.10;
	
	@Override
	public List<Effect> execute() {
		return null;
	}

	@Override
	public String message() {
		return this.getActor().getName()+" used BATTLE CRY";
	}

}
