package farguito.sarlanga.tournament.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;
import farguito.sarlanga.tournament.controller.TeamDTO;

@Component
public class CombatHandlerTest extends TextWebSocketHandler {

	private ObjectMapper mapper = new ObjectMapper();
	
	private CombatSystem system;
	private CardFactory cards = new CardFactory();
	
	private Map<String, Integer> session_team = new HashMap<>();
	private Map<String, WebSocketSession> sessions = new HashMap<>();
	
	private void init() {
		List<Team> teams = new ArrayList<>();
		
		teams.add(buildTeam(1));
		teams.add(buildTeam(3));
		
		this.system = new CombatSystem(teams);		
	}
	
	private Team buildTeam(int n) {
		TeamDTO team = new TeamDTO();
		team.setTeamNumber(n);
		int j = 1;
		for(int i = n-1; i <= n; i++) {
			team.addCharacter(j, 1, cards.getCriatures().get(i));
			team.getCharacter(j).addAction(cards.getActions().get(i));
			j++;
		}
		return team.create();		
	}
	

	private DefoldResponse action(DefoldRequest request) {
		DefoldResponse response = new DefoldResponse("action_response");
		
		Integer actionId = (int) request.get("action");
		Integer teamId = (int) request.get("team");
		List<Integer> objectiveIds = (List) request.get("objectives");
		
		Action action = this.system.getActiveCharacter().getActions().get(actionId);
		List<Character> objectives = new ArrayList<>();
		
		for(int i = 0; i < objectiveIds.size(); i++) {
			objectives.add(this.system.getTeams().get(teamId).getCharacters().get(objectiveIds.get(i)));
		}
			
		
		this.system.prepareAction(action, objectives);
		if(this.system.validateObjectives(action)) {
			this.system.executeAction(action);
			this.system.nextTurn();
			response.put("success", true);
			sendResults();
			sendStatus();
		} else {
			response.put("success", false);
			response.put("reason", "Invalid target.");
		}	
		
		System.out.println(stringify(response));
		return response;
			
	}
	
	private void reconnect(DefoldRequest request, String newSessionId) {		
		String oldSessionId = (String) request.get("session_id");
		if(session_team.containsKey(oldSessionId)) {
			Integer team = session_team.get(oldSessionId);
			session_team.put(newSessionId, team);
		}
	}

	private void sendStatus() {		
		DefoldResponse response = new DefoldResponse("status_response");
		
		sessions.values().stream().forEach(s -> {
			sendMessage(s, stringify(response));
		});	
	}
	
	private void sendResults() {		
		this.system.getLogger().getResults().stream().forEach(r -> {
			DefoldResponse response = new DefoldResponse("result_response");
			response.put("result", r);
			sessions.values().stream().forEach(s -> {
				sendMessage(s, stringify(response));
			});
		});
		
		this.system.getLogger().deleteResults();
	}
	
	private String stringify(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}		
	}
	
	//todo entra por aca
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		try { 
			System.out.println(message.getPayload());
			DefoldRequest request = mapper.readValue(message.getPayload(), DefoldRequest.class);

			if(request.getMethod().equals("action_request")) {
				sendMessage(session, stringify(action(request)));
			} else if(request.getMethod().equals("reconnect_request")){
				reconnect(request, session.getId());				
				sendMessage(session, stringify(session(session)));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}
	}
	
	public void sendMessage(WebSocketSession session, String message) {
		try {
			session.sendMessage(new TextMessage(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	private DefoldResponse session(WebSocketSession session){
		DefoldResponse response = new DefoldResponse("session_response");
		response.put("session_id", session.getId());	
		
		return response;		
	}
		
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		sessions.put(sessionId, session);
		System.out.println("IN: "+sessionId);
		
		//test
		if(session_team.isEmpty()) {
			init();
			session_team.put(sessionId, 0);
		} else if (session_team.size() == 1) {
			session_team.put(sessionId, 1);			
		}
				
		sendMessage(session, stringify(session(session)));
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		sessions.remove(sessionId);
		System.out.println("OUT: "+sessionId);
	}
	
	
	
}















