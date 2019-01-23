package farguito.sarlanga.tournament.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Card {

	private int id;
	private int essence;
	private String name;
	private Class object;
	private String type;
	private String description;
	
	public Card(int id, int essence, String name, Class object, String type, String description) {
		this.id = id;
		this.essence = essence;
		this.name = name;
		this.object = object;
		this.type = type;
		this.description = description;
	}

	public int getId() {
		return id;
	}
	public int getEssence() {
		return essence;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
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
