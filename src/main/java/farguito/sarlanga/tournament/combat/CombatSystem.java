package farguito.sarlanga.tournament.combat;

import java.util.List;

public class CombatSystem {

	List<CombatCharacter> characters; //personajes en combate
	List<Effect> effects; //efectos activos
	State state; //estado del combate
	CombatCharacter activeCharacter; //personaje activo (turno)	
	
	public CombatSystem (List<CombatCharacter> characters) {
		boolean playing = true;
		
		while(playing) {
			switch(state) {
			case ADVANCING_TURNS: advancingTurns(); break; 
			default: break;
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
	
	
}
