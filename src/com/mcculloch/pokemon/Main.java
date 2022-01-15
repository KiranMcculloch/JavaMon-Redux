package com.mcculloch.pokemon;

import java.util.Calendar;
import java.util.Date;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mcculloch.pokemon.battle.Trainer;
import com.mcculloch.pokemon.model.Pokemon;

public class Main {

	public static Trainer playerTrainer = new Trainer("Player");
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = 400;
		config.width = 600;
		config.vSyncEnabled = false;
		config.foregroundFPS = 200;
		config.addIcon("res/graphics/pokeball_icon.png", Files.FileType.Local);
		
		new LwjglApplication(new PokemonGame(), config);
	}

	//day = 600-1959
	//night = 2000-559
	public static int timeOfDay(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		return (hour*100)+minutes;
	}

}
