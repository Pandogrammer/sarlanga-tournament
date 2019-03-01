package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;
import farguito.sarlanga.tournament.combat.effects.LastingEffect;

public class Logger {
	
	//parte logger, podria ir en otra clase incluso
	private List<String> messages = new ArrayList<>();
	
	private List<Map<String, Object>> results = new ArrayList<>(); 
	
	public void log(String message) {
		//System.out.println(message);
		messages.add(message);
	}	

	public void addResult(ImmediateEffect ef) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", "effect");
		map.put("class", ef.getClass().getSimpleName().toLowerCase());
		map.put("value", ef.getValue());
		
		Map<String,Object> objective = new HashMap<>();
		objective.put("team", ef.getObjective().getTeam());
		objective.put("id", ef.getObjective().getId());
		
		map.put("objective", objective);
		
		this.results.add(map);
	}
	
	public void addResult(Action ac) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", "action");
		map.put("melee", ac.isMelee());
		map.put("target", ac.getTarget().name());
		map.put("team", ac.getActor().getTeam());
		map.put("source", ac.getActor().getId());
		
		List<Map<String, Object>> objectives = new ArrayList<>();
		for(Character obj : ac.getObjectives()) {
			Map<String,Object> objective = new HashMap<>();
			objective.put("team", obj.getTeam());
			objective.put("id", obj.getId());
			
			objectives.add(objective);
		}

		map.put("objectives", objectives);
		
		this.results.add(map);
	}
	
	public List<Map<String,Object>> getResults(){
		return results;		
	}
	
	public void deleteResults() {
		this.results.clear();
	}
	
	public List<String> getMessages(){
		return messages;
	}
	
	public void deleteMessages() {
		this.messages.clear();
	}
}
