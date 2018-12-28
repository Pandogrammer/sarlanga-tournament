package farguito.sarlanga.tournament.cards;

import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ConstantEffect extends Effect {
	
	private int duration;
	private List<Predicate> conditions = new ArrayList<>();
	
	private List<Effect> effects = new ArrayList<>();	
	private List<Modifier> modifiers = new ArrayList<>();
	
	public ConstantEffect(int duration) {
		this.duration = duration;
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

	public boolean hasModifiers() {
		return !modifiers.isEmpty();
	}
	
	public boolean generatesEffects() {
		return !effects.isEmpty();
	}
	
}
