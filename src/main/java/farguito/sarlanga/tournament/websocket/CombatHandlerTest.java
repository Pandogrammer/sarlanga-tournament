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
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.DefoldResponse;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.connection.TeamDTO;
import farguito.sarlanga.tournament.controller.MatchService;

@Component
public class CombatHandlerTest extends TextWebSocketHandler {	
	
	private ObjectMapper mapper = new ObjectMapper();
	
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
				send(session, action(request, session.getId()));

			} else if(request.getMethod().equals("reconnect_request")){
				reconnect(request, session.getId());	

			} else if(request.getMethod().equals("status_request")){
				send(session, status(session.getId()));

			} else if(request.getMethod().equals("teams_request")){
				send(session, teams(session.getId()));
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
	
	
	private void init() {
		this.cards = (CardFactory) SarlangaContext.getAppContext().getBean("cardFactory");	
		this.matchs = (MatchService) SarlangaContext.getAppContext().getBean("matchService");	
				
		Match match = this.matchs.create(10, cards.getCards());
		
		buildTeam(match, "eb105bc783cf4e22bdce6bff61ad5a431090728985");
		buildTeam(match, "7c5dd7987b93456db87a2a7e2ea9420c1090728985");
		
		this.matchs.start(match.getId());
	}
	
	private void buildTeam(Match match, String accountId) {
		match.addPlayer(accountId);

		TeamDTO team = match.getTeamDTO(accountId);
    	team.addCharacter(1, 1, cards.getCriatures().get(0));
    	team.addCharacter(2, 2, cards.getCriatures().get(1));
    	team.getCharacter(1).addAction(cards.getActions().get(0));
    	team.getCharacter(2).addAction(cards.getActions().get(0));
    	
		match.confirmTeam(accountId);	
	}
	

	private DefoldResponse action(DefoldRequest request, String sessionId) {
		DefoldResponse response = new DefoldResponse("action_response");
		CombatSystem system = getSystem(sessionId);
		
		int team = system.getActiveCharacter().getTeam()-1;
		boolean validTurn = system.getTeams().get(team).getOwner().equals(this.session_account.get(sessionId));
		
		if(validTurn) {
			Integer actionId = (int) request.get("action");
			List<Integer> objectiveIds = (List) request.get("objectives");
			
			Action action = system.getActiveCharacter().getActions().get(actionId);
			List<Character> objectives = new ArrayList<>();
			
			for(int i = 0; i < objectiveIds.size(); i++) {
				objectives.add(system.getCharacter(objectiveIds.get(i)));
			}
				
			
			system.prepareAction(action, objectives);
			if(system.validateObjectives(action)) {
				system.executeAction(action);
				system.nextTurn();
				broadcast(stringify(results(sessionId)));
				system.getLogger().deleteResults();
				broadcast(stringify(status(sessionId)));			
				response.put("success", true);
			} else {
				response.put("success", false);
				response.put("reason", "Invalid target.");
			}	
		} else {
			response.put("success", false);
			response.put("reason", "Not your turn.");
		}	
		return response;			
	}
	
	private DefoldResponse status(String sessionId) {		
		DefoldResponse response = new DefoldResponse("status_response");
		CombatSystem system = getSystem(sessionId);
		
		//estado del turno
		//estado de los personajes
		//estado efectos duraderos


		response.put("active_team", system.getActiveCharacter().getTeam());
		
		Map<String, Object> activeCharacter = new LinkedHashMap<>();
		activeCharacter.put("id", system.getActiveCharacter().getId());				
		activeCharacter.put("actions", system.getActiveCharacter().getActions());
		
		response.put("active_character", activeCharacter);
		
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
		
		return response;
	}
	
	
	private DefoldResponse results(String sessionId) {
		DefoldResponse response = new DefoldResponse("result_response");
		CombatSystem system = getSystem(sessionId);
		
		List<Object> results = new ArrayList<>();
		system.getLogger().getResults().stream().forEach(r -> {
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

	private DefoldResponse teamNumber(DefoldRequest request) {
		DefoldResponse response = new DefoldResponse("team_number_response");	
		
		String accountId = (String) request.get("account_id");
		
		response.put("team_number", this.matchs.get(accountId).getTeamNumber(accountId));		
		
		return response;	
		
	}
	
	private void accountLink(DefoldRequest request) {			
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
	

	public void send(WebSocketSession session, DefoldResponse response) {
		send(session, stringify(response));
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
	
	private CombatSystem getSystem(String sessionId) {
		return this.matchs.get(this.session_account.get(sessionId)).getSystem();
	}
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		sessions.put(sessionId, session);
		System.out.println(sessionId+": CONNECTED");
		
		//test
		init();
				
		send(session, session(session));
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		sessions.remove(sessionId);
		System.out.println(sessionId+": DISCONNECTED");
	}
	
	
	
}















