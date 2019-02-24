package farguito.sarlanga.tournament.websocket;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import farguito.sarlanga.tournament.SarlangaContext;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.DefoldRequest;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.connection.TeamDTO;
import farguito.sarlanga.tournament.controller.MatchService;

@Component
public class RoomHandler extends TextWebSocketHandler {	
	private ObjectMapper mapper = new ObjectMapper();
	
	private List<Map<String, Object>> team;
	
	private MatchService matchs;	
	private CardFactory cards;
	
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		try { 
			System.out.println(message.getPayload());
			DefoldRequest request = mapper.readValue(message.getPayload(), DefoldRequest.class);	
			
			if(request.getMethod().equals("confirm_team_request")) {
				team = (List<Map<String, Object>>) request.get("team");
				start();
				send(session, "{ \"message_id\":\"start_match_response\" }");
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}		
	}	
	
	private void init() {
		if(this.cards == null)
			this.cards = (CardFactory) SarlangaContext.getAppContext().getBean("cardFactory");	
		if(this.matchs == null)
			this.matchs = (MatchService) SarlangaContext.getAppContext().getBean("matchService");	
	}
	
	private void start() {
		Match match = this.matchs.create(100, cards.getCards());
		
		buildTeam(match, "eb105bc783cf4e22bdce6bff61ad5a431090728985");
		buildTeam(match, "7c5dd7987b93456db87a2a7e2ea9420c1090728985");
		
		this.matchs.start(match.getId());
		
	}


	private void buildTeam(Match match, String accountId) {
		TeamDTO team = match.addPlayer(accountId);
		
    	if(accountId.equals("eb105bc783cf4e22bdce6bff61ad5a431090728985")) {
    		for(int i = 0; i < this.team.size(); i++) {
        		team.addCharacter((int) this.team.get(i).get("line")
						 ,(int) this.team.get(i).get("position")
						 , cards.getCriatures().get((int) this.team.get(i).get("card_id")));
        		
        		List<Integer> actions =  (List<Integer>) this.team.get(i).get("actions");
        		
        		for(int j = 0; j < actions.size(); j++)
        			team.getCharacter(i+1).addAction(cards.getActions().get(actions.get(j)));
    		}
    		
    	} else {
        	team.addCharacter(1, 1, cards.getCriatures().get(0));
    		team.addCharacter(2, 2, cards.getCriatures().get(1));    	
			team.addCharacter(1, 2, cards.getCriatures().get(2));    		
	    	team.getCharacter(1).addAction(cards.getActions().get(0));
	    	team.getCharacter(1).addAction(cards.getActions().get(2));
	    	team.getCharacter(2).addAction(cards.getActions().get(4));
	    	team.getCharacter(2).addAction(cards.getActions().get(1));
	    	team.getCharacter(2).addAction(cards.getActions().get(2));
	    	team.getCharacter(2).addAction(cards.getActions().get(3));	
	    	team.getCharacter(3).addAction(cards.getActions().get(0));
    	}
    	
		match.confirmTeam(accountId);	
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
		init();
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId+": DISCONNECTED");
		System.out.println(status.getReason());
	}
	
	
	
}














