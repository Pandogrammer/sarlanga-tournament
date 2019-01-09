package farguito.sarlanga.tournament.combat.effects;

import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Deprecated
public class ReactiveEffect extends ConstantEffect {
	
	private List<Predicate> conditions = new ArrayList<>();	
	private List<Effect> effects = new ArrayList<>();	
	private List<Modifier> modifiers = new ArrayList<>();
	
	public ReactiveEffect(int duration) {
		super(duration);
	}

	public boolean hasExpired() {
		return duration <= 0;
	}
	
	public void removeDuration() {
		duration--;
	}
	
	public void addCondition(Predicate condition) {
		conditions.add(condition);
	}
	
	public void addEffect(Effect effect) {
		effects.add(effect);
	}
	
	public void addModifier(Modifier modifier) {
		modifiers.add(modifier);
	}

	public boolean hasModifiers() {
		return !modifiers.isEmpty();
	}
	
	public boolean generatesEffects() {
		return !effects.isEmpty();
	}
	
}
