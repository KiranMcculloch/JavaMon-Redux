package com.mcculloch.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.mcculloch.pokemon.model.Move;
import com.mcculloch.pokemon.model.Pokemon;

/**
 * Contains methods useful for calculations during battle. 
 * 
 * Some say this is a ShoddyBattle tactic, and they're probably right.
 * 
 * @author hydrozoa
 */
public class BattleMechanics {
	
	private String message = "";
	
	private boolean criticalHit(Move move, Pokemon user, Pokemon target) {
		float probability = 1f/16f;
		if (probability >= MathUtils.random(1.0f)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return True if the player goes first.
	 */
	public boolean goesFirst(Pokemon playerPokemon, Pokemon opponentPokemon) {
		if (playerPokemon.getSpeed() > opponentPokemon.getSpeed()) {
			return true;
		} else if (opponentPokemon.getSpeed() > playerPokemon.getSpeed()) {
			return false;
		} else {
			return MathUtils.randomBoolean();
		}
	}
	
	public boolean attemptHit(Move move, Pokemon user, Pokemon target) {
		float random = MathUtils.random(1.0f);
		return (move.getAccuracy() / 100f) >= random;
	}
	
	public boolean hasMessage() {
		return !message.isEmpty();
	}
	
	public String getMessage() {
		return message;
	}
}
