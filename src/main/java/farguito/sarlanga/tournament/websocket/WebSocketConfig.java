package farguito.sarlanga.tournament.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import farguito.sarlanga.tournament.falopa.SuperNave;
import farguito.sarlanga.tournament.falopa.ZLibSocket;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new CombatHandler(), "/test");
		registry.addHandler(new ZLibSocket(), "/zlib");
	}


}
