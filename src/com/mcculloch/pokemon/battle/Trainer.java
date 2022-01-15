package com.mcculloch.pokemon.battle;

import java.util.ArrayList;
import java.util.List;

import com.mcculloch.pokemon.model.Pokemon;

public class Trainer {
	public ArrayList<Pokemon> party = new ArrayList<>();
	private int partySize;
	private String name;

	public Trainer(String name){
		this.name = name;
	}

	public void addPokemon(Pokemon pPokemon){
		if (partySize < 6){
			party.add(pPokemon);
		}
		partySize += 1;
	}

	public void removePokemon(Pokemon pPokemon){
		if (partySize > 1){
			party.remove(pPokemon);
		}
		partySize -= 1;
	}

	public int getTeamSize(){
		return partySize;
	}

	public boolean canBattle(){
		int x = 0;
		for(int i = 0; i < partySize; i++){
			if (this.party.get(i).isDead()){
				x+=1;
			}
		}
		if (x>=partySize){ return false; } else{ return true; }
	}
}
