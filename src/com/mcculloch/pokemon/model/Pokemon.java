package com.mcculloch.pokemon.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.mcculloch.pokemon.Templates.TemplatePokemon;
import com.mcculloch.pokemon.Main;
import com.mcculloch.pokemon.PokemonGame;


import java.util.Random;

public class Pokemon {
	private String nickname;
	private int id, hp, level, experience;
	private int health, attack, defense, sAttack, sDefense, speed, friendship;
	private Item heldItem;
	final private int ivHealth, ivAttack, ivDefense, ivSAttack, ivSDefense, ivSpeed;
	final private int gender;
	//0=genderless, 1=female, 2=male
	final private boolean isShiny;
	private boolean fainted;
	private int status;
	//0=healthy,1=burn,2=freeze,3=paralysis,4=poison,5=toxic,6=sleep
	private int form;
	//species-dependent, see templates/forms.txt
	private Move[] moveset = new Move[4];
	static Random rGen = new Random();


	//CURRENTLY ONLY POKEMON THROUGH 802 ARE SPRITED, ANY OTHERS WILL CAUSE A CRASH
	private Texture frontSprite, backSprite;

	public Pokemon(String nickname, int dexNumber, int level, int move1, int move2, int move3, int move4, Item item, boolean isShiny, int form){
		if (nickname == ""){
			this.nickname =  TemplatePokemon.pokemonList[dexNumber].species;
			this.friendship = 0;
		} else {
			this.nickname = nickname;
			this.friendship = 70;
		}
		this.id = dexNumber;
		this.level = level;
		this.health = TemplatePokemon.pokemonList[dexNumber].health;
		this.hp = this.health;
		this.attack = TemplatePokemon.pokemonList[dexNumber].attack;
		this.sAttack = TemplatePokemon.pokemonList[dexNumber].sAttack;
		this.defense = TemplatePokemon.pokemonList[dexNumber].defense;
		this.sDefense = TemplatePokemon.pokemonList[dexNumber].sDefense;
		this.speed = TemplatePokemon.pokemonList[dexNumber].speed;
		this.ivHealth = rGen.nextInt(32);
		this.ivAttack = rGen.nextInt(32);
		this.ivDefense = rGen.nextInt(32);
		this.ivSAttack = rGen.nextInt(32);
		this.ivSDefense = rGen.nextInt(32);
		this.ivSpeed = rGen.nextInt(32);
		this.status = 0;
		this.moveset[0] = new Move(move1);
		this.moveset[1] = new Move(move2);
		this.moveset[2] = new Move(move3);
		this.moveset[3] = new Move(move4);
		this.experience = (int) Math.pow(level,3.0);
		if (TemplatePokemon.pokemonList[dexNumber].genderRatio == -1){
			this.gender = 0;
		} else {
			int k = rGen.nextInt(100);
			if (k > TemplatePokemon.pokemonList[dexNumber].genderRatio){
				this.gender = 2;
			} else {
				this.gender = 1;
			}
		}
		this.heldItem = item;
		this.isShiny = isShiny;
		this.form = form;
		this.fainted = false;

		String genderString = "Female";
		String shinyString = "Normal";

		if (this.gender == 2){
			genderString = "Male";
		}

		if (this.isShiny){
			shinyString = "Shiny";
		}

		//TODO: Resize pokemon sprites to consistent size upon import
		String frontFilepathBeginningString = "res/graphics/Pokemon/" + genderString + "/" + shinyString + "/Front/" + dexNumber;
		String backFilepathBeginningString = "res/graphics/Pokemon/" + genderString + "/" + shinyString + "/Back/" + dexNumber;
		if (form == 0) {
			frontSprite = new Texture(Gdx.files.internal(frontFilepathBeginningString+".png"));
			backSprite = new Texture(Gdx.files.internal(backFilepathBeginningString+".png"));
		} else {
			frontSprite = new Texture(Gdx.files.internal(frontFilepathBeginningString+"_"+form+".png"));
			backSprite = new Texture(Gdx.files.internal(backFilepathBeginningString+"_"+form+".png"));
		}
	}

	//use only for evolving a pokemon
//    public Pokemon(Pokemon evolveFrom){
//        this.nickname = evolveFrom.nickname;
//        //this.id = TemplatePokemon.pokemonList[evolveFrom.id].evolutionPointer;
//        this.level = evolveFrom.level;
//        this.health = TemplatePokemon.pokemonList[this.id].health;
//        this.hp = this.health;
//        this.attack = TemplatePokemon.pokemonList[this.id].attack;
//        this.sAttack = TemplatePokemon.pokemonList[this.id].sAttack;
//        this.defense = TemplatePokemon.pokemonList[this.id].defense;
//        this.sDefense = TemplatePokemon.pokemonList[this.id].sDefense;
//        this.speed = TemplatePokemon.pokemonList[this.id].speed;
//        this.ivHealth = evolveFrom.ivHealth;
//        this.ivAttack = evolveFrom.ivAttack;
//        this.ivDefense = evolveFrom.ivDefense;
//        this.ivSAttack = evolveFrom.ivSAttack;
//        this.ivSDefense = evolveFrom.ivSDefense;
//        this.ivSpeed = evolveFrom.ivSpeed;
//        this.status = evolveFrom.status;
//        this.moveset = evolveFrom.moveset;
//        this.experience = evolveFrom.experience;
//        this.gender = evolveFrom.gender;
//    }

