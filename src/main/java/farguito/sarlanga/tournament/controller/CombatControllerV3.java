package farguito.sarlanga.tournament.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import farguito.sarlanga.tournament.SarlangaTournamentApplication;
import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.cards.ActionCard;
import farguito.sarlanga.tournament.cards.Card;
import farguito.sarlanga.tournament.cards.CardFactory;
import farguito.sarlanga.tournament.cards.CriatureCard;
import farguito.sarlanga.tournament.combat.Character;
import farguito.sarlanga.tournament.combat.CombatSystem;
import farguito.sarlanga.tournament.connection.CharacterDTO;
import farguito.sarlanga.tournament.connection.Match;
import farguito.sarlanga.tournament.connection.TeamDTO;

@RestController
@RequestMapping("v3")
public class CombatControllerV3 {

	private String controllerUri = SarlangaTournamentApplication.URI+"v3/";
	
	private CardFactory cards = new CardFactory();
	private Map<String, Match> matchs = new HashMap<>();
	private Map<String, String> accountOwner_match = new HashMap<>();
	private Map<String, String> session_account = new HashMap<>();
	private Map<String, String> account_room = new HashMap<>();
	
	@GetMapping
	public Map<String, Object> information() {
		Map<String, Object> response = new LinkedHashMap<>();

		int i = 0;
		response.put(""+i, "Hola :D ! Bienvenido a la BETA de Sarlanga Tournament"); i++;
		
		return response;
	}
	
	
	@GetMapping("account")
	public Map<String, Object> account(HttpServletRequest request
								     , @RequestParam(value = "id", required = false) String accountId) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();
		
