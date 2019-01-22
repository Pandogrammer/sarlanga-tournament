package farguito.sarlanga.tournament.controller;

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
import farguito.sarlanga.tournament.cards.Card;
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
public class CombatControllerV1 {

	private List<Card> cartas = new ArrayList<>();
	private Map<Integer, Match> partidas = new HashMap<>();
	
	
	@PostConstruct
	private void init() {
		this.cartas.add(new Card(this.cartas.size(), 1, "Ortivactus", Ortivactus.class,  "Criatura"));
		this.cartas.add(new Card(this.cartas.size(), 1, "Sapurai", Sapurai.class, "Criatura"));
		
		this.cartas.add(new Card(this.cartas.size(), 1, "Punch", Punch.class, "Accion"));
		this.cartas.add(new Card(this.cartas.size(), 2, "Poison Spit", PoisonSpit.class, "Accion"));		
		
	}
	
	@GetMapping("crear")
	public Map<String, Object> crearPartida(@RequestParam int esencia) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		this.partidas.put(this.partidas.size(), new Match(esencia, cartas));
		
		respuesta.put("Id de la partida", this.partidas.size()-1);
		return respuesta;
	}
	
	//entro con equipo=0,2:0,2,3:1,2,3 etc
	@GetMapping("{id}/entrar")
	public Map<String, Object> entrarPartida(@PathVariable Integer id, @RequestParam String equipo) {
		Map<String, Object> respuesta = new LinkedHashMap<>();

		if(this.partidas.containsKey(id)) {
			Match p = this.partidas.get(id);
			try{
				List<Card> cartas = new ArrayList<>();
				List<Character> teamCharacters = new ArrayList<>();
				String[] personajes = equipo.split(":");
				for(int i = 0; i < personajes.length; i++) {
					for(int j = 0; j < personajes[i].length(); j = j+2) {
						cartas.add(this.cartas.get(java.lang.Character.getNumericValue(personajes[i].charAt(j))));
					}
				}
				int esencia = 0;
				for(Card c : cartas) {
					esencia += c.getEssence();
				}
				
				if(esencia <= p.getEssence()) {
					try {
						int i = 0;
						while(i < cartas.size()) {
							Criature cr = null;
							List<Action> acs = new ArrayList<>();
							//Criatura
							cr = (Criature) cartas.get(i).getObject().newInstance();
							i++;
							while(i < cartas.size() && cartas.get(i).getType().equals("Accion")) {
								//Acciones
								Action ac = (Action) cartas.get(i).getObject().newInstance();
								acs.add(ac);
								i++;
							}
							teamCharacters.add(new Character(p.getTeams().size(), cr, acs));
						}
						
						p.addTeam("",teamCharacters);
						respuesta.put("Acceso", "Ingresaste a la partida.");
					} catch (Exception e) {
						e.printStackTrace();
						respuesta.put("Error", "Equipo mal formado.");						
					}
				} else {
					respuesta.put("Error", "Pasaste la esencia maxima.");
				}
				
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
			Match partida = this.partidas.get(id);
			if(partida.getTeams().size() > 1)
				partida.start();
			else
				respuesta.put("Error", "Hace falta por lo menos 2 equipos para iniciar la partida.");				
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
			Match partida = this.partidas.get(id);
			CombatSystem sistema = partida.getSystem();

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

	
	@GetMapping("{id}")
	public Map<String, Object> informacionPartida(@PathVariable Integer id) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.partidas.containsKey(id)) {
			Match partida = this.partidas.get(id);
			if(partida.getState().equals("WAITING")) {
				respuesta.put("Esencia", partida.getEssence());
				respuesta.put("Equipos", partida.getTeams().size());
				respuesta.put("Cartas disponibles", partida.getCards());
			} else if (partida.getState().equals("PLAYING")) {
				Map<String, Object> estadoEquipos = new LinkedHashMap<>();
				partida.getSystem().getTeams().stream().forEach(t -> {
					Map<String, Object> estadoPersonajes = new LinkedHashMap<>();
					t.getCharacters().forEach(c -> {
						Map<String, Object> estadoPersonaje = new LinkedHashMap<>();
						estadoPersonaje.put("HP", c.getHp());
						estadoPersonaje.put("FATIGUE", c.getFatigue());
						
						estadoPersonajes.put(c.getName(), estadoPersonaje);
					});
					estadoEquipos.put("Equipo "+t.getTeamNumber(), estadoPersonajes);
				});
				respuesta.put("Estado", estadoEquipos);
				respuesta.put("Mensajes", partida.getSystem().getMessages());
			}
		} else {
			respuesta.put("Error", "La partida no existe.");
		}
		
		return respuesta;
	}
	
	
}
