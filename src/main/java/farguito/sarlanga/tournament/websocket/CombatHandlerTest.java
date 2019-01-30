package farguito.sarlanga.tournament.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;
import farguito.sarlanga.tournament.controller.TeamDTO;

@Component
public class CombatHandlerTest extends TextWebSocketHandler {

	private ObjectMapper mapper = new ObjectMapper();
	
	private CombatSystem system;
	private CardFactory cards = new CardFactory();
	private List<Team> teams = new ArrayList<>();
	
	private int i = 0;
	private String session_1;
	
	private void init() {
		
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
	
	
	private void action(int team, int id) {		
		if(this.system.getWinningTeam() == -1) {
			Action action = this.system.getActiveCharacter().getActions().get(0);
			this.system.prepareAction(action, this.system.getTeams().get(team).getCharacters().get(id));
			if(this.system.validateObjectives(action)) {
				this.system.executeAction(action);
				this.system.nextTurn();
			} else {
				System.out.println("Target incorrecto.");
			}					
		} 
	}	
	
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		try { 
			//System.out.println(message.getPayload());
			int teamTurn = session.getId().equals(session_1) ? 1 : 3;
			int targetTeam = teamTurn == 1 ? 1 : 0;
			if(this.system.getActiveCharacter().getTeam() == teamTurn)				
				action(targetTeam, Integer.valueOf(message.getPayload())-1);
			else
				System.out.println("no es tu turno");
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
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId);
		if(i == 0) { 
			init();
			this.teams.get(0).setOwner(sessionId);
			session_1 = sessionId;
			i++;		
		}  else {
			this.teams.get(1).setOwner(sessionId);			
		}
		DefoldResponse response = new DefoldResponse("session");
		response.put("session_id", sessionId);
		
		sendMessage(session, response.encode());
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId);	
	}
	
	
	private class DefoldResponse {
		
		private String message_id;
		private Map<String, Object> message;
		
		public DefoldResponse(String message_id) {
			this.message_id = message_id;
		}
		
		public String encode() throws JsonProcessingException {
			return mapper.writeValueAsString(this);
		}
		
		public void put(String key, Object value) {
			if(message == null) message = new LinkedHashMap<>();
			
			message.put(key, value);
		}

		public String getMessage_id() {
			return message_id;
		}

		@JsonInclude(Include.NON_NULL)
		public Map<String, Object> getMessage() {
			return message;
		}
		
		
		
		
	}
	
	
	
}