		if(accountId != null) {
			if(this.session_account.containsValue(accountId)) {
				this.session_account.put(sessionId, accountId);
				respuesta.put("Mensaje", "Reconectado!");
			} else {
				respuesta.put("Error", "La cuenta no existe.");
			}
		} else {
			if(this.session_account.containsKey(sessionId)) {	
				respuesta.put("Account ID", this.session_account.get(sessionId));
			} else {
				String newAccountId = UUID.randomUUID().toString().replaceAll("-", "");
				this.session_account.put(request.getSession().getId(), newAccountId);
				respuesta.put("Account ID", newAccountId);
			}
		}
		return respuesta;
	}
	

	@GetMapping("rooms/create")
	public Map<String, Object> createRoom(HttpServletRequest request
										, @RequestParam int essence) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		
		String accountId = this.session_account.get(sessionId);
		
		if(!this.accountOwner_match.containsKey(accountId)) {
			Match match = new Match(essence, cards.getCards());
			match.setOwner(accountId);
			match.addPlayer(accountId);
			String roomId = ""+(this.matchs.size()+1);
			this.matchs.put(roomId, match);
			this.accountOwner_match.put(accountId, roomId);
			this.account_room.put(accountId, roomId);
			
			respuesta.put("exito", "Sala creada.");
			respuesta.put("id", roomId);
		} else {
			respuesta.put("error", "Ya tenes una sala creada.");
			respuesta.put("id", this.accountOwner_match.get(accountId));
		}
		
		return respuesta;
	}

	@GetMapping("rooms/delete")
	public Map<String, Object> deleteRoom(HttpServletRequest request) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		
		String accountId = this.session_account.get(sessionId);
		
		if(this.accountOwner_match.containsKey(accountId)) {
			String roomId = this.accountOwner_match.get(accountId);
			if(this.matchs.get(roomId).getOwner().equals(accountId)){
				this.matchs.remove(roomId);	
				this.accountOwner_match.remove(accountId);
				
				respuesta.put("exito", "Sala eliminada.");
			}
		} else {
			respuesta.put("error", "No tenes una sala creada.");
		}
		
		return respuesta;
	}

	
	@GetMapping("rooms/start")
	public Map<String, Object> startMatch(HttpServletRequest request) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		
		String accountId = this.session_account.get(sessionId);
		
		if(!this.accountOwner_match.containsKey(accountId)) {
			respuesta.put("error", "No tenes una sala creada.");
		} else {
			String roomId = this.accountOwner_match.get(accountId);
			if(this.matchs.get(roomId).getTeams().size() < 2) {
				respuesta.put("error", "Hace falta por lo menos 2 equipos para iniciar la partida.");
			} else {
				this.matchs.get(roomId).start();
				respuesta.put("mensaje", "Partida iniciada.");
			}
		}
		
		return respuesta;
	}
	
	


	@GetMapping("rooms/{roomId}/enter")
	public Map<String, Object> enterRoom(HttpServletRequest request
									   , @PathVariable String roomId) {
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);

		if(this.accountOwner_match.containsKey(accountId)) {
			respuesta.put("error", "No podes entrar a una sala si tenés una creada.");			
		} else if(!this.matchs.containsKey(roomId)) {
			respuesta.put("error", "La sala no existe.");
		} else {
			Match partida = this.matchs.get(roomId);
			if(!partida.getState().equals("WAITING")) { 
				respuesta.put("error", "No podes entrar en una partida que ya inició.");
			} else {
				partida.addPlayer(accountId);
				this.account_room.put(accountId, roomId);
				respuesta.put("mensaje", "Ingresaste a la partida.");
			}
		}
		
		return respuesta;
	}

	@GetMapping("match")
	public Map<String, Object> match(HttpServletRequest request){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		

		if(!this.account_room.containsKey(accountId)) {
			respuesta.put("error", "No estas en ninguna partida.");			
		} else {
			String roomId = this.account_room.get(accountId);
			Match match = this.matchs.get(roomId);
			if(!match.getState().equals("PLAYING")) {
				respuesta.put("error", "La partida todavia no inició.");
			} else if (match.getState().equals("PLAYING")) {
				CombatSystem sistema = match.getSystem();
				Map<String, Object> estadoEquipos = new LinkedHashMap<>();
				sistema.getTeams().stream().forEach(t -> {
					Map<String, Object> estadoPersonajes = new LinkedHashMap<>();
					List<Character> personajes = t.getCharacters();
					for(int i = 0; i < personajes.size(); i++){
						Character c = personajes.get(i);
						Map<String, Object> estadoPersonaje = new LinkedHashMap<>();
						estadoPersonaje.put("NOMBRE", c.getName());
						estadoPersonaje.put("HP", c.getHp());
						estadoPersonaje.put("FATIGUE", c.getFatigue());
						
						estadoPersonajes.put(t.getNumber()+""+(i+1), estadoPersonaje);
					}
					estadoEquipos.put("equipo "+t.getNumber(), estadoPersonajes);
				});
				if(sistema.getWinningTeam() != -1) {
					respuesta.put("turno", "::: Victoria del equipo "+sistema.getWinningTeam()+" :::");
				} else {
					Character character = sistema.getActiveCharacter();
					if(character != null) {
						if(match.getTeamNumber(accountId) != character.getTeam()) {
							respuesta.put("turno", "Es el turno del equipo "+character.getTeam());
						} else {
							respuesta.put("turno", "Es tu turno. Equipo "+character.getTeam()+" - "+character.getName());
							Map<String, Object> accionesPosibles = new LinkedHashMap<>();
							List<Action> acciones = character.getActions();
							for(int i = 0; i < acciones.size(); i++) {
								accionesPosibles.put(""+i, acciones.get(i));
							}
							respuesta.put("acciones", accionesPosibles);
						}
					}
				}
				respuesta.put("estado", estadoEquipos);
				Map<Integer, String> mensajes = new LinkedHashMap<>();			
				for(int i = 0; i < match.getSystem().getMessages().size(); i++) {
					mensajes.put(i, match.getSystem().getMessages().get(i));
				}
				respuesta.put("mensajes", mensajes);
			}
		}
		
		return respuesta;
	}	



	@GetMapping("match/action/{actionId}/{objectiveId}")
	public Map<String, Object> matchExecuteAction(HttpServletRequest request												
												, @PathVariable String actionId
												, @PathVariable String objectiveId){
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		

		if(!this.account_room.containsKey(accountId)) {
			respuesta.put("error", "No estas en ninguna partida.");			
		} else {
			String roomId = this.account_room.get(accountId);
			Match match = this.matchs.get(roomId);
			if(!match.getState().equals("PLAYING")) {
				respuesta.put("error", "La partida todavia no inició.");
			} else if (match.getState().equals("PLAYING")) {				
				CombatSystem sistema = match.getSystem();
				if(match.getTeamNumber(accountId) == sistema.getActiveCharacter().getTeam()) {
					try {
						int team = java.lang.Character.getNumericValue(objectiveId.charAt(0));
						int obj = java.lang.Character.getNumericValue(objectiveId.charAt(1));
						int ac = Integer.parseInt(actionId);
						Action action = sistema.getActiveCharacter().getActions().get(ac);
						sistema.prepareAction(
								action, sistema.getTeams().get(team-1).getCharacters().get(obj-1));
						if(!sistema.validateObjectives(action))
							respuesta.put("mensaje", "Target incorrecto.");
						else { 
							sistema.executeAction(action);
							sistema.nextTurn();
						}
						
						respuesta.put("mensaje", "Accion ejecutada.");
					} catch (Exception e) {
						respuesta.put("error", "La accion no se pudo ejecutar.");
					}
					
				}
					
			}
		}
		
		return respuesta;
	}
	


	@GetMapping("match/status")
	public Map<String, Object> matchStatus(HttpServletRequest request){
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		

		if(!this.account_room.containsKey(accountId)) {
			respuesta.put("error", "No estas en ninguna partida.");			
		} else {
			String roomId = this.account_room.get(accountId);
			Match match = this.matchs.get(roomId);
			if(!match.getState().equals("PLAYING")) {
				respuesta.put("error", "La partida todavia no inició.");
			} else if (match.getState().equals("PLAYING")) {	
				CombatSystem sistema = match.getSystem();
				Map<String, Object> estadoEquipos = new LinkedHashMap<>();
				sistema.getTeams().stream().forEach(t -> {
					Map<String, Object> estadoPersonajes = new LinkedHashMap<>();
					List<Character> personajes = t.getCharacters();
					for(int i = 0; i < personajes.size(); i++){
						Character c = personajes.get(i);
						Map<String, Object> estadoPersonaje = new LinkedHashMap<>();
						estadoPersonaje.put("NOMBRE", c.getName());
						estadoPersonaje.put("HP", c.getHp());
						estadoPersonaje.put("FATIGUE", c.getFatigue());
						
						estadoPersonajes.put(t.getNumber()+""+(i+1), estadoPersonaje);
					}
					estadoEquipos.put("equipo "+t.getNumber(), estadoPersonajes);
				});
				if(sistema.getWinningTeam() != -1) {
					respuesta.put("turno", "::: Victoria del equipo "+sistema.getWinningTeam()+" :::");
				} else {
					Character character = sistema.getActiveCharacter();
					if(character != null) {
						if(match.getTeamNumber(accountId) != character.getTeam()) {
							respuesta.put("turno", "Es el turno del equipo "+character.getTeam());
						} else {
							respuesta.put("turno", "Es tu turno. Equipo "+character.getTeam()+" - "+character.getName());
							Map<String, Object> accionesPosibles = new LinkedHashMap<>();
							List<Action> acciones = character.getActions();
							for(int i = 0; i < acciones.size(); i++) {
								accionesPosibles.put(""+i, acciones.get(i));
							}
							respuesta.put("acciones", accionesPosibles);
						}
					}
				}
				respuesta.put("estado", estadoEquipos);					
			}
		}
		
		return respuesta;
	}

	@GetMapping("match/messages")
	public Map<String, Object> matchMessages(HttpServletRequest request){
		Map<String, Object> respuesta = new LinkedHashMap<>();
		
		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		

		if(!this.account_room.containsKey(accountId)) {
			respuesta.put("error", "No estas en ninguna partida.");			
		} else {
			String roomId = this.account_room.get(accountId);
			Match match = this.matchs.get(roomId);
			if(!match.getState().equals("PLAYING")) {
				respuesta.put("error", "La partida todavia no inició.");
			} else if (match.getState().equals("PLAYING")) {	
				Map<Integer, String> mensajes = new LinkedHashMap<>();			
				for(int i = 0; i < match.getSystem().getMessages().size(); i++) {
					mensajes.put(i, match.getSystem().getMessages().get(i));
				}
				respuesta.put("mensajes", mensajes);					
			}
		}
		
		return respuesta;
	}
	
	
	@GetMapping("team")
	public Map<String, Object> team(HttpServletRequest request){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		

		if(!this.account_room.containsKey(accountId)) {
			respuesta.put("error", "No estas en ninguna partida.");			
		} else {
			String roomId = this.account_room.get(accountId);
			TeamDTO team = this.matchs.get(roomId).getTeamDTO(accountId);
			
			respuesta.put("team", team);
		}
		return respuesta;
	}

	@GetMapping("team/confirm")
	public Map<String, Object> teamReady(HttpServletRequest request){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);

		if(!this.account_room.containsKey(accountId)) {
			respuesta.put("error", "No estas en ninguna partida.");			
		} else {
			String roomId = this.account_room.get(accountId);
			Match match = this.matchs.get(roomId);
			TeamDTO team = match.getTeamDTO(accountId);
			
			if(team.getEssence() > match.getEssence()) {
				respuesta.put("error-1", "El equipo supera la cantidad de esencia maxima");
				respuesta.put("error-2", "Equipo: "+team.getEssence()+" | Partida: "+match.getEssence());			
			} else if (team.getCharacters().isEmpty()) {
				respuesta.put("error", "El equipo no tiene personajes.");
			} else if (!team.validateCharacters()) {
				respuesta.put("error", "Una criatura no tiene acciones.");			
			} else {
				match.addTeam(accountId, team.create());
				respuesta.put("mensaje", "Equipo confirmado.");
			}
		}
		
		return respuesta;
	}

	@GetMapping("team/add/{cardId}/{line}/{position}")
	public Map<String, Object> teamAddCharacter(HttpServletRequest request
											  , @PathVariable Integer cardId
											  , @PathVariable Integer line
											  , @PathVariable Integer position){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		
		String roomId = this.account_room.get(accountId);
		Match match = this.matchs.get(roomId);
		TeamDTO team = match.getTeamDTO(accountId);
		
		if(!match.getCards().containsKey(cardId)) {
			respuesta.put("error", "La carta no existe.");
		} else {
			Card card = match.getCards().get(cardId); 
			if(!(card instanceof CriatureCard)) {
				respuesta.put("error", "La carta no es una criatura");
			} else if (!team.validatePosition(line, position)){
				respuesta.put("error", "La posicion ya esta ocupada");				
			} else {
				team.addCharacter(line, position, card);
			}
		}
		
		respuesta.put("team", team);
		return respuesta;
	}

	@GetMapping("team/remove/{characterId}")
	public Map<String, Object> teamRemoveCharacter(HttpServletRequest request
										   , @PathVariable Integer characterId){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		
		String roomId = this.account_room.get(accountId);
		Match match = this.matchs.get(roomId);
		TeamDTO team = match.getTeamDTO(accountId);
		
		team.removeCharacter(characterId);		
		
		respuesta.put("team", team);
		return respuesta;		
	}
	
	
	@GetMapping("team/{method}/{characterId}/{cardId}")
	public Map<String, Object> teamModifyCharacterAction(HttpServletRequest request
										   , @PathVariable String method
										   , @PathVariable Integer characterId
										   , @PathVariable Integer cardId){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		String sessionId = request.getSession().getId();		
		if(!this.session_account.containsKey(sessionId))
			return notLoggedError(); 
		String accountId = this.session_account.get(sessionId);
		
		String roomId = this.account_room.get(accountId);
		Match match = this.matchs.get(roomId);
		TeamDTO team = match.getTeamDTO(accountId);
		if(!team.getCharacters().containsKey(characterId)) {
			respuesta.put("error", "El personaje no existe.");						
		} else if(!match.getCards().containsKey(cardId)) {
			respuesta.put("error", "La carta no existe.");
		} else {
			CharacterDTO character = team.getCharacter(characterId);
			Card card = match.getCards().get(cardId); 
			if(method.equalsIgnoreCase("add")) {
				if(!(card instanceof ActionCard)) {
					respuesta.put("error", "La carta no es una acción.");
				} else {
					if(character.containsAction(card)) {
						respuesta.put("error", "El personaje ya posee esa acción.");					
					} else {
						character.addAction(card);
					}
				}
			} else if(method.equalsIgnoreCase("remove")) {
				character.removeAction(card);				
			}
		}
		
		respuesta.put("team", team);
		return respuesta;		
	}

	
	@GetMapping("rooms")
	public Map<String, Object> roomsInformation() {
		Map<String, Object> respuesta = new LinkedHashMap<>();

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
		
		return respuesta;
	}
	
	
	@GetMapping("rooms/{roomId}")
	public Map<String, Object> roomInformation(@PathVariable String roomId) {
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
						estadoEquipos.put("equipo "+t.getNumber(), estadoPersonajes);
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
	
	
	
	
	private Map<String, Object> notLoggedError(){
		Map<String, Object> respuesta = new LinkedHashMap<>();

		respuesta.put("1", "No estás logueado, anda a:");
		respuesta.put("2", controllerUri+"account");
		respuesta.put("3", "si te querés reconectar usá:");
		respuesta.put("4", controllerUri+"account?id=");
		
		return  respuesta;
	}
	
}
