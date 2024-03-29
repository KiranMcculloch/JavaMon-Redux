package com.mcculloch.pokemon.battle;

import com.mcculloch.pokemon.battle.event.BattleEvent;

/**
 * Objects can implement this interface and subscribe to a {@link Battle}.
 * 
 * @author hydrozoa
 */
public interface BattleObserver {
	
	/**
	 * {@link com.mcculloch.pokemon.battle.event.BattleEvent} spat out from a {@link Battle}.
	 * @param event	Catch it fast, and get free visuals for a live fight.
	 */
	public void queueEvent(BattleEvent event);
}
