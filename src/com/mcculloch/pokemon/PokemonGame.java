package com.mcculloch.pokemon;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mcculloch.pokemon.battle.Battle;
import com.mcculloch.pokemon.battle.Trainer;
import com.mcculloch.pokemon.battle.animation.*;
import com.mcculloch.pokemon.model.Item;
import com.mcculloch.pokemon.model.Pokemon;
import com.mcculloch.pokemon.model.world.World;
import com.mcculloch.pokemon.screen.AbstractScreen;
import com.mcculloch.pokemon.screen.BattleScreen;
import com.mcculloch.pokemon.screen.GameScreen;
import com.mcculloch.pokemon.screen.TransitionScreen;
import com.mcculloch.pokemon.screen.transition.BattleBlinkTransition;
import com.mcculloch.pokemon.screen.transition.BattleBlinkTransitionAccessor;
import com.mcculloch.pokemon.screen.transition.Transition;
import com.mcculloch.pokemon.util.Action;
import com.mcculloch.pokemon.util.SkinGenerator;
import com.mcculloch.pokemon.worldloader.*;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import static com.mcculloch.pokemon.Main.playerTrainer;

/**
 * Topmost class of the game. Logic is delegated to Screens from here.
 * 
 * @author hydrozoa
 */
public class PokemonGame extends Game {
	
	private GameScreen gameScreen;
	private BattleScreen battleScreen;
	private TransitionScreen transitionScreen;

	
	private AssetManager assetManager;
	
	private Skin skin;
	
	private TweenManager tweenManager;
	
	private ShaderProgram overlayShader;
	private ShaderProgram transitionShader;
	
	private String version;

	@Override
	public void create() {
		/*
		 * LOAD VERSION
		 */
		version = Gdx.files.internal("version.txt").readString();
		System.out.println("Pokémon by Kiran"+version);
		Gdx.app.getGraphics().setTitle("Pokémon by Kiran, version "+version);
		
		/*
		 * LOADING SHADERS
		 */
		ShaderProgram.pedantic = false;
		overlayShader = new ShaderProgram(
				Gdx.files.internal("res/shaders/overlay/vertexshader.txt"), 
				Gdx.files.internal("res/shaders/overlay/fragmentshader.txt"));
		if (!overlayShader.isCompiled()) {
			System.out.println(overlayShader.getLog());
		}
		transitionShader = new ShaderProgram(
				Gdx.files.internal("res/shaders/transition/vertexshader.txt"), 
				Gdx.files.internal("res/shaders/transition/fragmentshader.txt"));
		if (!transitionShader.isCompiled()) {
			System.out.println(transitionShader.getLog());
		}
		
		/*
		 * SETTING UP TWEENING
		 */
		tweenManager = new TweenManager();
		Tween.registerAccessor(BattleAnimation.class, new BattleAnimationAccessor());
		Tween.registerAccessor(BattleSprite.class, new BattleSpriteAccessor());
		Tween.registerAccessor(AnimatedBattleSprite.class, new BattleSpriteAccessor());
		Tween.registerAccessor(BattleBlinkTransition.class, new BattleBlinkTransitionAccessor());
		
		/*
		 * LOADING ASSETS
		 */
		assetManager = new AssetManager();
		assetManager.setLoader(LWorldObjectDb.class, new LWorldObjectLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(LTerrainDb.class, new LTerrainLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(World.class, new WorldLoader(new InternalFileHandleResolver()));
		
		assetManager.load("res/LTerrain.xml", LTerrainDb.class);
		assetManager.load("res/LWorldObjects.xml", LWorldObjectDb.class);
		
		assetManager.load("res/graphics_packed/tiles/tilepack.atlas", TextureAtlas.class);
		assetManager.load("res/graphics_packed/ui/uipack.atlas", TextureAtlas.class);
		assetManager.load("res/graphics_packed/battle/battlepack.atlas", TextureAtlas.class);
		for (int i = 0; i < 32; i++) {
			assetManager.load("res/graphics/statuseffect/attack_"+i+".png", Texture.class);
		}
		assetManager.load("res/graphics/statuseffect/white.png", Texture.class);
		
		for (int i = 0; i < 13; i++) {
			assetManager.load("res/graphics/transitions/transition_"+i+".png", Texture.class);
		}
		assetManager.load("res/font/small_letters_font.fnt", BitmapFont.class);
		
		File dir = new File("res/worlds/");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("Loading world "+child.getPath());
				assetManager.load(child.getPath(), World.class);
		    }
		}
		
		assetManager.finishLoading();
		
		skin = SkinGenerator.generateSkin(assetManager);
		
		gameScreen = new GameScreen(this);
		battleScreen = new BattleScreen(this);
		transitionScreen = new TransitionScreen(this);

		int random1 = ThreadLocalRandom.current().nextInt(1, 802 + 1);
		int random2 = ThreadLocalRandom.current().nextInt(1, 802 + 1);
		Pokemon Shaymin = new Pokemon("Shaydo",random1,52,72,73,74,75,new Item(0,1),false,0);
		Pokemon Bewear = new Pokemon("Beary",random2,55,167,168,292,303,new Item(0,1),false,0);

		playerTrainer.addPokemon(Shaymin);
		Trainer testOpponent = new Trainer("Rival");
		testOpponent.addPokemon(Bewear);

		Battle testBattle = new Battle(playerTrainer,testOpponent);

		battleScreen.initBattleScreen(testBattle);

		this.setScreen(battleScreen);
	}
	
	@Override
	public void render() {
		//System.out.println(Gdx.graphics.getFramesPerSecond());
		
		/* UPDATE */
		tweenManager.update(Gdx.graphics.getDeltaTime());
		if (getScreen() instanceof AbstractScreen) {
			((AbstractScreen)getScreen()).update(Gdx.graphics.getDeltaTime());
		}
		
		/* RENDER */
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public TweenManager getTweenManager() {
		return tweenManager;
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
	public BattleScreen getBattleScreen() {
		return battleScreen;
	}
	
	public void startTransition(AbstractScreen from, AbstractScreen to, Transition out, Transition in, Action action) {
		transitionScreen.startTransition(from, to, out, in, action);
	}
	
	public ShaderProgram getOverlayShader() {
		return overlayShader;
	}
	
	public ShaderProgram getTransitionShader() {
		return transitionShader;
	}
	
	public String getVersion() {
		return version;
	}
}
