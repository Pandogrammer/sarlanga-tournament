package farguito.sarlanga.tournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SarlangaTournamentApplication {

	public static final String URI = "https://sarlanga-tournament.herokuapp.com/";
	public static final String URI_LOCAL = "localhost:8080/";
	
	public static void main(String[] args) {
		SpringApplication.run(SarlangaTournamentApplication.class, args);
	}
}
