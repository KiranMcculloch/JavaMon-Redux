package com.mcculloch.pokemon.model.actor;

import com.mcculloch.pokemon.model.world.World;
import com.mcculloch.pokemon.model.world.cutscene.CutscenePlayer;
import com.mcculloch.pokemon.util.AnimationSet;

/**
 * @author hydrozoa
 */
public class PlayerActor extends Actor {
	
	private CutscenePlayer cutscenePlayer;

	public PlayerActor(World world, int x, int y, AnimationSet animations, CutscenePlayer cutscenePlayer) {
		super(world, x, y, animations);
		this.cutscenePlayer = cutscenePlayer;
	}
	
	public CutscenePlayer getCutscenePlayer() {
		return cutscenePlayer;
	}
}
