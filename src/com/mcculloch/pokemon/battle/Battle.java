package com.mcculloch.pokemon.battle;

import com.mcculloch.pokemon.battle.animation.FaintingAnimation;
import com.mcculloch.pokemon.battle.animation.PokeballAnimation;
import com.mcculloch.pokemon.battle.event.AnimationBattleEvent;
import com.mcculloch.pokemon.battle.event.BattleEvent;
import com.mcculloch.pokemon.battle.event.BattleEventQueuer;
import com.mcculloch.pokemon.battle.event.BattleEventPlayer;
import com.mcculloch.pokemon.battle.event.HPAnimationEvent;
import com.mcculloch.pokemon.battle.event.NameChangeEvent;
import com.mcculloch.pokemon.battle.event.PokeSpriteEvent;
import com.mcculloch.pokemon.battle.event.TextEvent;
import com.mcculloch.pokemon.model.Move;
import com.mcculloch.pokemon.model.Pokemon;
import com.mcculloch.pokemon.Main;

import static com.mcculloch.pokemon.Main.playerTrainer;

/**
 * A 100% real Pokemon fight! Right in your livingroom.
 * 
 * @author hydrozoa
 */
public class Battle implements BattleEventQueuer {
	
	public enum STATE {
		READY_TO_PROGRESS,
		SELECT_NEW_POKEMON,
		RAN,
		WIN,
		LOSE,
		;
	}
	
	private STATE state;
	
	private BattleMechanics mechanics;
	
	private Pokemon playerPokemon;
	private Pokemon opponentPokemon;

	private Trainer opponentTrainer;

	private BattleEventPlayer eventPlayer; 
	
	public Battle(Trainer player, Trainer opponent) {
		this.playerPokemon = player.party.get(0);
		this.opponentPokemon = opponent.party.get(0);
		mechanics = new BattleMechanics();
		this.state = STATE.READY_TO_PROGRESS;
	}
	
	/**
	 * Plays appropiate animation for starting a battle
	 */
	public void beginBattle() {
		queueEvent(new PokeSpriteEvent(opponentPokemon.getFrontSprite(), BATTLE_PARTY.OPPONENT));
		queueEvent(new TextEvent("Go "+ playerPokemon.getNickname()+"!", 1f));
		queueEvent(new PokeSpriteEvent(playerPokemon.getBackSprite(), BATTLE_PARTY.PLAYER));
		queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new PokeballAnimation()));
	}
	
	
	/**
	 * Progress the battle one turn. 
	 * @param input		Index of the move used by the player
	 */
	public void progress(int input) {
		if (state != STATE.READY_TO_PROGRESS) {
			return;
		}
		if (mechanics.goesFirst(playerPokemon, opponentPokemon)) {
			playTurn(BATTLE_PARTY.PLAYER, input);	
			if (state == STATE.READY_TO_PROGRESS) {
				playTurn(BATTLE_PARTY.OPPONENT, 0);
			}
		} else {
			playTurn(BATTLE_PARTY.OPPONENT, 0);
			if (state == STATE.READY_TO_PROGRESS) {
				playTurn(BATTLE_PARTY.PLAYER, input);
			}
		}
		/*
		 * XXX: Status effects go here.
		 */
	}
	
	/**
	 * Sends out a new Pokemon, in the case that the old one fainted.
	 * This will NOT take up a turn.
	 * @param pokemon	Pokemon the trainer is sending in
	 */
	public void chooseNewPokemon(Pokemon pokemon) {
		this.playerPokemon = pokemon;
		queueEvent(new HPAnimationEvent(
				BATTLE_PARTY.PLAYER, 
				pokemon.getHp(),
				pokemon.getHp(),
				pokemon.getHp(),
				0f));
		queueEvent(new PokeSpriteEvent(pokemon.getFrontSprite(), BATTLE_PARTY.PLAYER));
		queueEvent(new NameChangeEvent(pokemon.getNickname(), BATTLE_PARTY.PLAYER));
		queueEvent(new TextEvent("Go get 'em, "+pokemon.getNickname()+"!"));
		queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new PokeballAnimation()));
		this.state = STATE.READY_TO_PROGRESS;
	}
	
	/**
	 * Attempts to run away
	 */
	public void attemptRun() {
		queueEvent(new TextEvent("Got away successfully...", true));
		this.state = STATE.RAN;
	}
	
	private void playTurn(BATTLE_PARTY user, int input) {
		BATTLE_PARTY target = BATTLE_PARTY.getOpposite(user);
		
		Pokemon pokeUser = null;
		Pokemon pokeTarget = null;
		if (user == BATTLE_PARTY.PLAYER) {
			pokeUser = playerPokemon;
			pokeTarget = opponentPokemon;
		} else if (user == BATTLE_PARTY.OPPONENT) {
			pokeUser = opponentPokemon;
			pokeTarget = playerPokemon;
		}
		
		Move move = pokeUser.getMove(input);
		
		/* Broadcast the text graphics */
		queueEvent(new TextEvent(pokeUser.getNickname()+" used\n"+move.getName().toUpperCase()+"!", 0.5f));
		
		if (mechanics.attemptHit(move, pokeUser, pokeTarget)) {
			pokeUser.useMove(pokeTarget,move);
		} else { // miss
			/* Broadcast the text graphics */
			queueEvent(new TextEvent(pokeUser.getNickname()+"'s\nattack missed!", 0.5f));
		}
		
		if (playerPokemon.isDead()) {
			queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new FaintingAnimation()));
			if (playerTrainer.canBattle()) {
				queueEvent(new TextEvent(playerPokemon.getNickname()+" fainted!", true));
				this.state = STATE.SELECT_NEW_POKEMON;
			} else {
				queueEvent(new TextEvent("Unfortunately, you've lost...", true));
				this.state = STATE.LOSE;
			}
		} else if (opponentPokemon.isDead()) {
			queueEvent(new AnimationBattleEvent(BATTLE_PARTY.OPPONENT, new FaintingAnimation()));
			queueEvent(new TextEvent("Congratulations! You Win!", true));
			this.state = STATE.WIN;
		}
	}
	
	public Pokemon getPlayerPokemon() {
		return playerPokemon;
	}
	
	public Pokemon getOpponentPokemon() {
		return opponentPokemon;
	}
	
	public Trainer getOpponentTrainer() {
		return opponentTrainer;
	}
	
	public STATE getState() {
		return state;
	}

	public void setEventPlayer(BattleEventPlayer player) {
		this.eventPlayer = player;
	}
	
	@Override
	public void queueEvent(BattleEvent event) {
		eventPlayer.queueEvent(event);
	}
}
