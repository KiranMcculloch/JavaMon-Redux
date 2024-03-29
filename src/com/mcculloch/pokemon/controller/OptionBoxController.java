package com.mcculloch.pokemon.controller;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.mcculloch.pokemon.ui.OptionBox;
import com.mcculloch.pokemon.util.Action;

/**
 * Controller that can be coupled to a OptionBox, coupling each option with code for execution.
 * 
 * @author Hydrozoa
 */
public class OptionBoxController extends InputAdapter {
	
	private OptionBox box;
	private List<Action> actions;
	
	public OptionBoxController(OptionBox box) {
		this.box = box;
		this.actions = new ArrayList<Action>(box.getAmount());
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (!box.isVisible()) {
			return false;
		}
		
		if (keycode == Keys.UP) {
			return true;
		} else if (keycode == Keys.DOWN) {
			return true;
		} else if (keycode == Keys.ENTER) {
			// activate
			return true;
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.F12) {
			box.setVisible(!box.isVisible());
			return true;
		}
		
		if (!box.isVisible()) {
			return false;
		}
		
		if (keycode == Keys.UP) {
			box.moveUp();
			return true;
		} else if (keycode == Keys.DOWN) {
			box.moveDown();
			return true;
		} else if (keycode == Keys.X) {		// activate
			actions.get(box.getIndex()).action();
			box.setVisible(false);
			return true;
		}
		return false;
	}
	
	public void addAction(Action a, int index) {
		actions.add(index, a);
	}
	
	public void addAction(Action a, String option) {
		box.addOption(option);
		actions.add(a);
	}
}
