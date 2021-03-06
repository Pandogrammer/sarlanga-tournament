package farguito.sarlanga.tournament.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.Team;

public class TeamDTO {
	
	private String owner;
	private boolean ready = false;
	private int teamNumber;
	private Map<Integer, CharacterDTO> characters = new LinkedHashMap<>();
	
	public boolean addCharacter(int line, int position, Card characterCard) {
		if(validatePosition(line, position)) {
			CharacterDTO dto = new CharacterDTO();
			dto.setCharacter(characterCard);
			int id = characters.size()+1;
			dto.setLine(line);
			dto.setPosition(position);
			dto.setId(id);
			this.characters.put(id, dto);
			
			return true;			
		}		
		return false;
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
		Team team = new Team(teamNumber, characters);
		team.setOwner(this.owner);
		
		return team;
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


	public boolean validateActions() {
		boolean hasCharactersWithNoActions = characters.values().stream().anyMatch( c -> c.getActions().size() == 0 );
				
		return !hasCharactersWithNoActions;
	}
	public boolean validatePositions() {
		List<String> occupiedPositions = new ArrayList<>();
		
		characters.values().stream().forEach(c -> {
			String linePosition = c.getLine()+"|"+c.getPosition();
				occupiedPositions.add(linePosition);
			
		});
		
		if(occupiedPositions.size() != occupiedPositions.stream().distinct().count())
			return false;
		else 
			return true;
	}
	
	
	public void validate(Integer essence) throws TeamValidationException {
		if(this.getCharacters().isEmpty()) throw new TeamValidationException("Team empty.");
		if(!this.validateActions())  throw new TeamValidationException("All characters must have actions.");
		if(!this.validatePositions())  throw new TeamValidationException("Two or more characters occupy the same position");
		if(this.getEssence() > essence)   throw new TeamValidationException("Team excedes maximum essence.");
	}
	

	public int getTeamNumber() {
		return teamNumber;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
	
	
}
