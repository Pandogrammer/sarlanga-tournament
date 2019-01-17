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
		int hora = now.getHour() - 3;
		if(hora < 18) {
			if(hora == 17 && now.getMinute() > 50)
				return "NO, PERO FALTAN "+(60-now.getMinute());
			else
				return "No, son las "+hora+":"+String.format("%02d", now.getMinute());				
		} else {
			return "SEEEEEEEEEEEEEEEEEEE :D"+"\n"+"https://www.youtube.com/watch?v=oqGDHSGohjw";
		}
	}
	

	@GetMapping("hora")
	public String hora() {
		LocalDateTime now = LocalDateTime.now();
		return "son las "+now.getHour()+":"+String.format("%02d", now.getMinute());
	}
	
}
