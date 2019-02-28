package farguito.sarlanga.tournament.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.controller.MatchService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(combatHandler(), "/match");
		registry.addHandler(matchMaker(), "/match-maker");
	}

	@Bean
	public RoomHandler roomHandler() {
		RoomHandler rh = new RoomHandler(cardFactory(), matchService());		
		return rh;		
	}
	

	@Bean
	public MatchMaker matchMaker() {
		MatchMaker mm = new MatchMaker(cardFactory(), matchService());		
		return mm;		
	}
	
	
	@Bean
	public CombatHandler combatHandler() {
		CombatHandler ch = new CombatHandler(matchService());		
		return ch;
	}

	@Bean
	public MatchService matchService() {
		return new MatchService(cardFactory());
	}

	@Bean
	public CardFactory cardFactory() {
		return new CardFactory();
	}
	
	

}
