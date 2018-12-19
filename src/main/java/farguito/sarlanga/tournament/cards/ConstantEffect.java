package farguito.sarlanga.tournament.cards;

public class ConstantEffect extends Effect {
	
	private int duration;

	public boolean hasExpired() {
		return duration <= 0;
	}
	
	public void removeDuration() {
		duration--;
	}
	
}
