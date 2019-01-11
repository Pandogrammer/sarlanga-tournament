package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.combat.CombatSystem;

@RestController
@RequestMapping("v2")
public class CombatControllerV2 {

	private CardFactory cards = new CardFactory();
	private Map<String, Match> matchs = new HashMap<>();
	private Map<String, String> user_match = new HashMap<>();
	
	@GetMapping
	public Map<String, Object> information() {
		Map<String, Object> response = new LinkedHashMap<>();

		int i = 0;
		response.put(""+i, "Hola :D ! Bienvenido a la BETA de Sarlanga Tournament"); i++;

		response.put(""+i, ""); i++;
		response.put(""+i, "Para generar tu cuenta anda a /generate-account"); i++;
		response.put(""+i, "Esto te retornara un id con el cual utilizaras todos los comandos posibles"); i++;
		response.put(""+i, "Ej: /{accountId}/create-room"); i++;

		response.put(""+i, ""); i++;
		response.put(""+i, "Listado de comandos posibles:"); i++;
		response.put(""+i, "/{accountId}/create-room?essence={amount}"); i++;
		response.put(""+i, "/{accountId}/delete-room"); i++;
		response.put(""+i, "/{accountId}/start-match"); i++;
		response.put(""+i, "/rooms | /rooms?id={roomId}"); i++;
		
		
		return response;
	}
	
	
	@GetMapping("generate-account")
	public Map<String, Object> generateAccount() {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		String accountId = UUID.randomUUID().toString().replaceAll("-", "");
		
		respuesta.put("Account ID", accountId);
		return respuesta;
	}
	

	@GetMapping("{accountId}/create-room")
	public Map<String, Object> createRoom(@PathVariable String accountId
										, @RequestParam int essence) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(!this.user_match.containsKey(accountId)) {
			Match match = new Match(essence, cards.getCards());
			match.setOwner(accountId);
			String roomId = ""+(this.matchs.size()+1);
			this.matchs.put(roomId, match);
			this.user_match.put(accountId, roomId);
			
			respuesta.put("exito", "Sala creada.");
			respuesta.put("id", roomId);
		} else {
			respuesta.put("error", "Ya tenes una sala creada.");
		}
		
		return respuesta;
	}

	@GetMapping("{accountId}/delete-room")
	public Map<String, Object> deleteRoom(@PathVariable String accountId) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.user_match.containsKey(accountId)) {
			String roomId = this.user_match.get(accountId);
			if(this.matchs.get(roomId).getOwner().equals(accountId)){
				this.matchs.remove(roomId);	
				this.user_match.remove(accountId);		
				
				respuesta.put("exito", "Sala eliminada.");
			} else {
				respuesta.put("error", "No sos el dueño de la sala.");				
			}
		} else {
			respuesta.put("error", "La sala no existe.");
		}
		
		return respuesta;
	}

	
	@GetMapping("{accountId}/start-match")
	public Map<String, Object> startMatch(@PathVariable String accountId) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.user_match.containsKey(accountId)) {
			String roomId = this.user_match.get(accountId);
			if(this.matchs.get(roomId).getTeams().size() > 1) {
				this.matchs.get(roomId).start();
			} else {
				respuesta.put("error", "Hace falta por lo menos 2 equipos para iniciar la partida.");
			}
		} else {
			respuesta.put("error", "No tenes una sala creada.");
		}
		
		return respuesta;
	}
	
	

	
	@GetMapping("{accountId}/enter-room")
	public Map<String, Object> enterRoom(@PathVariable String accountId
									   , @RequestParam("id") String roomId) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		/*
		if(this.matchs.containsKey(roomId)) {
			String roomId = this.user_match.get(accountId);
			if(this.matchs.get(roomId).getOwner().equals(accountId)){
				if(this.matchs.get(roomId).getTeams().size() > 1) {
					this.matchs.get(roomId).start();
				} else {
					respuesta.put("Error", "Hace falta por lo menos 2 equipos para iniciar la partida.");
				}
			} else {
				respuesta.put("error", "No sos el dueño de la sala.");				
			}
		} else {
			respuesta.put("error", "La sala no existe.");
		}
		*/
		return respuesta;
	}
	

	
	@GetMapping("/rooms")
	public Map<String, Object> roomInformation(@RequestParam(required = false, value = "id") String roomId) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		if(roomId != null) {					
			if(this.matchs.containsKey(roomId)) {
				Match partida = this.matchs.get(roomId);
				if(partida.getState().equals("WAITING")) {
					respuesta.put("esencia", partida.getEssence());
					respuesta.put("equipos", partida.getTeams().size());
					respuesta.put("cartas disponibles", partida.getCards());
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
						estadoEquipos.put("equipo "+t.getTeamNumber(), estadoPersonajes);
					});
					respuesta.put("estado", estadoEquipos);
					respuesta.put("mensajes", partida.getSystem().getMessages());
				}
			} else {
				respuesta.put("error", "La sala no existe.");
			}
		} else {

			List<Map<String, Object>> rooms = new ArrayList<>();			
			this.matchs.entrySet().stream().forEach(m -> {
				Map<String, Object> roomInformation = new LinkedHashMap<>();
				roomInformation.put("id", m.getKey());
				roomInformation.put("essence", m.getValue().getEssence());
				roomInformation.put("teams", m.getValue().getTeams().size());
				roomInformation.put("state", m.getValue().getState());

				rooms.add(roomInformation);
			});			
			respuesta.put("salas", rooms);
		}
		
		return respuesta;
	}
	


	@GetMapping("{accountId}/{roomId}/accion/{ac}:{team}:{obj}")
	public Map<String, Object> accion(@PathVariable Integer id
			  						  , @PathVariable Integer ac
			  						  , @PathVariable Integer team
									  , @PathVariable Integer obj) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		if(this.matchs.containsKey(id)) {
			Match partida = this.matchs.get(id);
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

	
}
