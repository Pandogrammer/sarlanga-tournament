package farguito.sarlanga.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.Criature;
import farguito.sarlanga.tournament.cards.actions.PoisonSpit;
import farguito.sarlanga.tournament.cards.actions.Punch;
import farguito.sarlanga.tournament.cards.criatures.Ortivactus;
import farguito.sarlanga.tournament.cards.criatures.Sapurai;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.combat.Team;

@RestController
@RequestMapping("v1")
public class CombatController {

	private List<Carta> criaturas = new ArrayList<>();
	private List<Carta> acciones = new ArrayList<>();
	private Map<Integer, Partida> partidas = new HashMap<>();
	
	private class Carta {
		int id;
		int esencia;
		String nombre;
		String tipo;
		
		public Carta(int id, int esencia, String nombre, String tipo) {
			this.esencia = esencia;
			this.nombre = nombre;
			this.tipo = tipo;
		}

		
		public int getId() {
			return id;
		}
		public int getEsencia() {
			return esencia;
		}
		public String getNombre() {
			return nombre;
		}
		public String getTipo() {
			return tipo;
		}
		
	}
	
	private class Partida {
		int esencia;
		CombatSystem sistema;
		List<Carta> cartas = new ArrayList<>();
		List<Team> equipos = new ArrayList<>();
		
		public Partida(int esencia) {
			this.esencia = esencia;
			
			//generacion random iria aca
			cartas.addAll(criaturas);
			cartas.addAll(acciones);
			
		}
		
		public void iniciar() {
			this.sistema = new CombatSystem(equipos);
		}
		
		public void agregarEquipo(Team equipo) {
			this.equipos.add(equipo);
		}
		
		public int getEsencia() {
			return esencia;
		}		
		public List<Carta> getCartas() {
			return cartas;
		}
		public CombatSystem getSistema() {
			return sistema;
		}
		
		
	}
	
	@PostConstruct
	private void init() {
		this.criaturas.add(new Carta(this.criaturas.size(), 1, "Ortivactus", "Criatura"));
		this.criaturas.add(new Carta(this.criaturas.size(), 1, "Sapurai", "Criatura"));
		
		this.acciones.add(new Carta(this.acciones.size(), 1, "Punch", "Accion"));
		this.acciones.add(new Carta(this.acciones.size(), 2, "Poison Spit", "Accion"));		
		
	}
	
	@GetMapping("crear")
	public String crearPartida(@RequestParam int esencia) {
		this.partidas.put(this.partidas.size(), new Partida(esencia));
		
		return "Id de la partida: "+(this.partidas.size()-1);
	}
	
	@GetMapping("{id}/entrar")
	public Map<String, Object> entrarPartida(@PathVariable Integer id, @RequestParam String equipo) {
		Map<String, Object> respuesta = new LinkedHashMap<>();

		if(this.partidas.containsKey(id)) {
			Partida p = this.partidas.get(id);
			try{
				List<Character> teamCharacters = new ArrayList<>();
				String[] personajes = equipo.split(":");
				for(int i = 0; i < personajes.length; i++) {
					boolean pj = true;
					Criature cr = null;
					List<Action> acs = new ArrayList<>();
					for(int j = 0; j < personajes[i].length(); j = j+2) {
						if(pj) {
							switch(personajes[i].charAt(j)) {
							case '0': cr = new Ortivactus(); break;
							case '1': cr = new Sapurai(); break;
							}
							pj = false;
						} else {
							switch(personajes[i].charAt(j)) {
							case '0': acs.add(new Punch()); break;
							case '1': acs.add(new PoisonSpit()); break;
							}
						}
					}
					teamCharacters.add(new Character(p.equipos.size(), cr, acs));
				}
				p.agregarEquipo(new Team(teamCharacters));
				respuesta.put("Acceso", "Ingresaste a la partida.");
				
			} catch (Exception e) {
				e.printStackTrace();				
			}		
		} else {
			respuesta.put("Error", "La partida no existe.");
		}
		
		return respuesta;
	}

	@GetMapping("{id}/iniciar")
	public Map<String, Object> iniciarPartida(@PathVariable Integer id) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.partidas.containsKey(id)) {
			Partida partida = this.partidas.get(id);
			partida.iniciar();
		} else {
			respuesta.put("Error", "La partida no existe.");
		}
		
		return respuesta;
	}
	


	@GetMapping("{id}/accion/{ac}:{team}:{obj}")
	public Map<String, Object> accion(@PathVariable Integer id
			  						  , @PathVariable Integer ac
			  						  , @PathVariable Integer team
									  , @PathVariable Integer obj) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.partidas.containsKey(id)) {
			Partida partida = this.partidas.get(id);
			CombatSystem sistema = partida.getSistema();

			Action action = sistema.getActiveCharacter().getActions().get(ac);
			sistema.prepareAction(
					action, sistema.getTeams().get(team).getCharacters().get(obj));
			sistema.executeAction(action);
			
			
			sistema.applyImmediateEffects();
			sistema.advancingTurns();
			do {
				sistema.checkLastingEffectReady();
				sistema.applyImmediateEffects();
				sistema.checkCharacterReady();
				if(sistema.getActiveCharacter() == null)
					sistema.advancingTurns();
			} while(sistema.getActiveCharacter() == null);
			
		} else {
			respuesta.put("Error", "La partida no existe.");
		}
		
		return respuesta;
	}

	@GetMapping("{id}/mensajes")
	public Map<String, Object> mensajes(@PathVariable Integer id) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.partidas.containsKey(id)) {
			Partida partida = this.partidas.get(id);
			respuesta.put("Mensajes", partida.getSistema().getMessages());
		} else {
			respuesta.put("Error", "La partida no existe.");
		}
		
		return respuesta;
	}
	
	@GetMapping("{id}")
	public Map<String, Object> informacionPartida(@PathVariable Integer id) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.partidas.containsKey(id)) {
			Partida partida = this.partidas.get(id);
			respuesta.put("Esencia", partida.getEsencia());
			respuesta.put("Equipos", partida.equipos.size());
			respuesta.put("Cartas disponibles", partida.getCartas());
		} else {
			respuesta.put("Error", "La partida no existe.");
		}
		
		return respuesta;
	}
	
	
}
