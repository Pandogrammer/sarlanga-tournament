import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import farguito.sarlanga.tournament.connection.account.Account;
import farguito.sarlanga.tournament.connection.account.AccountService;

@RunWith(SpringRunner.class)
public class AccountServiceTest {
 
    @TestConfiguration
    static class AccountServiceConfig {
  
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }
    }
 
    @Autowired
    private AccountService accountService;
    
    @Test
    public void accountCreation() {
    	Account ac = accountService.create("caca");
    	
    	assertNotNull(ac);    	
    }
    
    @Test
    public void accountRecovery() {
    	Account ac = accountService.create("caca");
    	
    	Account ac2 = accountService.recover(ac.getAccountId(), "cacariana", "cacona");
    	assertNull(ac2);
    	
    	Account ac3 = accountService.recover(ac.getAccountId(), "caca", "cacona");
    	assertNotNull(ac3);
    }
}









