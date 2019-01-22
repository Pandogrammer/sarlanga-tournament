package farguito.sarlanga.tournament.falopa;

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
		int minutos = now.getMinute();
		if(hora < 18) {
			if(hora == 17 && minutos > 50) {
				if (minutos == 59) {
					return "NO, PERO FALTAN "+(60-now.getSecond());
				} else { 
					return "NO, PERO FALTAN "+(60-minutos);
				}
			} else {
				return "No, son las "+hora+":"+String.format("%02d", now.getMinute());
			}
		} else {
			return "SEEEEEEEEEEEEEEEEEEE :D"+System.getProperty("line.separator")+"https://www.youtube.com/watch?v=oqGDHSGohjw";
		}
	}
	

	@GetMapping("hora")
	public String hora() {
		LocalDateTime now = LocalDateTime.now();
		return "son las "+now.getHour()+":"+String.format("%02d", now.getMinute());
	}
	
}
