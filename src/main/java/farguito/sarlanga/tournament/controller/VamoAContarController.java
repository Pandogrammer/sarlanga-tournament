package farguito.sarlanga.tournament.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	private Map<String, String> conteos = new LinkedHashMap<>();
	
	@GetMapping
	public Map<String, String> contar(HttpServletRequest request) {
		String id = request.getSession().getId();

		LocalDateTime now = LocalDateTime.now();
		
		int hora = 18 - (now.getHour() - 3);
		int minutos = 60 - now.getMinute();
		if (minutos != 60) hora--;
		else minutos = 0;
		
		if(contadores.containsKey(id)) {
			String contador = contadores.get(id);
			String conteo = hora+"."+String.format("%02d",minutos);
			if(!conteos.containsKey(conteo)) {
				conteos.put(conteo, contador);
			} else {
				conteos.put(conteo, conteos.get(conteo)+", "+contador);
			}
			
			return conteos;
		} else {
			Map<String, String> respuesta = new LinkedHashMap<>();
			respuesta.put("mensaje", "Te falta registrarte, hacelo en:");
			respuesta.put("url", "https://sarlanga-tournament.herokuapp.com/contando-las-horas/quien-soy?nombre=TU_NOMBRE");
			return respuesta;
		}
    }

	@GetMapping("quien-soy")
	public Map<String, String> registrar(@RequestParam String nombre, HttpServletRequest request) {		

		Map<String, String> respuesta = new LinkedHashMap<>();
		
		if(nombre.equalsIgnoreCase("TU_NOMBRE")) {
			respuesta.put("~", "¬¬");
			respuesta.put("mensaje", "valía cambiarle el nombre");
		} else {
			String id = request.getSession().getId();
			
			if(!contadores.containsKey(id)) {
				contadores.put(id, nombre);
				respuesta.put("~", ":)");
			} else {
				contadores.put(id, nombre);
				respuesta.put("~", ":o");
			}
			respuesta.put("mensaje", "Bueno ahora volve a la url anterior:");
			respuesta.put("url", "https://sarlanga-tournament.herokuapp.com/contando-las-horas/");			
		}
		return respuesta;
	}
	
	
}
