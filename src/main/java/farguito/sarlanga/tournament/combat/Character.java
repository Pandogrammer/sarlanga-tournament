package farguito.sarlanga.tournament.combat;

import java.util.List;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Criature;

public class Character {

	private int team;
	private String name;
	private int hp;
	private int speed;
	private int fatigue;
	private int attack;
	
	private List<Action> actions;
	
	public Character (int team, Criature criature, List<Action> actions) {
		this.team = team;
		this.name = criature.getName();
		this.hp = criature.getHp();
		this.speed = criature.getSpeed();
		this.attack = criature.getAttack();
		this.fatigue = 0;
		this.actions = actions;
	}
	
	public boolean isAlive() {
		return this.hp > 0;
	}
	
	public boolean isReady() {
		return this.fatigue == 0;
	}
	
	public void rest() {
		this.fatigue = this.fatigue - this.speed;
		if (this.fatigue < 0) {
			this.fatigue = 0;
		}
	}
	
	public void fatigate(int value) {
		this.fatigue = this.fatigue + value;
	}
	
	public void damage(int value) {
		this.hp = this.hp - value;
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
	
	
}
