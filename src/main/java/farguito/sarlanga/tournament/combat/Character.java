package farguito.sarlanga.tournament.combat;

import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Criature;

public class Character {

	private int team;
	private int line;
	private int position;
	private String name;
	private int hp;
	private int speed;
	private int speedBonus;
	private int fatigue;
	private int attack;
	private int attackBonus;
	
	private List<Action> actions;
	
	public Character (Criature criature) {
		this.name = criature.getName();
		this.hp = criature.getHp();
		this.speed = criature.getSpeed();
		this.attack = criature.getAttack();
		this.fatigue = 0;
		this.attackBonus = 0;
		this.speedBonus = 0;
	}
	
	public Character (int team, Criature criature, List<Action> actions) {
		this(criature);
		this.team = team;
		this.actions = actions;		
	}
	
	public boolean isAlive() {
		return this.hp > 0;
	}
	
	public boolean isReady() {
		return this.fatigue == 0;
	}
	
	public void rest() {
		int totalSpeed = this.speed - this.speedBonus;
		if(totalSpeed < 1) totalSpeed = 1;
		
		this.fatigue = this.fatigue - totalSpeed;
		if (this.fatigue < 0) {
			this.fatigue = 0;
		}
	}
	
	public void fatigate(int value) {
		this.fatigue = this.fatigue + value;
	}
	
	public void damage(int value) {
		if(this.hp > 0 ) {
			this.hp = this.hp - value;
			if(this.hp < 0) this.hp = 0;
		}
	}
	
	public void modifyAttackBonus(int value) {
		this.attackBonus += value;
	}
	
	public void modifySpeedBonus(int value) {
		this.speedBonus += value;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getFatigue() {
		return fatigue;
	}
	public void setFatigue(int fatigue) {
		this.fatigue = fatigue;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getAttackBonus() {
		return attackBonus;
	}
	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}
	public int getSpeedBonus() {
		return speedBonus;
	}
	public void setSpeedBonus(int speedBonus) {
		this.speedBonus = speedBonus;
	}
	
	
	
}
