package farguito.sarlanga.tournament.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.controller.MatchService;

public class MatchMaker extends TextWebSocketHandler {
	private ObjectMapper mapper = new ObjectMapper();
	
	private MatchService matchs;
	private CardFactory cards;
	
	private Map<String, String> session_account = new HashMap<>();
	
	
	public MatchMaker(CardFactory cards, MatchService matchs) {
		this.cards = cards;
		this.matchs = matchs;
	}
	
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		try { 
			System.out.println(message.getPayload());
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
		
		this.matchs.queueReady(accountId);
	}

	private void accountLink(DefoldRequest request) {			
		String sessionId = (String) request.get("session_id");
		String accountId = (String) request.get("account_id");
		session_account.put(sessionId, accountId);			
	}

	public void send(WebSocketSession session, String message) {
		try {
			System.out.println(session.getId()+": "+message);
			session.sendMessage(new TextMessage(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId+": CONNECTED");
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
			this.session_account.remove(sessionId);
		}		
	}

}
