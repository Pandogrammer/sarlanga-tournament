package farguito.sarlanga.tournament.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.DefoldResponse;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.controller.MatchService;

public class MatchMaker extends TextWebSocketHandler {
	private ObjectMapper mapper = new ObjectMapper();
	
	private MatchService matchs;

	private Map<String, WebSocketSession> session_websocketsession = new HashMap<>();
	private Map<String, String> session_account = new HashMap<>();
	private Map<String, String> account_session = new HashMap<>();
	
	
	public MatchMaker(MatchService matchs) {
		this.matchs = matchs;
	}
	
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		try { 
			DefoldRequest request = mapper.readValue(message.getPayload(), DefoldRequest.class);			

			if(request.getMethod().equals("account_link_request"))
				accountLink(request);
			
			else if(request.getMethod().equals("players_ready_request"))
				playersReady(session.getId());
				
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}		
	}	
	
	private void playersReady(String sessionId) {
		String accountId = session_account.get(sessionId);
		if(this.matchs.queueReady(accountId)) {
			DefoldResponse response = new DefoldResponse("players_ready_response");
			response.put("success", true);
			broadcastInMatch(sessionId, stringify(response));
		}
	}

	private void accountLink(DefoldRequest request) {			
		String sessionId = (String) request.get("session_id");
		String accountId = (String) request.get("account_id");
		session_account.put(sessionId, accountId);	
		account_session.put(accountId, sessionId);		
	}

	private DefoldResponse session(WebSocketSession session){
		DefoldResponse response = new DefoldResponse("session_response");
		
		response.put("session_id", session.getId());
		
		return response;		
	}

	public void send(WebSocketSession session, DefoldResponse response) {
		send(session, stringify(response));
	}

	private String stringify(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}		
	}

	private void broadcastInMatch(String sessionId, String message) {
		Match match = this.matchs.get(this.session_account.get(sessionId));
		match.getPlayers().stream().forEach(p -> {
			if(!p.equals(MatchService.IA))
			send(this.session_websocketsession.get(this.account_session.get(p)), message);
		});
	}
	
	public void send(WebSocketSession session, String message) {
		try {
			session.sendMessage(new TextMessage(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		this.session_websocketsession.put(sessionId, session);
		System.out.println(sessionId+": CONNECTED");
		
		send(session, session(session));
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId+": DISCONNECTED");
		System.out.println(status.getReason());
		
		remove(session.getId());
	}

	private void remove(String sessionId) {
		if(this.session_account.containsKey(sessionId)) {
			String accountId = this.session_account.get(sessionId);
			this.matchs.removeFromQueue(accountId);
			this.account_session.remove(accountId);
			this.session_account.remove(sessionId);
			this.session_websocketsession.remove(sessionId);
		}		
	}

}
