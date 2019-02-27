package farguito.sarlanga.tournament.websocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.TeamDTO;
import farguito.sarlanga.tournament.controller.MatchService;

public class MatchMaker extends TextWebSocketHandler {
	private ObjectMapper mapper = new ObjectMapper();
	
	private MatchService matchs;
	private CardFactory cards;
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	
	
	public MatchMaker(CardFactory cards, MatchService matchs) {
		this.cards = cards;
		this.matchs = matchs;
	}
	
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		try { 
			System.out.println(message.getPayload());
			DefoldRequest request = mapper.readValue(message.getPayload(), DefoldRequest.class);
				
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}		
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
		
		sessions.add(session);
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId+": DISCONNECTED");
		System.out.println(status.getReason());
		
		sessions.remove(session);
	}

}
