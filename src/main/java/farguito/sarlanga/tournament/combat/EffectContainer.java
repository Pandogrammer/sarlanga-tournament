package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.ConstantEffect;
import farguito.sarlanga.tournament.cards.Effect;
import farguito.sarlanga.tournament.cards.ImmediateEffect;
import farguito.sarlanga.tournament.cards.LastingEffect;

public class EffectContainer {

	private List<ImmediateEffect> immediateEffects = new ArrayList<>();
	private List<LastingEffect> lastingEffects = new ArrayList<>();
	private List<ConstantEffect> constantEffects = new ArrayList<>();
	
	public void addEffects(List<Effect> effects) {
		effects.stream().forEach(e -> {
			if (e instanceof LastingEffect) lastingEffects.add((LastingEffect) e);
			else if (e instanceof ConstantEffect) constantEffects.add((ConstantEffect) e);
			else immediateEffects.add((ImmediateEffect) e);
		});
	}

	public void purgeEffects() {
		this.immediateEffects.clear();
				
		this.lastingEffects.removeIf(e -> e.hasExpired());
	}
	
	public List<ImmediateEffect> getImmediateEffects() {
		return immediateEffects;
	}
	
	public List<LastingEffect> getLastingEffects() {
		return lastingEffects;
	}
	
	public List<ConstantEffect> getConstantEffects() {
		return constantEffects;
	}

}
