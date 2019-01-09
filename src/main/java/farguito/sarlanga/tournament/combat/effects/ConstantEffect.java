package farguito.sarlanga.tournament.combat.effects;

import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ConstantEffect extends Effect {
	
	protected int duration;
	
	public ConstantEffect(int duration) {
		this.duration = duration;
	}

	public boolean hasExpired() {
		return duration <= 0;
	}
	
	public void removeDuration() {
		duration--;
	}
	
	
}