	public void kill(){
		this.fainted = true;
	}

	public boolean isDead(){ return this.fainted; }

	public int getGender(){
		return this.gender;
	}

	public int getID(){
		return this.id;
	}

	public String getNickname(){
		return nickname;
	}

	public Texture getFrontSprite() {
		return frontSprite;
	}

	public Texture getBackSprite() {
		return backSprite;
	}

	public Move getMove(int index) {
		return moveset[index];
	}

	public void addStatus(int status){
		if (this.status == 0){
			this.status = status;
		} else {
			switch(this.status){
				case 1:
					System.out.println("This pokemon is already burned!");
					break;
				case 2:
					System.out.println("This pokemon is already frozen solid!");
					break;
				case 3:
					System.out.println("This pokemon is already paralyzed!");
					break;
				case 4:
					System.out.println("This pokemon is already poisoned!");
					break;
				case 5:
					System.out.println("This pokemon is already badly poisoned!");
					break;
				case 6:
					System.out.println("This pokemon is already asleep!");
					break;
			}
		}
	}

	public void addHp(int x){
		this.hp += x;
		if (this.hp > TemplatePokemon.pokemonList[this.id].health){
			this.hp = TemplatePokemon.pokemonList[this.id].health;
		}
	}

	public void removeHp(int x){
		this.hp -= x;
		if (this.hp <= 0){
			this.hp = 0;
		}
	}

	public int getHp(){
		return this.hp;
	}

	public Move[] getMoves(){
		return this.moveset;
	}

	public boolean knowsMove(int moveID){
		boolean knows = false;
		for (int i = 0; i<4; i++){
			if (this.moveset[i].id == moveID){
				knows = true;
			}
		}
		return knows;
	}

	public boolean knowsMoveType(int type){
		boolean knows = false;
		for (int i = 0; i<4; i++){
			if (this.moveset[i].type == type){
				knows = true;
			}
		}
		return knows;
	}

	public int getTypeOne() { return TemplatePokemon.pokemonList[this.id].type1; }
	public int getTypeTwo() { return TemplatePokemon.pokemonList[this.id].type1; }

	public void holdItem(Item item) {
		this.heldItem = item;
		item.add(-1);
	}

	public void gainXP(int xp){
		this.experience += xp;
		boolean finished = false;
		int oldHealth = this.health;
		int oldAttack = this.attack;
		int oldSAttack = this.sAttack;
		int oldDefense = this.defense;
		int oldSDefense = this.sDefense;
		int oldSpeed = this.speed;
		while (!finished){
			if (this.experience >= Math.pow(this.level+1,3)){
				this.level += 1;
				System.out.println("Congrats! "+this.nickname+" grew to level "+this.level+"!\n");
				if (this.id != 292){
					this.health = ((((2*TemplatePokemon.pokemonList[this.id].health)+this.ivHealth)*this.level)/100)+this.level+10;
				}
				this.attack = ((((2*TemplatePokemon.pokemonList[this.id].attack)+this.ivAttack)*this.level)/100)+5;
				this.sAttack = ((((2*TemplatePokemon.pokemonList[this.id].sAttack)+this.ivSAttack)*this.level)/100)+5;
				this.defense = ((((2*TemplatePokemon.pokemonList[this.id].defense)+this.ivDefense)*this.level)/100)+5;
				this.sDefense = ((((2*TemplatePokemon.pokemonList[this.id].sDefense)+this.ivSDefense)*this.level)/100)+5;
				this.speed = ((((2*TemplatePokemon.pokemonList[this.id].speed)+this.ivSpeed)*this.level)/100)+5;
				System.out.println("Health: +"+(this.health-oldHealth));
				System.out.println("Attack: +"+(this.attack-oldAttack));
				System.out.println("Special Attack: +"+(this.sAttack-oldSAttack));
				System.out.println("Defense: +"+(this.defense-oldDefense));
				System.out.println("Special Defense: +"+(this.sDefense-oldSDefense));
				System.out.println("Speed: +"+(this.speed-oldSpeed));
				checkEvolutionRequirements(this, null);
			} else {
				finished = true;
			}
		}
	}

	public int[] getStats(){
		return new int[]{this.health, this.attack, this.defense, this.sAttack, this.sDefense, this.speed};
	}

