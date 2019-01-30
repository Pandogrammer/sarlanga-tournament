package farguito.sarlanga.tournament.websocket;

import java.util.Map;

public class DefoldRequest {

	private String method;
	private Map<String, Object> object;
	
	public String getMethod() {
		return method;
	}
	
	public Map getObject() {
		return object;
	}
	
	public Object get(String key) {
		return object.get(key);
	}
	
			
}