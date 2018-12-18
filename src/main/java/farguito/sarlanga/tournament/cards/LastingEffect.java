package farguito.sarlanga.tournament.cards;

//efectos duraderos
public abstract class LastingEffect extends Effect{
	
	private int duration;
	private int cooldown;
	private int counter;	
	
	
	
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	
}
