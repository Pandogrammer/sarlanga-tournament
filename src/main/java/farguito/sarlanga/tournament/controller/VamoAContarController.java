package farguito.sarlanga.tournament.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("contando-las-horas")
public class VamoAContarController {
		
	private Map<String, String> contadores = new HashMap<>();
	private List<String> conteos = new ArrayList<>();
	
	@GetMapping
	public String contar(HttpServletRequest request) {
		String ip = request.getRemoteAddr();

		LocalDateTime now = LocalDateTime.now();
		
		int hora = 18 - (now.getHour() - 3);
		int minutos = 60 - now.getMinute();
		if (minutos != 60) hora--;
		
		StringBuilder respuesta = new StringBuilder();
		if(contadores.containsKey(ip)) {
			conteos.add(contadores.get(ip)+": "+hora +"."+minutos);
			
			conteos.stream().forEach(c -> {
				respuesta.append(c); respuesta.append(System.getProperty("line.separator"));
			});
			
			return respuesta.toString();
		} else {
			return "Te falta registrarte, hacelo en /quien-soy?nombre=_____";
		}
    }

	@GetMapping("quien-soy")
	public String registrar(@RequestParam String nombre, HttpServletRequest request) {		
		String ip = request.getRemoteAddr();
		if(!contadores.containsKey(ip)) {
			contadores.put(ip, nombre);
			return ":)";
		} else {
			return ":o";
		}
	}
	
	
}
