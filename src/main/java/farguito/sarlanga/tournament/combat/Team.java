package farguito.sarlanga.tournament.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Team {
	
	private String owner;
	private int number;
	private List<Character> characters;
	
	public Team(List<Character> characters) {
		this.characters = characters;
	}
	
	public Team(int number, List<Character> characters) {
		this.number = number;
		this.characters = characters;
	}	
	
	public List<Character> lineCharacters(int line){
		return characters.stream().filter(c -> c.getLine() == line).collect(Collectors.toList());
	}

	public boolean someoneAlive() {
		if (characters.stream().anyMatch(ch -> ch.isAlive())) {
			return true;
		} else {
			return false;
		}		
	}
	
	public Character fastestCharacter() {
		return characters.stream().max(Comparator.comparing(Character::getSpeed)).get();		
	}

	
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return number;
	}
	public List<Character> getCharacters() {
		return characters;
	}
	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}	
	
	
}
