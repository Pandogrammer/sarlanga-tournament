package farguito.sarlanga.tournament.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import farguito.sarlanga.tournament.cards.Action;
import farguito.sarlanga.tournament.combat.effects.Effect;
import farguito.sarlanga.tournament.combat.effects.ImmediateEffect;
import farguito.sarlanga.tournament.combat.effects.LastingEffect;

public class CombatSystem {
	
	private List<Team> teams; // personajes en combate
	
	private Character activeCharacter; // personaje activo (turno)
	
	private int lastTeamTurn;
	private int winningTeam = -1;
	
	private Logger logger = new Logger();
	private EffectContainer effectContainer = new EffectContainer();
	private EventListener eventListener = new EventListener();
	

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

		firstTurn();
	}
	
	//test
	public void addEffectModifier(ImmediateEffectModifier iem) {
		eventListener.addImmediateEffectModifier(iem);
	}
	
	public void applyImmediateEffects() {		
		effectContainer.getImmediateEffects().stream().forEach(e -> executeImmediateEffect(e));
		
		effectContainer.purgeEffects();
	}
	

	public void prepareAction(Action action, Character objective) {
		List<Character> objectives = new ArrayList<>();
		objectives.add(objective);
		
		
		this.prepareAction(action, objectives);
	}
	
	//chequeo previo y definicion de objetivos
	public void prepareAction(Action action, List<Character> objectives) {
		//se le pasan los parametros iniciales de la criatura que genera la accion
		//se definen los objetivos
		//se chequean que sean validos (ataque melee, etc)
		action.setActor(activeCharacter);
		action.setObjectives(objectives);
		//action.validate();
	}
	
	//4- La criatura realiza una accion y pasa su turno (la accion genera cansancio).
	//el chequeo del objetivo en que momento se hace?
	public void executeAction(Action action) {
		logger.log("Executing action: "+action.getClass().getSimpleName());
		logger.log(action.message());
		/*
		//habilidades y efectos constantes la modifican
		List<Modifier> actionModifiers = eventListener.getActionGenerationModifiers();
		//se aplica esa modificacion
		actionModifiers.stream().forEach(am -> {
			am.apply(action);
		});		
		*/
		//se ejecuta la accion, se generan efectos
		List<Effect> effects = action.execute();
		
		effectContainer.addEffects(effects);
		
		//pobreza intensifies
		lastTeamTurn = action.getActor().getTeam();
		action.getActor().fatigate(action.getFatigue());
	}
	
	
	/* Se ejecuta un efecto:
	 * accion: parametros iniciales
	 * campo: efectos constantes
	 * destino: habilidades, atributos
	 * resultado: x
	 */
	public void executeImmediateEffect(ImmediateEffect effect) {
		logger.log("Executing immediate effect: "+effect.getClass().getSimpleName());
		/* codigo repetido con generacion de accion.. podria ser algo dentro del eventListener*/
		/* List<Modifier> effectModifiers = eventListener.getEffectExecutionModifiers();
		
		effectModifiers.stream().forEach(em -> {
			em.apply(effect);
		});		
		*/
		eventListener.modifyImmediateEffect(effect);
		logger.log(effect.message());
		effect.execute();
	}
	
	public void executeLastingEffect(LastingEffect effect) {
		logger.log("Executing lasting effect.");
		
		List<Effect> effects = effect.execute();
		
		effectContainer.addEffects(effects);
	}
	
	public void checkLastingEffectReady() {		
		effectContainer.getLastingEffects().stream().forEach(e -> {
			if(e.isReady()) executeLastingEffect(e);
		});
	}
	
	//5- Sigue la criatura que no tenga cansancio y tenga mayor velocidad. En caso de que criaturas de ambos jugadores 
	//   empaten, toma el turno la criatura del jugador que no jugo en el turno anterior
	public void checkCharacterReady() {		
		List<Character> readyCharacters = new ArrayList<>();
		//agarro todas las criaturas listas (que esten vivas)
		teams.stream().forEach(t -> {
			t.getCharacters().stream().forEach(c -> {
				if(c.isReady() && c.isAlive()) {
					readyCharacters.add(c);
				}
			});
		});
		
		//si hay mas de una criatura lista
		if(readyCharacters.size() > 1) {

			//tomo las mas rapidas
			readyCharacters.sort(Comparator.comparing(Character::getSpeed).reversed());
					
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
					if (readyCharacters.get(j).getTeam() != lastTeamTurn)
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

		if(activeCharacter != null)
			logger.log(activeCharacter.getName()+" from Team "+activeCharacter.getTeam()+" is ready.");		
	}
	


	//3- Al iniciar la partida, arranca la criatura con mayor velocidad, en caso de que criaturas de ambos jugadores
	//    empaten, se tira una moneda.
	public void firstTurn() {		
		List<Character> fastestCharacters = new ArrayList<>();
		
		teams.stream().forEach(team -> {
			fastestCharacters.add(team.fastestCharacter());
		});
		
		fastestCharacters.sort(Comparator.comparing(Character::getSpeed).reversed());
				
		int fastestSpeed = fastestCharacters.get(0).getSpeed();
		boolean search = true;
		int amount = 1;
		while(search && amount < fastestCharacters.size()) {
			if(fastestCharacters.get(amount).getSpeed() < fastestSpeed) {
				search = false;
				amount--; //el anterior fue el ultimo mas rapido, y amount pasa a ser el index
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
		
		logger.log(activeCharacter.getName()+" from Team "+activeCharacter.getTeam()+" is ready.");
	}
	
	
	/**
	 * Avanzo los turnos: 
	 * + Criaturas se restan cansancio (- velocidad) 
	 * + Efectos duraderos aumentan cronicidad 
	 * + Efectos constantes restan duracion
	 */
	public void advancingTurns() {
		logger.log("Advancing turns.");
		
		teams.stream().forEach(t -> {
			t.getCharacters().stream().forEach(c -> c.rest());			
		});
		
		/*
		 * + Efectos duraderos aumentan cronicidad 
		 * + Efectos constantes restan duracion
		 */
		effectContainer.getLastingEffects().forEach(e -> e.addCooldown());
		effectContainer.getConstantEffects().forEach(e -> e.removeDuration());
		
	}

	// 6- Si solo queda un jugador con criaturas vivas, gana. (se revierte)
	public void checkPlayers() {
		List<Team> aliveTeams = teams.stream().filter(t -> t.someoneAlive()).collect(Collectors.toList());
		//esto esta pensado para FFA
		if(aliveTeams.size() == 1) {
			this.winningTeam = aliveTeams.get(0).getTeamNumber(); //hacer algo con el ganador			
		}
	}

	
	
	public int getWinningTeam() {
		return winningTeam;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void removeActiveCharacter() {
		this.activeCharacter = null;
	}
	public Character getActiveCharacter() {
		return activeCharacter;
	}
	
	public List<String> getMessages() {
		return this.logger.getMessages();
	}

	public EffectContainer getEffectContainer() {
		return effectContainer;
	}
	
	
	

}
