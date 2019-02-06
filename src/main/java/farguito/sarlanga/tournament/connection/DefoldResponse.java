package farguito.sarlanga.tournament.connection;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

public class DefoldResponse {
	
	private String message_id;
	private Map<String, Object> message;
	
	public DefoldResponse(String message_id) {
		this.message_id = message_id;
	}
	
	
	public void put(String key, Object value) {
		if(message == null) message = new LinkedHashMap<>();
		
		message.put(key, value);
	}

	public String getMessage_id() {
		return message_id;
	}

	@JsonInclude(Include.NON_NULL)
	public Map<String, Object> getMessage() {
		return message;
	}	
	
}
