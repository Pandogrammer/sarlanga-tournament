package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("invocador")
public class Invocador {

	private List<String> invocaciones = new ArrayList<>();
	
	@GetMapping("/invocaciones")
	public String invocaciones() {
		StringBuilder inv = new StringBuilder();
		invocaciones.stream().forEach(i -> {
			inv.append(i).append(System.getProperty("line.separator"));
		});
		return inv.toString();
	}
	

	@GetMapping("/{objeto}/{cantidad}")
	public String invocar(@PathVariable String objeto, @PathVariable int cantidad) {
		if(cantidad < 100) {
			return "Es muy poco, pedi mas.";
		} else {
			invocaciones.add(cantidad+" "+objeto);		
			return "NECESITO "+cantidad+" "+objeto.toUpperCase()+" PARA SACIAR MI SED DE LABURANTE";
		}
	}
	
	
}






