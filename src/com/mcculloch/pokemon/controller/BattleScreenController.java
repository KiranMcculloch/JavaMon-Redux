package com.mcculloch.pokemon.controller;

import java.util.Queue;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.mcculloch.pokemon.battle.Battle;
import com.mcculloch.pokemon.battle.event.BattleEvent;
import com.mcculloch.pokemon.battle.event.TextEvent;
import com.mcculloch.pokemon.ui.DialogueBox;
import com.mcculloch.pokemon.ui.MoveSelectBox;
import com.mcculloch.pokemon.ui.OptionBox;
import org.lwjgl.Sys;

import static com.mcculloch.pokemon.Main.playerTrainer;


public class BattleScreenController extends InputAdapter {
	
	public enum STATE {
		USE_NEXT_POKEMON, 	// Text displayed when Pokemon faints 
		SELECT_ACTION,		// Moves, Items, Pokemon, Run
		DEACTIVATED,		// Do nothing, display nothing
		;
	}
	
	private STATE state = STATE.DEACTIVATED;
	
	private Queue<BattleEvent> queue;
	
	private Battle battle;
	
	private DialogueBox dialogue;
	private OptionBox optionBox;
	private MoveSelectBox moveSelect;
	
	public BattleScreenController(Battle battle, Queue<BattleEvent> queue, DialogueBox dialogue, MoveSelectBox options, OptionBox optionBox) {
		this.battle = battle;
		this.queue = queue;
		this.dialogue = dialogue;
		this.moveSelect = options;
		this.optionBox = optionBox;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (this.state == STATE.DEACTIVATED) {
			return false;
		}
		if (this.state == STATE.USE_NEXT_POKEMON && optionBox.isVisible()) {
			if (keycode == Keys.UP) {
				optionBox.moveUp();
			} else if (keycode == Keys.DOWN) {
				optionBox.moveDown();
			} else if (keycode == Keys.ENTER) {
				if (optionBox.getIndex() == 0) { // YES selected
					
					
					/* 
					 * WRONG
					 */
					for (int i = 0; i < playerTrainer.getTeamSize(); i++) {
						if (!playerTrainer.party.get(i).isDead()) {
							battle.chooseNewPokemon(playerTrainer.party.get(i));
							optionBox.setVisible(false);
							this.state = STATE.DEACTIVATED;
							break;
						}
					}
				} else if (optionBox.getIndex() == 1) { // NO selected
					battle.attemptRun();
					optionBox.setVisible(false);
					this.state = STATE.DEACTIVATED;
				}
			}
		}
		if (moveSelect.isVisible()) {
			if (keycode == Keys.ENTER) {
				int selection = moveSelect.getSelection();
				if (battle.getPlayerPokemon().getMove(selection) == null) {
					queue.add(new TextEvent("No such move...", 0.5f));
				} else {
					battle.progress(moveSelect.getSelection());
					endTurn();
				}
			} else if (keycode == Keys.UP) {
				moveSelect.moveUp();
				return true;
			} else if (keycode == Keys.DOWN) {
				moveSelect.moveDown();
				return true;
			} else if (keycode == Keys.LEFT) {
				moveSelect.moveLeft();
				return true;
			} else if (keycode == Keys.RIGHT) {
				moveSelect.moveRight();
				return true;
			}
		}
		return false;
	}
	
	public STATE getState() {
		return state;
	}
	
	public void update(float delta) {
		if (isDisplayingNextDialogue() && dialogue.isFinished() && !optionBox.isVisible()) {
			optionBox.clearChoices();
			optionBox.addOption("YES");
			optionBox.addOption("NO");
			optionBox.setVisible(true);
		}
	}
	
	/**
	 * Displays the UI for a new turn
	 */
	public void restartTurn() {
		this.state = STATE.SELECT_ACTION;
		dialogue.setVisible(false);
		for (int i = 0; i <= 3; i++){
			String label = "------";
			moveSelect.setLabel(i, label.toUpperCase());
		}
		moveSelect.setVisible(true);
	}
	
	/**
	 * Displays UI for selecting a new Pokemon
	 */
	public void displayNextDialogue() {
		this.state = STATE.USE_NEXT_POKEMON;
		dialogue.setVisible(true);
		dialogue.animateText("Send out next pokemon?");
	}
	
	public boolean isDisplayingNextDialogue() {
		return this.state == STATE.USE_NEXT_POKEMON;
	}
	
	private void endTurn() {
		moveSelect.setVisible(false);
		this.state = STATE.DEACTIVATED;
	}
}
