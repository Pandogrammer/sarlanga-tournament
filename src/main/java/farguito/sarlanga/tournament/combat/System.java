package farguito.sarlanga.tournament.combat;

import java.util.List;

public class System {

	List<Character> teamOne; // personajes en combate
	List<Character> teamTwo; // personajes en combate
	List<Effect> effects; // efectos activos
	State state; // estado del combate
	Character activeCharacter; // personaje activo (turno)

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
	public System(List<Character> teamOne, List<Character> teamTwo) {
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		
		boolean playing = true;

		while (playing) {
			switch (state) {
			case ADVANCING_TURNS:
				checkPlayers();
				advancingTurns();
				break;
			default:
				break;
			}

		}
	}

	/**
	 * Avanzo los turnos: 
	 * + Criaturas se restan cansancio (- velocidad) 
	 * + Efectos duraderos aumentan cronicidad 
	 * + Efectos constantes restan duracion
	 */
	private void advancingTurns() {

	}

	// 6- Si un jugador queda sin criaturas vivas, pierde.
	private void checkPlayers() {
		teamOne.stream().any
	}

}
