package farguito.sarlanga.tournament.cards;

import java.util.ArrayList;
import java.util.List;

//efectos duraderos
//los efectos duraderos se activan cada X turnos, y generan un efecto
public class LastingEffect extends Effect {	
	private int duration;
	private int cooldown;
	private int counter = 0;
	
	private List<Effect> effects = new ArrayList<>();

	public LastingEffect(int duration, int cooldown, List<Effect> effects) {
		this.duration = duration;
		this.cooldown = cooldown;
		this.effects.addAll(effects);	
	}
	
	public LastingEffect(int duration, int cooldown, Effect effect) {
		this.duration = duration;
		this.cooldown = cooldown;
		this.effects.add(effect);
	}

	public List<Effect> execute() {
		counter = 0;
		duration--;
		return effects;
	}	
	
	public boolean hasExpired() {
		return duration <= 0;
	}
	
	public boolean isReady() {
		return counter == cooldown;
	}
	
	public void addCooldown() {
		counter++;
	}
	
	
}
