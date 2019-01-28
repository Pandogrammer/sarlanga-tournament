package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.Team;

public class TeamDTO {
	
	private int teamNumber;
	private Map<Integer, CharacterDTO> characters = new LinkedHashMap<>();
	
	public void addCharacter(int line, int position, Card characterCard) {
		CharacterDTO dto = new CharacterDTO();
		dto.setCharacter(characterCard);
		int id = characters.size()+1;
		dto.setLine(line);
		dto.setPosition(position);
		dto.setId(id);
		this.characters.put(id, dto);
	}
	
	public boolean validatePosition(int line, int position) {
		return !characters.values().stream().anyMatch(c -> c.getLine() == line && c.getPosition() == position);
	}

	public Team create() {
		List<Character> characters = new ArrayList<>();
		
		for(CharacterDTO dto : this.characters.values()) {
			Character ch = dto.create();
			ch.setTeam(teamNumber);
			characters.add(ch);						
		}
		
		return new Team(teamNumber, characters);		
	}
	
	public void removeCharacter(int id) {
		this.characters.remove(id);
	}
	
	public CharacterDTO getCharacter(int id) {
		return this.characters.get(id);
	}
	

	public Map<Integer, CharacterDTO> getCharacters() {
		return characters;
	}

	public int getEssence() {
		int essence = 0;
		
		for(CharacterDTO c : characters.values()) {
			essence += c.getCharacter().getEssence();
			
			for(Card a : c.getActions()) {
				essence += a.getEssence();
			}
		}
		
		return essence;
	}


	public boolean validateCharacters() {
		return !characters.values().stream().anyMatch( c -> c.getActions().size() == 0 );
	}

	public int getTeamNumber() {
		return teamNumber;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	
	
}
