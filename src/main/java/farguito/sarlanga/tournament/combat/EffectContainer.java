package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Effect;

public class EffectContainer {

	private List<Effect> effects = new ArrayList<>();
	
	
	public void addEffects(List<Effect> effects) {
		this.effects.addAll(effects);
	}
	
	public List<Effect> getEffects(){
		return effects;
	}
	
	public void purgeEffects() {
		this.effects.clear();
	}
}
