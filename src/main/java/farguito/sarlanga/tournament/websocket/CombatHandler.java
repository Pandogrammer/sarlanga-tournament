package farguito.sarlanga.tournament.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.DefoldResponse;
import farguito.sarlanga.tournament.controller.MatchService;

public class CombatHandler extends TextWebSocketHandler {	
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private MatchService matchs;	
	
	private Map<String, WebSocketSession> session_websocketsession = new HashMap<>();
	private Map<String, String> session_account = new HashMap<>();
	private Map<String, String> account_session = new HashMap<>();
	
	private Map<String, Integer> account_team = new HashMap<>();
	
	public CombatHandler(MatchService matchs) {
		this.matchs = matchs;
	}

	//todo entra por aca
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		try { 
			DefoldRequest request = mapper.readValue(message.getPayload(), DefoldRequest.class);			
			System.out.println(request.getMethod());
			if(request.getMethod().equals("action_request")) {				
				try {
					action(request, session.getId());
					send(session, actionExecution(true));
					broadcastInMatch(getSystem(session.getId()), stringify(results(session.getId())));

					handleIaTurn(session.getId());//caca
					
					broadcastInMatch(getSystem(session.getId()), stringify(status(session.getId())));					
				} catch (ActionExecutionException e) {
					send(session, actionExecution(false, e.getMessage()));					
				}

			} else if(request.getMethod().equals("reconnect_request")){
				reconnect(request, session.getId());	

			} else if(request.getMethod().equals("status_request")){
				send(session, status(session.getId()));

			} else if(request.getMethod().equals("teams_request")){
				send(session, teams(session.getId()));		
				
				handleIaTurn(session.getId());//caca
				
				send(session, status(session.getId()));
				
			} else if(request.getMethod().equals("account_link_request")){
				accountLink(request);
				send(session, teamNumber(request));
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}
	}

	private void handleIaTurn(String id) { //caca y encima buguea
		boolean wasIaTurn = iaTurn(getSystem(id));
		while(iaTurn(getSystem(id))) {}
		if(wasIaTurn) broadcastInMatch(getSystem(id), stringify(results(id)));		
	}

	private DefoldResponse actionExecution(boolean success) {
		return this.actionExecution(success, null);
	}
	private DefoldResponse actionExecution(boolean success, String message) {
		DefoldResponse response = new DefoldResponse("action_response");
		
		response.put("success", success);
		
		if(message != null) {
			response.put("reason", message);
		}
		
		return response;		
	}

	private void action(DefoldRequest request, String sessionId) throws ActionExecutionException{
		CombatSystem system = getSystem(sessionId);
		
		if(!system.isInProgress()) 
			throw new ActionExecutionException("Match has ended.");
		
		int team = system.getActiveCharacter().getTeam()-1;
		boolean validTurn = system.getTeams().get(team).getOwner().equals(this.session_account.get(sessionId));
		
		if(!validTurn) 
			throw new ActionExecutionException("Not your turn.");
		
		Integer actionId = (int) request.get("action");
		List<Integer> objectiveIds = (List) request.get("objectives");
		
		Action action = system.getActiveCharacter().getActions().get(actionId);
		List<Character> objectives = new ArrayList<>();
		
		for(int i = 0; i < objectiveIds.size(); i++) {
			objectives.add(system.getCharacter(objectiveIds.get(i)));
		}
					
		system.prepareAction(action, objectives);
		system.validateObjectives(action);
		system.executeAction(action);
		system.nextTurn();		
				
	}
	
