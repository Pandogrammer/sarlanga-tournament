package farguito.sarlanga.tournament.cards;

public class CriatureCard extends Card {

	private int hp;
	private int speed;
	private int attack;
	
	public CriatureCard(int id, int essence, Criature criature, String description) {
		super();
		this.id = id;
		this.essence = essence;
		this.object = criature.getClass();
		this.name = criature.getName();
		this.description = description;
		
		this.hp = criature.getHp();
		this.speed = criature.getSpeed();
		this.attack = criature.getAttack();
	}

	public int getHp() {
		return hp;
	}

	public int getSpeed() {
		return speed;
	}

	public int getAttack() {
		return attack;
	}
	
	

	
}
