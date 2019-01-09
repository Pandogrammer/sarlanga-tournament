package farguito.sarlanga.tournament.controller;

public class Card {

	private int id;
	private int essence;
	private String name;
	private Class object;
	private String type;
	
	public Card(int id, int essence, String name, Class object, String type) {
		this.essence = essence;
		this.name = name;
		this.object = object;
		this.type = type;
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
	public Class getObject() {
		return object;
	}	
	
}