	private boolean iaTurn(CombatSystem system) {
		try {
			if(system.isInProgress()) {
				int team = system.getActiveCharacter().getTeam()-1;
				boolean iaTurn = system.getTeams().get(team).getOwner().equals(MatchService.IA);
				
				if(iaTurn) {
					Action action = system.getActiveCharacter().getActions().get(0);
					List<Character> objectives = new ArrayList<>();
					
					objectives.add(system.getActiveCharacter());
								
					system.prepareAction(action, objectives);
					system.validateObjectives(action);
					system.executeAction(action);	
					system.nextTurn();
					return true;
				}
				return false;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private DefoldResponse status(String sessionId) {		
		DefoldResponse response = new DefoldResponse("status_response");
		CombatSystem system = getSystem(sessionId);
		
		//estado del turno
		//estado de los personajes
		//estado efectos duraderos

		List<Object> charactersStatus = new ArrayList<>();
		system.getTeams().stream().forEach(t -> {
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
		
		if(system.isInProgress()) {
			response.put("active_team", system.getActiveCharacter().getTeam());
			
			int teamNumber = this.account_team.get(this.session_account.get(sessionId));
			
			Map<String, Object> activeCharacter = new LinkedHashMap<>();
			//se saca la validacion porque hay que mejorar el broadcast
			//deberia mandar mensajes diferentes para cada jugador, pero al ser broadcast chequea desde el jugador 
			//que hizo la accion en vez de cada uno
			//if (teamNumber == system.getActiveCharacter().getTeam()) {
				activeCharacter.put("id", system.getActiveCharacter().getId());	
				
				List<Object> activeCharacterActions = new ArrayList<>();
				system.getActiveCharacter().getActions().stream().forEach(a -> {
					Map<String, Object> action = new HashMap<>();
					action.put("name", a.getName());
					
					activeCharacterActions.add(action);
				});
				activeCharacter.put("actions", activeCharacterActions);
			
				response.put("active_character", activeCharacter);
			//}
		} else {
			response.put("winning_team", system.getWinningTeam());
		}
		
		return response;
	}
	
	
	private DefoldResponse results(String sessionId) {
		DefoldResponse response = new DefoldResponse("result_response");
		CombatSystem system = getSystem(sessionId);
		
		List<Object> results = new ArrayList<>();
		system.getLogger().getResults().stream().forEach(r -> {
			results.add(r);
		});
		
		system.getLogger().deleteResults();	
		
		response.put("results", results);
		
		return response;
	}


	private DefoldResponse session(WebSocketSession session){
		DefoldResponse response = new DefoldResponse("session_response");
		
		response.put("session_id", session.getId());
		
		return response;		
	}

	private DefoldResponse teamNumber(DefoldRequest request) {
		DefoldResponse response = new DefoldResponse("team_number_response");	
		
		String accountId = (String) request.get("account_id");
		Integer teamNumber = this.matchs.get(accountId).getTeamNumber(accountId);
		this.account_team.put(accountId, teamNumber);
		response.put("team_number", teamNumber);		
		
		return response;	
		
	}
	
	private void accountLink(DefoldRequest request) {			
		String sessionId = (String) request.get("session_id");
		String accountId = (String) request.get("account_id");
		session_account.put(sessionId, accountId);
		account_session.put(accountId, sessionId);
	}

	
	private void reconnect(DefoldRequest request, String newSessionId) {		
		String oldSessionId = (String) request.get("session_id");
		if(session_account.containsKey(oldSessionId)) {
			String accountId = session_account.get(oldSessionId);
			session_account.put(newSessionId, accountId);
			account_session.put(accountId, newSessionId);
		}

		System.out.println(newSessionId+": RECONNECTED - WAS ["+oldSessionId+"]");
	}

	private DefoldResponse teams(String sessionId){
		DefoldResponse response = new DefoldResponse("teams_response");
		CombatSystem system = getSystem(sessionId);
		
		List<Map<String, Object>> teams = new ArrayList<>();
		system.getTeams().stream().forEach(t -> {
			Map<String, Object> team = new HashMap<>();
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
				character.put("name", ch.getName());
				
				characters.add(character);
			});			
			team.put("characters", characters);
			
			teams.add(team);
		});
		response.put("teams", teams);
		
		return response;		
	}
			
	
	

	private void broadcast(String message) {
		session_websocketsession.values().stream().forEach(s -> {
			send(s, message);
		});
	}
	
	private void broadcastInMatch(CombatSystem system, String message) {
		system.getTeams().stream().forEach(t -> {
			if(!t.getOwner().equals(MatchService.IA))
			send(this.session_websocketsession.get(this.account_session.get(t.getOwner())), message);
		});		
	}

	public void send(WebSocketSession session, DefoldResponse response) {
		send(session, stringify(response));
	}
	
	public void send(WebSocketSession session, String message) {
		try {
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
	
	private CombatSystem getSystem(String sessionId) {
		return this.matchs.get(this.session_account.get(sessionId)).getSystem();
	}
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		session_websocketsession.put(sessionId, session);
		System.out.println(sessionId+": CONNECTED");
		
		send(session, session(session));
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		session_websocketsession.remove(sessionId);
		System.out.println(sessionId+": DISCONNECTED");
	}


	public void setMatchs(MatchService matchs) {
		this.matchs = matchs;
	}
	
	
	
}















