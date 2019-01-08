package farguito.sarlanga.tournament.combat.effects;

public abstract class ImmediateEffect extends Effect {

	protected int value;
	
	public abstract void execute();
	
	public abstract String message();

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}	
	
}
