package farguito.sarlanga.tournament.combat;

import java.util.List;

import farguito.sarlanga.tournament.cards.Action;

public class Character {

	private int team;
	private int hp;
	private int speed;
	private int fatigue;
	private List<Action> actions;
	
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
	
	public void fatigate(int fatigue) {
		this.fatigue = this.fatigue + fatigue;
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

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	
}