	public void useMove(Pokemon target, Move move){
		int damage = 0;
		if (move.getPP() == 0){
			System.out.println("You can't use this move!");
		} else {
			move.addPP(-1);
			float a,d;
			if (move.physical){
				a = this.attack;
				d = target.defense;
			} else {
				a = this.sAttack;
				d = target.sDefense;
			}
			float stab = 1.0f;
			if (TemplatePokemon.pokemonList[this.id].type1 == move.type || TemplatePokemon.pokemonList[this.id].type2  == move.type){
				stab = 1.5f;
			}
			if (move.power != 0) {
				if (this.status == 1 && move.physical) {
					damage = (int) (((((((this.level * 2.0f) / 5.0f) + 2.0f) * move.power * (a / d)) / 50.0f) + 2.0f) * stab * 0.5 * Move.effectiveness(move.type, target));
				} else {
					damage = (int) (((((((this.level * 2.0f) / 5.0f) + 2.0f) * move.power * (a / d)) / 50.0f) + 2.0f) * stab * Move.effectiveness(move.type, target));
				}
			}
			if (Move.effectiveness(move.type,target) == 0){
				System.out.println("It doesn't affect the opposing pokemon...");
			}else if (Move.effectiveness(move.type,target) == 0.25){
				System.out.println("It barely makes a dent...");
			}else if (Move.effectiveness(move.type,target) == 0.5){
				System.out.println("It's not very effective...");
			}else if (Move.effectiveness(move.type,target) == 2){
				System.out.println("It's super effective!");
			}else if (Move.effectiveness(move.type,target) == 4){
				System.out.println("It's incredibly effective!!");
			}

			target.removeHp(damage);
			if (target.getHp() <= 0){
				target.kill();
				System.out.println("The opposing pokemon fainted!");
			} else {
				//move.doEffects(target);
			}
		}
	}


	public static void checkEvolutionRequirements(Pokemon pokemon, Item itemused){

	}

