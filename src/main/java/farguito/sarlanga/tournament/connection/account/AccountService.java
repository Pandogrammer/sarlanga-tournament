package farguito.sarlanga.tournament.connection.account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class AccountService {

	private Map<String, Account> accounts = new HashMap<>(); 
	
	public Account create(String secret) {
		Account acc = new Account();

		String accountId = UUID.randomUUID().toString().replaceAll("-", "");
		String encodedSecret = encode(secret);
				
		accountId += encodedSecret;
		
		acc.setAccountId(accountId);
		acc.setLevel(1);
		acc.setWins(0);
		acc.setSecret(encodedSecret);
		
		accounts.put(accountId, acc);
		
		return acc;
	}
	
	public Account recover(String accountId, String oldSecret, String newSecret) {
		Account acc = accounts.get(accountId);
		String encodedOldSecret = encode(oldSecret);
		
		if(acc.getSecret().equals(encodedOldSecret)) {
			accounts.remove(accountId);
			
			accountId = accountId.substring(0, accountId.length()-encodedOldSecret.length());
			
			String encodedSecret = encode(newSecret);
			accountId += encodedSecret;
			
			acc.setSecret(encodedSecret);
			
			accounts.put(accountId, acc);
			
			return acc;			
		}
		
		return null;
	}
	
	
	private String encode(String secret) {		
		byte[] bytes = secret.getBytes();
		int value = 0;
		String encoded = "";
		
		for(int i = 0; i < bytes.length; i++) {
			encoded += bytes[i];
			
			if (encoded.length() > 6) {
				value += Integer.parseInt(encoded) % 1000000000;
				encoded = "";
			}
		}
		
		return String.valueOf(value).replaceAll("-", "");
	}
	
}
