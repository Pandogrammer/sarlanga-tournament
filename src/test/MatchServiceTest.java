import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.connection.CharacterDTO;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.connection.TeamDTO;
import farguito.sarlanga.tournament.controller.MatchService;

@RunWith(SpringRunner.class)
public class MatchServiceTest {

    @TestConfiguration
    static class AccountServiceConfig {
  
        @Bean
        public MatchService matchService() {
            return new MatchService();
        }
        
        @Bean
        public CardFactory cardFactory() {
        	return new CardFactory();
        }
        
    }

    @Autowired
    private MatchService matchService;
    @Autowired
    private CardFactory cardFactory;
    
    @Test
    public void createMatch() {
    	Match m = matchService.create(10, cardFactory.getCards());
    	
    	assertNotNull(m);
    	
    	assertNotNull(matchService.get(m.getId()));
    }

    @Test
    public void enterMatch() {
    	Match m = mockMatch();
    	m.addPlayer("caca");
    
    	assertNotNull(m.getPlayerTeamDTO("caca"));
    }    

    @Test
    public void modifyTeam() {
    	Match m = mockMatch();
    	TeamDTO team = m.addPlayer("caca");
    	
    	assertTrue(team.getCharacters().isEmpty());
    	
    	mockCharacters(team);
    	
    	CharacterDTO ch = m.getPlayerTeamDTO("caca").getCharacter(1); 
    	
    	assertNotNull(ch);
    	
    	mockActions(ch);
    	
    	assertFalse(ch.getActions().isEmpty());    	
    }
    
    @Test
    public void startMatch() {
    	Match m = mockMatch();
    	TeamDTO teamA = m.addPlayer("caca");

    	TeamDTO teamB = m.addPlayer("cacona");
    	
    	assertFalse(m.confirmTeam("caca"));
    	assertFalse(m.start());
    	
    	mockTeam(teamA);
    	mockTeam(teamB);
    	
    	assertTrue(m.confirmTeam("caca"));
    	assertTrue(m.confirmTeam("cacona"));
    	
    	assertTrue(m.start());
    	
    	assertEquals(m.getState(), "PLAYING");
    	assertNotNull(m.getSystem());    	
    	
    }
    
    @Test
    public void getMatch() {
    	Match m = matchService.create(10, cardFactory.getCards());
    	mockTeam(m.addPlayer("caca"));
    	mockTeam(m.addPlayer("cacona"));

    	assertTrue(m.confirmTeam("caca"));
    	assertTrue(m.confirmTeam("cacona"));
    	
    	assertTrue(matchService.start(m.getId()));
    	
    	assertEquals(m.getId(), matchService.findByAccount("caca").getId());
    	
    }
    
    
    private Match mockMatch() {
    	Match m = new Match(10, cardFactory.getCards());
    	return m;    	
    }
    
    private CharacterDTO mockActions(CharacterDTO ch) {
    	ch.addAction(cardFactory.getActions().get(0));
    	ch.addAction(cardFactory.getActions().get(1));
    	return ch;
    }
    
    private TeamDTO mockCharacters(TeamDTO team) {
    	team.addCharacter(1, 1, cardFactory.getCriatures().get(0));
    	team.addCharacter(2, 2, cardFactory.getCriatures().get(1));
    	return team;
    }
    
    private TeamDTO mockTeam(TeamDTO team) {
    	if(team == null) team = new TeamDTO();
    	team.addCharacter(1, 1, cardFactory.getCriatures().get(0));
    	team.addCharacter(2, 2, cardFactory.getCriatures().get(1));
    	team.getCharacter(1).addAction(cardFactory.getActions().get(0));
    	team.getCharacter(2).addAction(cardFactory.getActions().get(0));    	
    	return team;
    }
    /*
     * crear una sala
     * entrar a una sala
     * crear un equipo
     * modificar equipo
     *   abm personajes abm acciones
     * poner ready
     * iniciar partida
     */
    
    
}
