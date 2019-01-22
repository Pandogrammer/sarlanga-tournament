package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.List;

import farguito.sarlanga.tournament.cards.Card;

public class TeamDTO {
	
	private List<CharacterDTO> characters = new ArrayList<>();
	
	public void addCharacter(Card characterCard) {
		CharacterDTO dto = new CharacterDTO();
		dto.setCharacter(characterCard);
		this.characters.add(dto);
	}

	public List<CharacterDTO> getCharacters() {
		return characters;
	}

	public int getEssence() {
		int essence = 0;
		
		for(CharacterDTO c : characters) {
			essence += c.getCharacter().getEssence();
			
			for(Card a : c.getActions()) {
				essence += a.getEssence();
			}
		}
		
		return essence;
	}
	
	
}
