package farguito.sarlanga.tournament.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Card {

	protected int id;
	protected int essence;
	protected Class<?> object;
	protected String name;
	protected String description;
	
	
	public int getId() {
		return id;
	}
	public int getEssence() {
		return essence;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	@JsonIgnore
	public Object getObject() {
		try {
			return object.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	
}
