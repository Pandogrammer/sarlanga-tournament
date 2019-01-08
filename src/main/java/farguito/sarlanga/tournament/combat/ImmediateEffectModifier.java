package farguito.sarlanga.tournament.combat;

import java.util.function.Predicate;

import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;

/**
 * los modificadores tienen:
 * reciben lo que van a modificar
 * un predicado que si se cumple lo modifica
 * la modificacion que aplican
 */
@Deprecated
public class ImmediateEffectModifier extends Modifier {	//crear el EffectModifier generico
	
	private Predicate<ImmediateEffect> predicate;
	private float modifier;
	
	public ImmediateEffectModifier(Predicate<ImmediateEffect> predicate, float modifier) {
		this.predicate = predicate;
		this.modifier = modifier;
	}
	
	public void modify(ImmediateEffect effect) {
		if(predicate.test(effect)) {
			apply(effect);
		};
	}
	
	private void apply(ImmediateEffect effect) {
		effect.setValue(Math.round(effect.getValue() * modifier));
	}
	
}
