package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;

public class EventListener {

	private List<ImmediateEffectModifier> immediateEffectModifiers = new ArrayList<>();
	
	public void modifyImmediateEffect(ImmediateEffect effect) {
		immediateEffectModifiers.stream().forEach(iem -> iem.modify(effect));
	}
	
	public void addImmediateEffectModifier(ImmediateEffectModifier iem) {
		this.immediateEffectModifiers.add(iem);
	}

}
