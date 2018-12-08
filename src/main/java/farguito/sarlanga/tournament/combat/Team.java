package farguito.sarlanga.tournament.combat;

import java.util.Comparator;
import java.util.List;

public class Team {
	
	List<Character> characters;
	
	public Team(List<Character> characters) {
		this.characters = characters;
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

	
	
	
	
	public List<Character> getCharacters() {
		return characters;
	}
	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}	
	
}
