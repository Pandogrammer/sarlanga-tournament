package farguito.sarlanga.tournament.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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
	
	private int i = 0;
	private String session_1;
	

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
			session_1 = sessionId;
			i++;		
		} 
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId);		
	}
}
