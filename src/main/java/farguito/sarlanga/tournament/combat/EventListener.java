package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.List;

public class EventListener {
	
	private List<String> messages = new ArrayList<>();
	
	public void log(String message) {
		messages.add(message);
	}	
	
	public List<String> getMessages(){
		return messages;
	}
	
	public void deleteMessages() {
		this.messages.clear();
	}

}
