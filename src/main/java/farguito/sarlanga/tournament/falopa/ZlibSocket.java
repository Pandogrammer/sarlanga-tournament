package farguito.sarlanga.tournament.falopa;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ZlibSocket extends TextWebSocketHandler {	
	
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		try { 
			System.out.println(message.getPayload());
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
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId+": DISCONNECTED");
		System.out.println(status.getReason());
	}
	
	
	
}















