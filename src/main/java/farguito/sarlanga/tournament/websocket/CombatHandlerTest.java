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

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.SarlangaContext;
import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.DefoldResponse;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.connection.TeamDTO;
import farguito.sarlanga.tournament.controller.MatchService;

@Component
public class CombatHandlerTest extends TextWebSocketHandler {	
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private CombatSystem system;
	
	private CardFactory cards;
	private MatchService matchs;	

	private Map<String, String> session_account = new HashMap<>();
	private Map<String, WebSocketSession> sessions = new HashMap<>();
	

	//todo entra por aca
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		try { 
			System.out.println(message.getPayload());
			DefoldRequest request = mapper.readValue(message.getPayload(), DefoldRequest.class);

			if(request.getMethod().equals("action_request")) {
				send(session, stringify(action(request)));

			} else if(request.getMethod().equals("reconnect_request")){
				reconnect(request, session.getId());	

			} else if(request.getMethod().equals("status_request")){
				send(session, stringify(status()));

			} else if(request.getMethod().equals("teams_request")){
				send(session, stringify(teams()));
				
			} else if(request.getMethod().equals("connection_request")){
				connect(request);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}
	}
	
	
	private void init() {
		this.cards = (CardFactory) SarlangaContext.getAppContext().getBean("cardFactory");	
		this.matchs = (MatchService) SarlangaContext.getAppContext().getBean("matchService");	
		
		matchs.create(10, cards.getCards());
		
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
			team.addCharacter(j, i%2+1, cards.getCriatures().get(i));
			team.getCharacter(j).addAction(cards.getActions().get(i));
			j++;
		}
		return team.create();		
	}
	

	private DefoldResponse action(DefoldRequest request) {
		DefoldResponse response = new DefoldResponse("action_response");
		
		Integer actionId = (int) request.get("action");
		List<Integer> objectiveIds = (List) request.get("objectives");
		
		Action action = this.system.getActiveCharacter().getActions().get(actionId);
		List<Character> objectives = new ArrayList<>();
		
		for(int i = 0; i < objectiveIds.size(); i++) {
			objectives.add(this.system.getCharacter(objectiveIds.get(i)));
		}
			
		
		this.system.prepareAction(action, objectives);
		if(this.system.validateObjectives(action)) {
			this.system.executeAction(action);
			this.system.nextTurn();
			broadcast(stringify(results()));
			this.system.getLogger().deleteResults();
			broadcast(stringify(status()));			
			response.put("success", true);
		} else {
			response.put("success", false);
			response.put("reason", "Invalid target.");
		}	
		
		return response;			
	}
	
	private DefoldResponse status() {		
		DefoldResponse response = new DefoldResponse("status_response");

		//estado del turno
		//estado de los personajes
		//estado efectos duraderos

		response.put("character_active", this.system.getActiveCharacter().getId());
		
		List<Object> charactersStatus = new ArrayList<>();
		this.system.getTeams().stream().forEach(t -> {
			List<Character> character = t.getCharacters();
			for(int i = 0; i < character.size(); i++){
				Character c = character.get(i);
				Map<String, Object> status = new LinkedHashMap<>();
				status.put("id", c.getId());
				status.put("alive", c.isAlive());
				status.put("line", c.getLine());
				status.put("position", c.getPosition());
				status.put("hp", c.getHp());
				status.put("hp_max", c.getHpMax());
				status.put("fatigue", c.getFatigue());
				status.put("attack", c.getAttack());
				status.put("attack_bonus", c.getAttackBonus());				
				status.put("speed", c.getSpeed());
				status.put("speed_bonus", c.getSpeedBonus());
				
				charactersStatus.add(status);
			}
		});
		
		response.put("characters", charactersStatus);
		
		return response;
	}
	
	
	private DefoldResponse results() {
		DefoldResponse response = new DefoldResponse("result_response");
		
		List<Object> results = new ArrayList<>();
		this.system.getLogger().getResults().stream().forEach(r -> {
			results.add(r);
		});
		
		response.put("results", results);
		
		return response;
	}


	private DefoldResponse session(WebSocketSession session){
		DefoldResponse response = new DefoldResponse("session_response");
		
		response.put("session_id", session.getId());
		
		return response;		
	}

	
	private void connect(DefoldRequest request) {		
		String sessionId = (String) request.get("session_id");
		String accountId = (String) request.get("account_id");
		session_account.put(sessionId, accountId);
		
	}

	
	private void reconnect(DefoldRequest request, String newSessionId) {		
		String oldSessionId = (String) request.get("session_id");
		if(session_account.containsKey(oldSessionId)) {
			String accountId = session_account.get(oldSessionId);
			session_account.put(newSessionId, accountId);
		}

		System.out.println(newSessionId+": RECONNECTED - WAS ["+oldSessionId+"]");
	}

	private DefoldResponse teams(){
		DefoldResponse response = new DefoldResponse("teams_response");
		
		List<Map<String, Object>> teams = new ArrayList<>();
		this.system.getTeams().stream().forEach(t -> {
			Map<String, Object> team = new HashMap<>();
			team.put("owner", t.getOwner());
			team.put("number", t.getNumber());
			
			List<Map<String, Object>> characters = new ArrayList<>();
			t.getCharacters().stream().forEach(ch -> {
				Map<String, Object> character = new HashMap<>();
				
				character.put("id", ch.getId());
				character.put("alive", ch.isAlive());
				character.put("line", ch.getLine());
				character.put("position", ch.getPosition());
				character.put("hp", ch.getHp());
				character.put("hp_max", ch.getHpMax());
				character.put("fatigue", ch.getFatigue());
				character.put("attack", ch.getAttack());
				character.put("attack_bonus", ch.getAttackBonus());
				character.put("speed", ch.getSpeed());
				character.put("speed_bonus", ch.getSpeedBonus());
				
				characters.add(character);
			});			
			team.put("characters", characters);
			
			teams.add(team);
		});
		response.put("teams", teams);
		
		return response;		
	}
			
	
	

	private void broadcast(String message) {
		sessions.values().stream().forEach(s -> {
			send(s, message);
		});
	}
	
	public void send(WebSocketSession session, String message) {
		try {
			System.out.println(session.getId()+": "+message);
			session.sendMessage(new TextMessage(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	private String stringify(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}		
	}
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		sessions.put(sessionId, session);
		System.out.println(sessionId+": CONNECTED");
		
		//test
		init();
				
		send(session, stringify(session(session)));
		//send(session, stringify(status()));
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		sessions.remove(sessionId);
		System.out.println(sessionId+": DISCONNECTED");
	}
	
	
	
}















