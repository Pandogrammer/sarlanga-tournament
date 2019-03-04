package farguito.sarlanga.tournament.cards;

public class CreatureCard extends Card {

	private int hp;
	private int speed;
	private int attack;
	
	public CreatureCard(int id, int essence, Creature creature, String description) {
		super();
		this.id = id;
		this.essence = essence;
		this.object = creature.getClass();
		this.name = creature.getName();
		this.description = description;
		
		this.hp = creature.getHp();
		this.speed = creature.getSpeed();
		this.attack = creature.getAttack();
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
