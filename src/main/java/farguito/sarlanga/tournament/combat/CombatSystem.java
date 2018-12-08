package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import farguito.sarlanga.tournament.cards.Action;

public class CombatSystem {

	//se podria pasar a un map<String,List<Character>> en donde definan los equipos, se le podria meter banderas como "jugando" por si ya perdio o no
	//pero me parece que lo voy a hacer despues, porque ahora es complicarmela (serviria para poder hacer partidas de mas de 2 jugadores)
	List<Team> teams; // personajes en combate
	
	State state; // estado del combate
	Character activeCharacter; // personaje activo (turno)
	List<Effect> activeEffects; // efectos activos

	/**
	 * 1- Antes de que arranque la partida se define la esencia disponible para los jugadores
	 * 2- Los jugadores arman su grupo compuesto de criaturas con acciones
	 * 3- Al iniciar la partida, arranca la criatura con mayor velocidad, en caso de que criaturas de ambos jugadores
	 *    empaten, se tira una moneda.
	 * 4- La criatura realiza una accion y pasa su turno (la accion genera cansancio).
	 * 5- Sigue la criatura que no tenga cansancio y tenga mayor velocidad. En caso de que criaturas de ambos jugadores 
	 *    empaten, toma el turno la criatura del jugador que no jugo en el turno anterior
	 * 6- Si un jugador queda sin criaturas vivas, pierde.
	 */
	public CombatSystem(List<Team> teams) {
		this.teams = teams;
		
		boolean playing = true;

		firstTurn();
		
		while (playing) {
			switch (state) { //se puede poner un patron state... : ) CACA CACONA.
			case CHECKING_PLAYERS:
				checkPlayers();
				break;
			case CHECKING_READY:
				checkReady();
				break;
			case ADVANCING_TURNS:
				advancingTurns();
				break;
			default:
				break;
			}

		}
	}

	//4- La criatura realiza una accion y pasa su turno (la accion genera cansancio).
	public void action(Action action) {
		
		this.state = State.ADVANCING_TURNS;
	}

	//5- Sigue la criatura que no tenga cansancio y tenga mayor velocidad. En caso de que criaturas de ambos jugadores 
	//   empaten, toma el turno la criatura del jugador que no jugo en el turno anterior
	private void checkReady() {
		List<Character> readyCharacters = new ArrayList<>();
		//agarro todas las criaturas listas
		teams.stream().forEach(t -> {
			t.getCharacters().stream().forEach(c -> {
				if(c.isReady()) {
					readyCharacters.add(c);
				}
			});
		});
		
		//si hay mas de una criatura lista
		if(readyCharacters.size() > 1) {

			//tomo las mas rapidas
			readyCharacters.sort(Comparator.comparing(Character::getSpeed));
					
			int fastestSpeed = readyCharacters.get(0).getSpeed();
			boolean search = true;
			int amount = 1;
			while(search && amount < readyCharacters.size()) {
				if(readyCharacters.get(amount).getSpeed() < fastestSpeed) {
					search = false;
					amount--; //el anterior fue el ultimo mas rapido
				} else {
					amount++;
				}
			}
			
			//si hay mas de una criatura con velocidad maxima
			if(amount > 1) {
				//si hay de equipos diferentes al ultimo que jugo
				int j = 0;
				boolean found = false;
				while(!found && j < amount) {
					if (readyCharacters.get(j).getTeam() != activeCharacter.getTeam())
						found = true;
				}	
				
				if(found) {
					activeCharacter = readyCharacters.get(j);						
				} else {
					activeCharacter = readyCharacters.get(0);				
				}
			} else {
				activeCharacter = readyCharacters.get(0);				
			}
			
		} else if(readyCharacters.size() == 1) {
			activeCharacter = readyCharacters.get(0);
		}
		
	}
	


	//3- Al iniciar la partida, arranca la criatura con mayor velocidad, en caso de que criaturas de ambos jugadores
	//    empaten, se tira una moneda.
	private void firstTurn() {		
		List<Character> fastestCharacters = new ArrayList<>();
		
		teams.stream().forEach(team -> {
			fastestCharacters.add(team.fastestCharacter());
		});
		
		fastestCharacters.sort(Comparator.comparing(Character::getSpeed));
				
		int fastestSpeed = fastestCharacters.get(0).getSpeed();
		boolean search = true;
		int amount = 1;
		while(search && amount < fastestCharacters.size()) {
			if(fastestCharacters.get(amount).getSpeed() < fastestSpeed) {
				search = false;
				amount--; //el anterior fue el ultimo mas rapido, y pasa a ser el index
			} else {
				amount++;
			}
		}
		
		//si hay mas de 1 rapido max, se decide random
		if(amount > 0) {
			Random r = new Random(System.currentTimeMillis());
			amount = r.nextInt(amount);
		}
		
		activeCharacter = fastestCharacters.get(amount);		
	}
	
	
	/**
	 * Avanzo los turnos: 
	 * + Criaturas se restan cansancio (- velocidad) 
	 * + Efectos duraderos aumentan cronicidad 
	 * + Efectos constantes restan duracion
	 */
	private void advancingTurns() {
		//para no comparar en cada iteracion voy a hacer una chanchada
		//en vez de ver si cada criatura es la activa, voy a restarle el cansancio a la activa y se lo voy a re-sumar
		teams.stream().forEach(t -> {
			t.getCharacters().stream().forEach(c -> c.rest());			
		});
		
		activeCharacter.fatigate(activeCharacter.getSpeed());
		activeCharacter = null;
		
		/*
		 * + Efectos duraderos aumentan cronicidad 
		 * + Efectos constantes restan duracion
		 */
	}

	// 6- Si solo queda un jugador con criaturas vivas, gana. (se revierte)
	private void checkPlayers() {
		List<Team> aliveTeams = teams.stream().filter(t -> t.someoneAlive()).collect(Collectors.toList());
		//esto esta pensado para FFA
		if(aliveTeams.size() == 1) {
			this.state = State.TEAM_VICTORY;
			Team winnerTeam = aliveTeams.get(0); //hacer algo con el ganador
		}
	}

}
