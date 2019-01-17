package farguito.sarlanga.tournament.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ya-son-las-seis")
public class YaSonLasSeisController {


	@GetMapping
	public String sonLasSeis() {
		LocalDateTime now = LocalDateTime.now();
		if(now.getHour() < 18) {
			return "No, son las "+now.getHour()+":"+String.format("%02d", now.getMinute());
		}
		return "SI :D";
	}
	

	@GetMapping("hora")
	public String hora() {
		LocalDateTime now = LocalDateTime.now();
		return "son las "+now.getHour()+":"+String.format("%02d", now.getMinute());
	}
	
}