	public int getMaxHealth() {
		return health;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getSAttack() {
		return sAttack;
	}

	public int getSDefense() {
		return sDefense;
	}

	public int getSpeed() {
		return speed;
	}


	//HERE BEGINS THE COMPLICATED EVOLUTION SECTION

	public void evolve(int evolveTo){

		//add in ability to cancel somewhere in this method!
		this.id = evolveTo;
		this.health = ((((2*TemplatePokemon.pokemonList[evolveTo].health)+this.ivHealth)*this.level)/100)+this.level+10;
		this.hp = this.health;
		this.attack = ((((2*TemplatePokemon.pokemonList[evolveTo].attack)+this.ivAttack)*this.level)/100)+5;
		this.sAttack = ((((2*TemplatePokemon.pokemonList[evolveTo].sAttack)+this.ivSAttack)*this.level)/100)+5;
		this.defense = ((((2*TemplatePokemon.pokemonList[evolveTo].defense)+this.ivDefense)*this.level)/100)+5;
		this.sDefense = ((((2*TemplatePokemon.pokemonList[evolveTo].sDefense)+this.ivSDefense)*this.level)/100)+5;
		this.speed = ((((2*TemplatePokemon.pokemonList[evolveTo].speed)+this.ivSpeed)*this.level)/100)+5;
	}

	public static void checkEvolutionRequirements(Pokemon pokemon){
		switch(pokemon.id){
			case 1: if (pokemon.level >= 16) {pokemon.evolve(2);} break;
			case 2: if (pokemon.level >= 32){pokemon.evolve(3);}break;
			case 4: if (pokemon.level >= 16){pokemon.evolve(5);}break;
			case 5: if (pokemon.level >= 36){pokemon.evolve(6);}break;
			case 7: if (pokemon.level >= 16){pokemon.evolve(8);}break;
			case 8: if (pokemon.level >= 36){pokemon.evolve(9);}break;
			case 10: if (pokemon.level >= 7){pokemon.evolve(11);}break;
			case 11: if (pokemon.level >= 10){pokemon.evolve(12);}break;
			case 13: if (pokemon.level >= 7){pokemon.evolve(14);}break;
			case 14: if (pokemon.level >= 10){pokemon.evolve(15);}break;
			case 16: if (pokemon.level >= 7){pokemon.evolve(17);}break;
			case 17: if (pokemon.level >= 10){pokemon.evolve(18);}break;
			case 19: if (pokemon.level >= 20){pokemon.evolve(20);}break;
			case 23: if (pokemon.level >= 22){pokemon.evolve(24);} break;
			case 27: if (pokemon.level >= 22){pokemon.evolve(28);} break;
			case 29:
				if (pokemon.level >= 16){
					if (pokemon.gender == 1){pokemon.evolve(30);}
					else if (pokemon.gender == 2){pokemon.evolve(33);}
				}
				break;
			case 41: if (pokemon.level >= 22){pokemon.evolve(42);} break;
			case 42: if (pokemon.friendship >= 220){pokemon.evolve(169);} break;
			case 43: if (pokemon.level >= 21){pokemon.evolve(44);} break;
			case 46: if (pokemon.level >= 24){pokemon.evolve(47);} break;
			case 48: if (pokemon.level >= 31){pokemon.evolve(49);} break;
			case 50: if (pokemon.level >= 26){pokemon.evolve(51);} break;
			case 52: if (pokemon.level >= 28){pokemon.evolve(53);} break;
			case 54: if (pokemon.level >= 33){pokemon.evolve(55);} break;
			case 56: if (pokemon.level >= 28){pokemon.evolve(57);} break;
			case 60: if (pokemon.level >= 25){pokemon.evolve(61);} break;
			case 63: if (pokemon.level >= 16){pokemon.evolve(64);} break;
			case 66: if (pokemon.level >= 28){pokemon.evolve(67);} break;
			case 69: if (pokemon.level >= 21){pokemon.evolve(70);} break;
			case 72: if (pokemon.level >= 30){pokemon.evolve(73);} break;
			case 74: if (pokemon.level >= 25){pokemon.evolve(75);} break;
			case 77: if (pokemon.level >= 40){pokemon.evolve(78);} break;
			case 79: if (pokemon.level >= 37){pokemon.evolve(80);} break;
			case 81: if (pokemon.level >= 30){pokemon.evolve(82);} break;
			case 84: if (pokemon.level >= 31){pokemon.evolve(85);} break;
			case 86: if (pokemon.level >= 34){pokemon.evolve(87);} break;
			case 88: if (pokemon.level >= 38){pokemon.evolve(89);} break;
			case 92: if (pokemon.level >= 25){pokemon.evolve(93);} break;
			case 96: if (pokemon.level >= 26){pokemon.evolve(97);} break;
			case 98: if (pokemon.level >= 28){pokemon.evolve(99);} break;
			case 100: if (pokemon.level >= 30){pokemon.evolve(101);} break;
			case 104: if (pokemon.level >= 28){pokemon.evolve(105);} break;
			case 108: if (pokemon.knowsMove(205)){pokemon.evolve(463);} break;
			case 109: if (pokemon.level >= 35){pokemon.evolve(110);} break;
			case 111: if (pokemon.level >= 42){pokemon.evolve(112);} break;
			case 113: if (pokemon.friendship >= 220){pokemon.evolve(242);} break;
			case 114: if (pokemon.knowsMove(246)){pokemon.evolve(465);} break;
			case 116: if (pokemon.level >= 32){pokemon.evolve(117);} break;
			case 118: if (pokemon.level >= 33){pokemon.evolve(119);} break;
			case 129: if (pokemon.level >= 20){pokemon.evolve(130);} break;
			//eevee
			case 133:
				//leafeon, glaceon go here before sylveon
				if (pokemon.friendship >= 220){
					if (pokemon.knowsMoveType(17)){
						pokemon.evolve(700);
					}
					else if (Main.timeOfDay() >= 600 && Main.timeOfDay() < 2000){
						pokemon.evolve(196);
					}
					else if (Main.timeOfDay() >= 2000 || Main.timeOfDay() < 559){
						pokemon.evolve(197);
					}
				}
				break;
			case 138: if (pokemon.level >= 40){pokemon.evolve(139);} break;
			case 140: if (pokemon.level >= 40){pokemon.evolve(141);} break;
			case 147: if (pokemon.level >= 30){pokemon.evolve(148);} break;
			case 148: if (pokemon.level >= 55){pokemon.evolve(149);} break;
			case 152: if (pokemon.level >= 16){pokemon.evolve(153);} break;
			case 153: if (pokemon.level >= 32){pokemon.evolve(154);} break;
			case 155: if (pokemon.level >= 14){pokemon.evolve(156);} break;
			case 156: if (pokemon.level >= 36){pokemon.evolve(157);} break;
			case 158: if (pokemon.level >= 18){pokemon.evolve(159);} break;
			case 159: if (pokemon.level >= 30){pokemon.evolve(160);} break;
			case 161: if (pokemon.level >= 15){pokemon.evolve(162);} break;
			case 163: if (pokemon.level >= 20){pokemon.evolve(164);} break;
			case 165: if (pokemon.level >= 18){pokemon.evolve(166);} break;
			case 167: if (pokemon.level >= 22){pokemon.evolve(168);} break;
			case 170: if (pokemon.level >= 27){pokemon.evolve(171);} break;
			case 172: if (pokemon.friendship >= 220){pokemon.evolve(25);} break;
			case 173: if (pokemon.friendship >= 220){pokemon.evolve(35);} break;
			case 174: if (pokemon.friendship >= 220){pokemon.evolve(39);} break;
			case 175: if (pokemon.friendship >= 220){pokemon.evolve(176);} break;
			case 177: if (pokemon.level >= 25){pokemon.evolve(178);} break;
			case 179: if (pokemon.level >= 15){pokemon.evolve(180);} break;
			case 180: if (pokemon.level >= 30){pokemon.evolve(181);} break;
			case 183: if (pokemon.level >= 18){pokemon.evolve(184);} break;
			case 187: if (pokemon.level >= 18){pokemon.evolve(188);} break;
			case 188: if (pokemon.level >= 27){pokemon.evolve(189);} break;
			case 190: if(pokemon.knowsMove(458)){pokemon.evolve(424);} break;
			case 193: if(pokemon.knowsMove(246)){pokemon.evolve(469);} break;
			case 194: if (pokemon.level >= 20){pokemon.evolve(195);} break;
			case 204: if (pokemon.level >= 31){pokemon.evolve(205);} break;
			case 207: if ((pokemon.heldItem.id == 327) && (Main.timeOfDay() >= 2000 || Main.timeOfDay() < 559)){pokemon.evolve(472);}break;
			case 209: if (pokemon.level >= 23){pokemon.evolve(210);} break;
			case 215: if ((pokemon.heldItem.id == 326) && (Main.timeOfDay() >= 2000 || Main.timeOfDay() < 559)){pokemon.evolve(461);}break;
			case 216: if (pokemon.level >= 30){pokemon.evolve(217);} break;
			case 218: if (pokemon.level >= 38){pokemon.evolve(219);} break;
			case 220: if (pokemon.level >= 33){pokemon.evolve(221);} break;
			case 221: if(pokemon.knowsMove(246)){pokemon.evolve(473);} break;
			case 223: if (pokemon.level >= 25){pokemon.evolve(224);} break;
			case 228: if (pokemon.level >= 24){pokemon.evolve(229);} break;
			case 231: if (pokemon.level >= 25){pokemon.evolve(232);} break;
			case 236:
				if (pokemon.level >= 20){
					if (pokemon.attack > pokemon.defense){
						pokemon.evolve(106);
					}
					if (pokemon.attack < pokemon.defense){
						pokemon.evolve(107);
					}
					else if (pokemon.attack == pokemon.defense){
						pokemon.evolve(237);
					}
				}
			case 238: if (pokemon.level >= 30){pokemon.evolve(239);} break;
			case 239: if (pokemon.level >= 30){pokemon.evolve(240);} break;
			case 240: if (pokemon.level >= 30){pokemon.evolve(241);} break;
			case 246: if (pokemon.level >= 30){pokemon.evolve(247);} break;
			case 247: if (pokemon.level >= 55){pokemon.evolve(248);} break;
			case 252: if (pokemon.level >= 16){pokemon.evolve(253);} break;
			case 253: if (pokemon.level >= 36){pokemon.evolve(254);} break;
			case 255: if (pokemon.level >= 16){pokemon.evolve(256);} break;
			case 256: if (pokemon.level >= 36){pokemon.evolve(257);} break;
			case 258: if (pokemon.level >= 16){pokemon.evolve(259);} break;
			case 259: if (pokemon.level >= 36){pokemon.evolve(260);} break;
			case 261: if (pokemon.level >= 18){pokemon.evolve(262);} break;
			case 263: if (pokemon.level >= 20){pokemon.evolve(264);} break;
			case 265:
				if (pokemon.level >= 7){
					if (rGen.nextInt(2) == 0){
						pokemon.evolve(266);
					} else {
						pokemon.evolve(268);
					}

				}
				break;
			case 266: if (pokemon.level >= 10){pokemon.evolve(267);} break;
			case 268: if (pokemon.level >= 10){pokemon.evolve(269);} break;
			case 270: if (pokemon.level >= 14){pokemon.evolve(271);} break;
			case 273: if (pokemon.level >= 14){pokemon.evolve(274);} break;
			case 276: if (pokemon.level >= 22){pokemon.evolve(277);} break;
			case 278: if (pokemon.level >= 25){pokemon.evolve(279);} break;
			case 280: if (pokemon.level >= 20){pokemon.evolve(281);} break;
			case 281: if (pokemon.level >= 30){pokemon.evolve(282);} break;
			case 283: if (pokemon.level >= 22){pokemon.evolve(284);} break;
			case 285: if (pokemon.level >= 23){pokemon.evolve(286);} break;
			case 287: if (pokemon.level >= 18){pokemon.evolve(288);} break;
			case 288: if (pokemon.level >= 36){pokemon.evolve(289);} break;
			case 290: if (pokemon.level >= 20){pokemon.evolve(291);} break;
			case 293: if (pokemon.level >= 20){pokemon.evolve(294);} break;
			case 294: if (pokemon.level >= 40){pokemon.evolve(295);} break;
			case 296: if (pokemon.level >= 24){pokemon.evolve(297);} break;
			case 298: if (pokemon.friendship >= 220){pokemon.evolve(183);} break;
			case 304: if (pokemon.level >= 32){pokemon.evolve(305);} break;
			case 305: if (pokemon.level >= 42){pokemon.evolve(306);} break;
			case 307: if (pokemon.level >= 37){pokemon.evolve(308);} break;
			case 309: if (pokemon.level >= 26){pokemon.evolve(310);} break;
			case 316: if (pokemon.level >= 26){pokemon.evolve(317);} break;
			case 318: if (pokemon.level >= 30){pokemon.evolve(319);} break;
			case 320: if (pokemon.level >= 40){pokemon.evolve(321);} break;
			case 322: if (pokemon.level >= 33){pokemon.evolve(323);} break;
			case 325: if (pokemon.level >= 32){pokemon.evolve(326);} break;
			case 328: if (pokemon.level >= 35){pokemon.evolve(329);} break;
			case 329: if (pokemon.level >= 45){pokemon.evolve(330);} break;
			case 331: if (pokemon.level >= 32){pokemon.evolve(332);} break;
			case 333: if (pokemon.level >= 35){pokemon.evolve(334);} break;
			case 339: if (pokemon.level >= 30){pokemon.evolve(340);} break;
			case 341: if (pokemon.level >= 30){pokemon.evolve(342);} break;
			case 343: if (pokemon.level >= 36){pokemon.evolve(344);} break;
			case 345: if (pokemon.level >= 40){pokemon.evolve(346);} break;
			case 347: if (pokemon.level >= 40){pokemon.evolve(348);} break;
			case 353: if (pokemon.level >= 37){pokemon.evolve(354);} break;
			case 355: if (pokemon.level >= 37){pokemon.evolve(356);} break;
			case 360: if (pokemon.level >= 15){pokemon.evolve(202);} break;
			case 361: if (pokemon.level >= 42){pokemon.evolve(362);} break;
			case 363: if (pokemon.level >= 32){pokemon.evolve(364);} break;
			case 364: if (pokemon.level >= 44){pokemon.evolve(365);} break;
			case 371: if (pokemon.level >= 30){pokemon.evolve(372);} break;
			case 372: if (pokemon.level >= 50){pokemon.evolve(373);} break;
			case 374: if (pokemon.level >= 20){pokemon.evolve(375);} break;
			case 375: if (pokemon.level >= 45){pokemon.evolve(376);} break;
			case 387: if (pokemon.level >= 18){pokemon.evolve(388);} break;
			case 388: if (pokemon.level >= 32){pokemon.evolve(389);} break;
			case 390: if (pokemon.level >= 14){pokemon.evolve(391);} break;
			case 391: if (pokemon.level >= 36){pokemon.evolve(392);} break;
			case 393: if (pokemon.level >= 16){pokemon.evolve(394);} break;
			case 394: if (pokemon.level >= 36){pokemon.evolve(395);} break;
			case 396: if (pokemon.level >= 14){pokemon.evolve(397);} break;
			case 397: if (pokemon.level >= 34){pokemon.evolve(398);} break;
			case 399: if (pokemon.level >= 15){pokemon.evolve(400);} break;
			case 401: if (pokemon.level >= 10){pokemon.evolve(402);} break;
			case 403: if (pokemon.level >= 15){pokemon.evolve(404);} break;
			case 404: if (pokemon.level >= 30){pokemon.evolve(405);} break;
			case 406: if (pokemon.friendship >= 220 && (Main.timeOfDay() >= 600 && Main.timeOfDay() < 2000)){pokemon.evolve(315);} break;
			case 408: if (pokemon.level >= 30){pokemon.evolve(409);} break;
			case 410: if (pokemon.level >= 30){pokemon.evolve(411);} break;
			case 412:
				if (pokemon.level >= 20){
					if (pokemon.gender == 1){
						pokemon.evolve(413);
					} else {
						pokemon.evolve(414);
					}
				} break;
			case 415: if (pokemon.level >= 21 && pokemon.gender == 1){pokemon.evolve(416);} break;
			case 418: if (pokemon.level >= 26){pokemon.evolve(419);} break;
			case 420: if (pokemon.level >= 25){pokemon.evolve(421);} break;
			case 422: if (pokemon.level >= 30){pokemon.evolve(423);} break;
			case 425: if (pokemon.level >= 28){pokemon.evolve(426);} break;
			case 427: if (pokemon.friendship >= 220){pokemon.evolve(428);} break;
			case 431: if (pokemon.level >= 38){pokemon.evolve(432);} break;
			case 433: if (pokemon.friendship >= 220 && (Main.timeOfDay() >= 2000 || Main.timeOfDay() < 559)){pokemon.evolve(358);} break;
			case 434: if (pokemon.level >= 34){pokemon.evolve(435);} break;
			case 436: if (pokemon.level >= 33){pokemon.evolve(437);} break;
			case 438: if (pokemon.knowsMove(102)){pokemon.evolve(185);} break;
			case 439: if (pokemon.knowsMove(102)){pokemon.evolve(122);} break;
			case 443: if (pokemon.level >= 24){pokemon.evolve(444);} break;
			case 444: if (pokemon.level >= 48){pokemon.evolve(445);} break;
			case 446: if (pokemon.friendship >= 220){pokemon.evolve(143);} break;
			case 447: if (pokemon.friendship >= 220 && (Main.timeOfDay() >= 600 && Main.timeOfDay() < 2000)){pokemon.evolve(448);} break;
			case 449: if (pokemon.level >= 34){pokemon.evolve(450);} break;
			case 451: if (pokemon.level >= 40){pokemon.evolve(452);} break;
			case 453: if (pokemon.level >= 37){pokemon.evolve(454);} break;
			case 456: if (pokemon.level >= 31){pokemon.evolve(457);} break;
			case 459: if (pokemon.level >= 40){pokemon.evolve(460);} break;
			case 495: if (pokemon.level >= 17){pokemon.evolve(496);} break;
			case 496: if (pokemon.level >= 36){pokemon.evolve(497);} break;
			case 498: if (pokemon.level >= 17){pokemon.evolve(499);} break;
			case 499: if (pokemon.level >= 36){pokemon.evolve(500);} break;
			case 501: if (pokemon.level >= 17){pokemon.evolve(502);} break;
			case 502: if (pokemon.level >= 36){pokemon.evolve(503);} break;
			case 504: if (pokemon.level >= 20){pokemon.evolve(505);} break;
			case 506: if (pokemon.level >= 16){pokemon.evolve(507);} break;
			case 507: if (pokemon.level >= 32){pokemon.evolve(508);} break;
			case 509: if (pokemon.level >= 20){pokemon.evolve(510);} break;
			case 519: if (pokemon.level >= 21){pokemon.evolve(520);} break;
			case 520: if (pokemon.level >= 32){pokemon.evolve(521);} break;
			case 522: if (pokemon.level >= 27){pokemon.evolve(523);} break;
			case 524: if (pokemon.level >= 25){pokemon.evolve(525);} break;
			case 527: if (pokemon.friendship >= 220){pokemon.evolve(528);} break;
			case 529: if (pokemon.level >= 31){pokemon.evolve(530);} break;
			case 532: if (pokemon.level >= 25){pokemon.evolve(533);} break;
			case 535: if (pokemon.level >= 25){pokemon.evolve(536);} break;
			case 536: if (pokemon.level >= 36){pokemon.evolve(537);} break;
			case 540: if (pokemon.level >= 20){pokemon.evolve(541);} break;
			case 541: if (pokemon.friendship >= 220){pokemon.evolve(542);} break;
			case 543: if (pokemon.level >= 22){pokemon.evolve(544);} break;
			case 544: if (pokemon.level >= 30){pokemon.evolve(545);} break;
			case 551: if (pokemon.level >= 29){pokemon.evolve(552);} break;
			case 552: if (pokemon.level >= 40){pokemon.evolve(553);} break;
			case 554: if (pokemon.level >= 35){pokemon.evolve(555);} break;
			case 557: if (pokemon.level >= 34){pokemon.evolve(558);} break;
			case 559: if (pokemon.level >= 39){pokemon.evolve(560);} break;
			case 562: if (pokemon.level >= 34){pokemon.evolve(563);} break;
			case 564: if (pokemon.level >= 37){pokemon.evolve(565);} break;
			case 566: if (pokemon.level >= 37){pokemon.evolve(567);} break;
			case 568: if (pokemon.level >= 36){pokemon.evolve(569);} break;
			case 570: if (pokemon.level >= 30){pokemon.evolve(571);} break;
			case 574: if (pokemon.level >= 32){pokemon.evolve(575);} break;
			case 575: if (pokemon.level >= 41){pokemon.evolve(576);} break;
			case 577: if (pokemon.level >= 32){pokemon.evolve(578);} break;
			case 578: if (pokemon.level >= 41){pokemon.evolve(579);} break;
			case 580: if (pokemon.level >= 35){pokemon.evolve(581);} break;
			case 582: if (pokemon.level >= 35){pokemon.evolve(583);} break;
			case 583: if (pokemon.level >= 47){pokemon.evolve(584);} break;
			case 585: if (pokemon.level >= 34){pokemon.evolve(586);} break;
			case 590: if (pokemon.level >= 39){pokemon.evolve(591);} break;
			case 592: if (pokemon.level >= 40){pokemon.evolve(593);} break;
			case 595: if (pokemon.level >= 36){pokemon.evolve(596);} break;
			case 597: if (pokemon.level >= 40){pokemon.evolve(598);} break;
			case 599: if (pokemon.level >= 38){pokemon.evolve(600);} break;
			case 600: if (pokemon.level >= 49){pokemon.evolve(601);} break;
			case 602: if (pokemon.level >= 39){pokemon.evolve(603);} break;
			case 605: if (pokemon.level >= 42){pokemon.evolve(606);} break;
			case 607: if (pokemon.level >= 41){pokemon.evolve(608);} break;
			case 610: if (pokemon.level >= 38){pokemon.evolve(611);} break;
			case 611: if (pokemon.level >= 48){pokemon.evolve(612);} break;
			case 613: if (pokemon.level >= 37){pokemon.evolve(614);} break;
			case 619: if (pokemon.level >= 50){pokemon.evolve(620);} break;
			case 622: if (pokemon.level >= 43){pokemon.evolve(623);} break;
			case 624: if (pokemon.level >= 52){pokemon.evolve(625);} break;
			case 627: if (pokemon.level >= 54){pokemon.evolve(628);} break;
			case 629: if (pokemon.level >= 54){pokemon.evolve(630);} break;
			case 633: if (pokemon.level >= 50){pokemon.evolve(634);} break;
			case 634: if (pokemon.level >= 64){pokemon.evolve(635);} break;
			case 636: if (pokemon.level >= 59){pokemon.evolve(637);} break;
			case 650: if (pokemon.level >= 16){pokemon.evolve(651);} break;
			case 651: if (pokemon.level >= 36){pokemon.evolve(652);} break;
			case 653: if (pokemon.level >= 16){pokemon.evolve(654);} break;
			case 654: if (pokemon.level >= 36){pokemon.evolve(655);} break;
			case 656: if (pokemon.level >= 16){pokemon.evolve(657);} break;
			case 657: if (pokemon.level >= 36){pokemon.evolve(658);} break;
			case 659: if (pokemon.level >= 20){pokemon.evolve(660);} break;
			case 661: if (pokemon.level >= 17){pokemon.evolve(662);} break;
			case 662: if (pokemon.level >= 35){pokemon.evolve(663);} break;
			case 664: if (pokemon.level >= 9){pokemon.evolve(665);} break;
			case 665: if (pokemon.level >= 12){pokemon.evolve(666);} break;
			case 667: if (pokemon.level >= 35){pokemon.evolve(668);} break;
			case 669: if (pokemon.level >= 19){pokemon.evolve(670);} break;
			case 672: if (pokemon.level >= 32){pokemon.evolve(673);} break;
			case 677: if (pokemon.level >= 25){pokemon.evolve(678);} break;
			case 679: if (pokemon.level >= 35){pokemon.evolve(680);} break;
			case 686: if (pokemon.level >= 30){pokemon.evolve(687);} break;
			case 688: if (pokemon.level >= 39){pokemon.evolve(689);} break;
			case 690: if (pokemon.level >= 48){pokemon.evolve(691);} break;
			case 692: if (pokemon.level >= 37){pokemon.evolve(693);} break;
			case 704: if (pokemon.level >= 40){pokemon.evolve(705);} break;
			case 712: if (pokemon.level >= 37){pokemon.evolve(713);} break;
			case 714: if (pokemon.level >= 48){pokemon.evolve(715);} break;
			case 722: if (pokemon.level >= 17){pokemon.evolve(723);} break;
			case 723: if (pokemon.level >= 34){pokemon.evolve(724);} break;
			case 725: if (pokemon.level >= 17){pokemon.evolve(726);} break;
			case 726: if (pokemon.level >= 34){pokemon.evolve(727);} break;
			case 728: if (pokemon.level >= 17){pokemon.evolve(729);} break;
			case 729: if (pokemon.level >= 34){pokemon.evolve(730);} break;
			case 731: if (pokemon.level >= 14){pokemon.evolve(732);} break;
			case 732: if (pokemon.level >= 28){pokemon.evolve(733);} break;
			case 736: if (pokemon.level >= 20){pokemon.evolve(737);} break;
			case 742: if (pokemon.level >= 25){pokemon.evolve(743);} break;
			case 747: if (pokemon.level >= 38){pokemon.evolve(748);} break;
			case 749: if (pokemon.level >= 30){pokemon.evolve(750);} break;
			case 751: if (pokemon.level >= 22){pokemon.evolve(752);} break;
			case 755: if (pokemon.level >= 24){pokemon.evolve(756);} break;
			case 759: if (pokemon.level >= 27){pokemon.evolve(760);} break;
			case 761: if (pokemon.level >= 18){pokemon.evolve(762);} break;
			case 767: if (pokemon.level >= 30){pokemon.evolve(768);} break;
			case 769: if (pokemon.level >= 42){pokemon.evolve(770);} break;
			case 772: if (pokemon.friendship >= 220){pokemon.evolve(773);} break;
			case 782: if (pokemon.level >= 35){pokemon.evolve(783);} break;
			case 783: if (pokemon.level >= 45){pokemon.evolve(784);} break;
			case 789: if (pokemon.level >= 43){pokemon.evolve(790);} break;
			case 790: if (pokemon.level >= 53){pokemon.evolve(791);} break;
			case 810: if (pokemon.level >= 16){pokemon.evolve(811);} break;
			case 811: if (pokemon.level >= 35){pokemon.evolve(812);} break;
			case 813: if (pokemon.level >= 16){pokemon.evolve(814);} break;
			case 814: if (pokemon.level >= 35){pokemon.evolve(815);} break;
			case 816: if (pokemon.level >= 16){pokemon.evolve(817);} break;
			case 817: if (pokemon.level >= 35){pokemon.evolve(818);} break;
			case 819: if (pokemon.level >= 24){pokemon.evolve(820);} break;
			case 821: if (pokemon.level >= 18){pokemon.evolve(822);} break;
			case 822: if (pokemon.level >= 38){pokemon.evolve(823);} break;
			case 824: if (pokemon.level >= 10){pokemon.evolve(825);} break;
			case 825: if (pokemon.level >= 30){pokemon.evolve(826);} break;
			case 827: if (pokemon.level >= 18){pokemon.evolve(828);} break;
			case 829: if (pokemon.level >= 20){pokemon.evolve(830);} break;
			case 831: if (pokemon.level >= 24){pokemon.evolve(832);} break;
			case 833: if (pokemon.level >= 22){pokemon.evolve(834);} break;
			case 835: if (pokemon.level >= 25){pokemon.evolve(836);} break;
			case 837: if (pokemon.level >= 18){pokemon.evolve(838);} break;
			case 838: if (pokemon.level >= 34){pokemon.evolve(839);} break;
			case 843: if (pokemon.level >= 36){pokemon.evolve(844);} break;
			case 846: if (pokemon.level >= 26){pokemon.evolve(847);} break;
			case 848: if (pokemon.level >= 30){pokemon.evolve(849);} break;
			case 850: if (pokemon.level >= 28){pokemon.evolve(851);} break;
			case 856: if (pokemon.level >= 32){pokemon.evolve(857);} break;
			case 857: if (pokemon.level >= 42){pokemon.evolve(858);} break;
			case 859: if (pokemon.level >= 32){pokemon.evolve(860);} break;
			case 860: if (pokemon.level >= 42){pokemon.evolve(861);} break;
			case 872: if (pokemon.friendship >= 220 && (Main.timeOfDay() >= 2000 || Main.timeOfDay() < 559)){pokemon.evolve(873);} break;
			case 878: if (pokemon.level >= 34){pokemon.evolve(879);} break;
			case 885: if (pokemon.level >= 50){pokemon.evolve(886);} break;
			case 886: if (pokemon.level >= 60){pokemon.evolve(887);} break;
		}
	}

}
