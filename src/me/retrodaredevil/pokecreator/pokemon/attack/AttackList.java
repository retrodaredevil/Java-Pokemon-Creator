package me.retrodaredevil.pokecreator.pokemon.attack;

import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackType.PHYSICAL;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackType.SPECIAL;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.AttackType.STATUS;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.Contest.BEAUTIFUL;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.Contest.CLEVER;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.Contest.COOL;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.Contest.CUTE;
import static me.retrodaredevil.pokecreator.pokemon.attack.Attack.Contest.TOUGH;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.BUG;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.DARK;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.DRAGON;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.ELECTRIC;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.FIGHTING;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.FIRE;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.FLYING;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.GHOST;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.GRASS;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.GROUND;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.ICE;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.NORMAL;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.POISON;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.ROCK;
import static me.retrodaredevil.pokecreator.pokemon.type.Type.WATER;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.Battle.BattlePokemon;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.MessageOutcome;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.DamageUpdate;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.FaintUpdate;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.pokemon.type.Type;


/**
 * @author retro
 *
 */
public class AttackList {
	public static final Attack POUND = new Attack(40, 100, NORMAL, "Pound", PHYSICAL, 35, TOUGH, -1);//
	public static final Attack KARATECHOP = new Attack(50, 100, FIGHTING, "Karate Chop", PHYSICAL, 25, TOUGH, 1){//
		@Override
		public boolean hasHighCriticalHit() {
			return true;
		}
	};
	public static final Attack DOUBLESLAP = new Attack(15, 85, NORMAL, "Double Slap", PHYSICAL, 10, CUTE, 5);
	public static final Attack COMETPUNCH = new Attack(18, 85, NORMAL, "Comet Punch", PHYSICAL, 15, TOUGH, 5);
	public static final Attack MEGAPUNCH = new Attack(80, 85, NORMAL, "Mega Punch", PHYSICAL, 20, TOUGH, -1);//
	public static final Attack PAYDAY = new Attack(40, 100, NORMAL, "Pay Day", PHYSICAL, 20, CLEVER, -1);//
	public static final Attack FIREPUNCH = new Attack(75, 100, FIRE, "Fire Punch", PHYSICAL, 15, TOUGH, -1);
	public static final Attack ICEPUNCH = new Attack(75, 100, ICE, "Ice Punch", PHYSICAL, 15, BEAUTIFUL, -1);
	public static final Attack THUNDERPUNCH = new Attack(75, 100, ELECTRIC, "Thunder Punch", PHYSICAL, 15, COOL, -1);
	public static final Attack SCRATCH = new Attack(40, 100, NORMAL, "Scratch", PHYSICAL, 35, TOUGH, -1);
	public static final Attack VICEGRIP = new Attack(55, 100, NORMAL, "Vice Grip", PHYSICAL, 30, TOUGH, -1);
	public static final Attack GUILLOTINE = new Attack(0, 0, NORMAL, "Guillotine", PHYSICAL, 5, COOL, -1);
	public static final Attack RAZORWIND = new Attack(80, 100, NORMAL, "Razor Wind", SPECIAL, 10, COOL, -1);
	public static final Attack SWORDSDANCE = new Attack(0, 0, NORMAL, "Swords Dance", STATUS, 20, BEAUTIFUL, -1);
	public static final Attack CUT = new Attack(50, 95, NORMAL, "Cut", PHYSICAL, 30, COOL, -1);
	public static final Attack GUST = new Attack(40, 100, FLYING, "Gust", SPECIAL, 35, CLEVER, -1);
	public static final Attack WINGATTACK = new Attack(60, 100, FLYING, "Wing Attack", PHYSICAL, 35, COOL, -1);
	public static final Attack WHIRLWIND = new Attack(0, 0, NORMAL, "Whirlwind", STATUS, 20, CLEVER, -1);
	public static final Attack FLY = new Attack(90, 95, FLYING, "Fly", PHYSICAL, 15, CLEVER, -1);
	public static final Attack BIND = new Attack(15, 85, NORMAL, "Bind", PHYSICAL, 20, TOUGH, -1);
	public static final Attack SLAM = new Attack(80, 75, NORMAL, "Slam", PHYSICAL, 20, TOUGH, -1);
	public static final Attack VINEWHIP = new Attack(45, 100, GRASS, "Vine Whip", PHYSICAL, 25, COOL, -1);
	public static final Attack STOMP = new Attack(65, 100, NORMAL, "Stomp", PHYSICAL, 20, TOUGH, -1);
	public static final Attack DOUBLEKICK = new Attack(30, 100, FIGHTING, "Double Kick", PHYSICAL, 30, COOL, -1);
	public static final Attack MEGAKICK = new Attack(120, 75, NORMAL, "Mega Kick", PHYSICAL, 5, COOL, -1);
	public static final Attack JUMPKICK = new Attack(100, 95, FIGHTING, "Jump Kick", PHYSICAL, 10, COOL, -1);
	public static final Attack ROLLINGKICK = new Attack(60, 85, FIGHTING, "Rolling Kick", PHYSICAL, 15, COOL, -1);
	public static final Attack SANDATTACK = new StatusAttack(0, 100, GROUND, "Sand Attack", STATUS, 15, CUTE, -1, StatType.ACCURACY, -1);
	public static final Attack HEADBUTT = new Attack(70, 100, NORMAL, "Headbutt", PHYSICAL, 15, TOUGH, -1);
	public static final Attack HORNATTACK = new Attack(65, 100, NORMAL, "Horn Attack", PHYSICAL, 25, COOL, -1);
	public static final Attack FURYATTACK = new Attack(15, 85, NORMAL, "Fury Attack", PHYSICAL, 20, COOL, -1);
	public static final Attack HORNDRILL = new Attack(0, 0, NORMAL, "Horn Drill", PHYSICAL, 5, COOL, -1);
	public static final Attack TACKLE = new Attack(50, 100, NORMAL, "Tackle", PHYSICAL, 35, TOUGH, -1);
	public static final Attack BODYSLAM = new Attack(85, 100, NORMAL, "Body Slam", PHYSICAL, 15, TOUGH, -1);
	public static final Attack WRAP = new Attack(15, 90, NORMAL, "Wrap", PHYSICAL, 20, TOUGH, -1);
	public static final Attack TAKEDOWN = new Attack(90, 85, NORMAL, "Take Down", PHYSICAL, 20, TOUGH, -1);
	public static final Attack THRASH = new Attack(120, 100, NORMAL, "Thrash", PHYSICAL, 10, TOUGH, -1);
	public static final Attack DOUBLEEDGE = new RecoilAttack(120, 100, NORMAL, "Double-Edge", PHYSICAL, 15, TOUGH, -1, 1/3.0);
	public static final Attack TAILWHIP = new Attack(0, 100, NORMAL, "Tail Whip", STATUS, 30, CUTE, -1);
	public static final Attack POISONSTING = new Attack(15, 100, POISON, "Poison Sting", PHYSICAL, 35, CLEVER, -1);
	public static final Attack TWINEEDLE = new Attack(25, 100, BUG, "Twineedle", PHYSICAL, 20, COOL, -1);
	public static final Attack PINMISSILE = new Attack(25, 95, BUG, "Pin Missile", PHYSICAL, 20, COOL, -1);
	public static final Attack LEER = new StatusAttack(0, 100, NORMAL, "Leer", STATUS, 30, COOL, -1, StatType.DEFENSE, -1);
	public static final Attack BITE = new Attack(60, 100, DARK, "Bite", PHYSICAL, 25, TOUGH, -1);
	public static final Attack GROWL = new Attack(0, 100, NORMAL, "Growl", STATUS, 40, CUTE, -1);
	public static final Attack ROAR = new Attack(0, 0, NORMAL, "Roar", STATUS, 20, COOL, -1);
	public static final Attack SING = new Attack(0, 55, NORMAL, "Sing", STATUS, 15, CUTE, -1);
	public static final Attack SUPERSONIC = new Attack(0, 55, NORMAL, "Supersonic", STATUS, 20, CLEVER, -1);
	public static final Attack SONICBOOM = new Attack(0, 90, NORMAL, "Sonic Boom", SPECIAL, 20, COOL, -1);
	public static final Attack DISABLE = new Attack(0, 100, NORMAL, "Disable", STATUS, 20, CLEVER, -1);
	public static final Attack ACID = new Attack(40, 100, POISON, "Acid", SPECIAL, 30, CLEVER, -1);
	public static final Attack EMBER = new Attack(40, 100, FIRE, "Ember", SPECIAL, 25, CUTE, -1);
	public static final Attack FLAMETHROWER = new Attack(90, 100, FIRE, "Flamethrower", SPECIAL, 15, BEAUTIFUL, -1);
	public static final Attack MIST = new Attack(0, 0, ICE, "Mist", STATUS, 30, BEAUTIFUL, -1);
	public static final Attack WATERGUN = new Attack(40, 100, WATER, "Water Gun", SPECIAL, 25, CUTE, -1);
	public static final Attack HYDROPUMP = new Attack(110, 80, WATER, "Hydro Pump", SPECIAL, 5, BEAUTIFUL, -1);
	public static final Attack SURF = new Attack(90, 100, WATER, "Surf", SPECIAL, 15, BEAUTIFUL, -1);
	public static final Attack ICEBEAM = new Attack(90, 100, ICE, "Ice Beam", SPECIAL, 10, BEAUTIFUL, -1);
	public static final Attack BLIZZARD = new Attack(110, 70, ICE, "Blizzard", SPECIAL, 5, BEAUTIFUL, -1);
	public static final Attack PSYBEAM = new Attack(65, 100, Type.PSYCHIC, "Psybeam", SPECIAL, 20, BEAUTIFUL, -1);
	public static final Attack BUBBLEBEAM = new Attack(65, 100, WATER, "Bubble Beam", SPECIAL, 20, BEAUTIFUL, -1);
	public static final Attack AURORABEAM = new Attack(65, 100, ICE, "Aurora Beam", SPECIAL, 20, BEAUTIFUL, -1);
	public static final Attack HYPERBEAM = new Attack(150, 90, NORMAL, "Hyper Beam", SPECIAL, 5, COOL, -1){
		
		final String tag = "hyper-beam-recharge";
		
		@Override
		public List<TurnOutcome> attack(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle) {
			
			if(user.hasTag(tag)){
				user.removeTag(tag);
				System.out.println("removing tag because user is set to recharge.");
				user.setSayUsed(false);
				return Arrays.asList(new MessageOutcome(battle, Arrays.asList(user.getPokemon().getShortName() + " must recharge.")));	
				
			}
			user.setSayUsed(true);
			
			List<TurnOutcome> r = super.attack(user, team, enemy, battle);
			for(TurnOutcome t : r){
				if(t instanceof AttackOutcome){
					AttackOutcome a = (AttackOutcome) t;
					if(!a.missed()){
						user.addTag(tag);
						System.out.println("Adding tag because user will have to recharge next turn");
						break;
					}
				}
			}
			return r;
		}
		@Override
		public boolean forceUse(BattlePokemon mon) {
			return mon.hasTag(tag);
		}
	};
	public static final Attack PECK = new Attack(35, 100, FLYING, "Peck", PHYSICAL, 35, COOL, -1);
	public static final Attack DRILLPECK = new Attack(80, 100, FLYING, "Drill Peck", PHYSICAL, 20, COOL, -1);
	public static final Attack SUBMISSION = new Attack(80, 80, FIGHTING, "Submission", PHYSICAL, 25, COOL, -1);
	public static final Attack LOWKICK = new Attack(0, 100, FIGHTING, "Low Kick", PHYSICAL, 20, TOUGH, -1);
	public static final Attack COUNTER = new Attack(0, 100, FIGHTING, "Counter", PHYSICAL, 20, TOUGH, -1);
	public static final Attack SEISMICTOSS = new Attack(0, 100, FIGHTING, "Seismic Toss", PHYSICAL, 20, TOUGH, -1);
	public static final Attack STRENGTH = new Attack(80, 100, NORMAL, "Strength", PHYSICAL, 15, TOUGH, -1);
	public static final Attack ABSORB = new Attack(20, 100, GRASS, "Absorb", SPECIAL, 25, CLEVER, -1);
	public static final Attack MEGADRAIN = new Attack(40, 100, GRASS, "Mega Drain", SPECIAL, 15, CLEVER, -1);
	public static final Attack LEECHSEED = new Attack(0, 90, GRASS, "Leech Seed", STATUS, 10, CLEVER, -1);
	public static final Attack GROWTH = new Attack(0, 0, NORMAL, "Growth", STATUS, 20, BEAUTIFUL, -1);
	public static final Attack RAZORLEAF = new Attack(55, 95, GRASS, "Razor Leaf", PHYSICAL, 25, COOL, -1);
	public static final Attack SOLARBEAM = new Attack(120, 100, GRASS, "Solar Beam", SPECIAL, 10, COOL, -1){
		final String tag = "solar-beam-charged";
		public List<TurnOutcome> attack(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, Battle battle) {
			if(user.hasTag(tag)){
				user.removeTag(tag);
				return super.attack(user, team, enemy, battle);
			}
			
			user.addTag(tag);
			return Arrays.asList(new MessageOutcome(battle, Arrays.asList(user.getPokemon().getShortName() + " took in sunlight.")));			
		}
		public boolean forceUse(BattlePokemon mon) {
			return mon.hasTag(tag);
		};
		protected List<TurnOutcome> onAttack(BattlePokemon attacker, BattlePokemon defender, AttackOutcome outcome, Battle battle, int hitNumber, int maxHits) {
			
			
			String type1 = attacker.getPokemon().getPokemonType().getName();
			String type2 = defender.getPokemon().getPokemonType().getName();
			//System.out.println("Called onAttack type1: " + type1 + " type2: "+ type2);
			
			if(type1.equalsIgnoreCase("venusaur") && type2.equalsIgnoreCase("pikachu")){
				List<TurnOutcome> r = new ArrayList<>();
				attacker.setSayUsed(false);

				//r.add(new MessageOutcome(battle, Arrays.asList(attacker.getPokemon().getShortName() + " used " + getName())));
				r.add(new MessageOutcome(battle, Arrays.asList("I don't even need you're permission for this.")));
				List<TurnOutcome> s = super.onAttack(attacker, defender, outcome, battle, hitNumber, maxHits);
				if(s != null){
					r.addAll(s);
				}
				if(!defender.getPokemon().isFainted()){
					r.add(new MessageOutcome(battle, Arrays.asList(defender.getPokemon().getShortName() + ": Ahh my knee! Stupid! What do you even do? You're just a plant!")));
				}
				return r;
				
			} 
			
			return super.onAttack(attacker, defender, outcome, battle, hitNumber, maxHits);
			
			
		}
		
	};
	public static final Attack POISONPOWDER = new Attack(0, 75, POISON, "Poison Powder", STATUS, 35, CLEVER, -1);
	public static final Attack STUNSPORE = new Attack(0, 75, GRASS, "Stun Spore", STATUS, 30, CLEVER, -1);
	public static final Attack SLEEPPOWDER = new Attack(0, 75, GRASS, "Sleep Powder", STATUS, 15, CLEVER, -1);
	public static final Attack PETALDANCE = new Attack(120, 100, GRASS, "Petal Dance", SPECIAL, 10, BEAUTIFUL, -1);
	public static final Attack STRINGSHOT = new Attack(0, 95, BUG, "String Shot", STATUS, 40, CLEVER, -1);
	public static final Attack DRAGONRAGE = new Attack(0, 100, DRAGON, "Dragon Rage", SPECIAL, 10, COOL, -1);
	public static final Attack FIRESPIN = new Attack(35, 85, FIRE, "Fire Spin", SPECIAL, 15, BEAUTIFUL, -1);
	public static final Attack THUNDERSHOCK = new Attack(40, 100, ELECTRIC, "Thunder Shock", SPECIAL, 30, COOL, -1);
	public static final Attack THUNDERBOLT = new Attack(90, 100, ELECTRIC, "Thunderbolt", SPECIAL, 15, COOL, -1);
	public static final Attack THUNDERWAVE = new Attack(0, 100, ELECTRIC, "Thunder Wave", STATUS, 20, COOL, -1);
	public static final Attack THUNDER = new Attack(110, 70, ELECTRIC, "Thunder", SPECIAL, 10, COOL, -1);
	public static final Attack ROCKTHROW = new Attack(50, 90, ROCK, "Rock Throw", PHYSICAL, 15, TOUGH, -1);
	public static final Attack EARTHQUAKE = new Attack(100, 100, GROUND, "Earthquake", PHYSICAL, 10, TOUGH, -1);
	public static final Attack FISSURE = new Attack(0, 0, GROUND, "Fissure", PHYSICAL, 5, TOUGH, -1);
	public static final Attack DIG = new Attack(80, 100, GROUND, "Dig", PHYSICAL, 10, TOUGH, -1);
	public static final Attack TOXIC = new Attack(0, 90, POISON, "Toxic", STATUS, 10, CLEVER, -1);
	public static final Attack CONFUSION = new Attack(50, 100, Type.PSYCHIC, "Confusion", SPECIAL, 25, CLEVER, -1);
	public static final Attack PSYCHIC = new Attack(90, 100, Type.PSYCHIC, "Psychic", SPECIAL, 10, CLEVER, -1);
	public static final Attack HYPNOSIS = new Attack(0, 60, Type.PSYCHIC, "Hypnosis", STATUS, 20, CLEVER, -1);
	public static final Attack MEDITATE = new Attack(0, 0, Type.PSYCHIC, "Meditate", STATUS, 40, BEAUTIFUL, -1);
	public static final Attack AGILITY = new StatusSelfAttack(0, 0, Type.PSYCHIC, "Agility", STATUS, 30, COOL, -1, StatType.SPEED, 2);
	public static final Attack QUICKATTACK = new Attack(40, 100, NORMAL, "Quick Attack", PHYSICAL, 30, COOL, -1){
		public int getPriority() {
			return 1;
		};
	};
	public static final Attack RAGE = new Attack(20, 100, NORMAL, "Rage", PHYSICAL, 20, TOUGH, -1);
	public static final Attack TELEPORT = new Attack(0, 0, Type.PSYCHIC, "Teleport", STATUS, 20, COOL, -1);
	public static final Attack NIGHTSHADE = new Attack(0, 100, GHOST, "Night Shade", SPECIAL, 15, CLEVER, -1);
	public static final Attack MIMIC = new Attack(0, 100, NORMAL, "Mimic", STATUS, 10, CUTE, -1);
	public static final Attack SCREECH = new Attack(0, 85, NORMAL, "Screech", STATUS, 40, CLEVER, -1);
	public static final Attack DOUBLETEAM = new Attack(0, 0, NORMAL, "Double Team", STATUS, 15, COOL, -1);
	public static final Attack RECOVER = new Attack(0, 0, NORMAL, "Recover", STATUS, 10, CLEVER, -1);
	public static final Attack HARDEN = new Attack(0, 0, NORMAL, "Harden", STATUS, 30, TOUGH, -1);
	public static final Attack MINIMIZE = new Attack(0, 0, NORMAL, "Minimize", STATUS, 10, CUTE, -1);
	public static final Attack SMOKESCREEN = new Attack(0, 100, NORMAL, "Smokescreen", STATUS, 20, CLEVER, -1);
	public static final Attack CONFUSERAY = new Attack(0, 100, GHOST, "Confuse Ray", STATUS, 10, CLEVER, -1);
	public static final Attack WITHDRAW = new Attack(0, 0, WATER, "Withdraw", STATUS, 40, CUTE, -1);
	public static final Attack DEFENSECURL = new Attack(0, 0, NORMAL, "Defense Curl", STATUS, 40, CUTE, -1);
	public static final Attack BARRIER = new StatusSelfAttack(0, 0, Type.PSYCHIC, "Barrier", STATUS, 20, COOL, -1, StatType.DEFENSE, 2);
	public static final Attack LIGHTSCREEN = new Attack(0, 0, Type.PSYCHIC, "Light Screen", STATUS, 30, BEAUTIFUL, -1);
	public static final Attack HAZE = new Attack(0, 0, ICE, "Haze", STATUS, 30, BEAUTIFUL, -1);
	public static final Attack REFLECT = new Attack(0, 0, Type.PSYCHIC, "Reflect", STATUS, 20, CLEVER, -1);
	public static final Attack FOCUSENERGY = new Attack(0, 0, NORMAL, "Focus Energy", STATUS, 30, COOL, -1);
	public static final Attack BIDE = new Attack(0, 100, NORMAL, "Bide", PHYSICAL, 10, TOUGH, -1);
	public static final Attack METRONOME = new Attack(0, 0, NORMAL, "Metronome", STATUS, 10, CUTE, -1);
	public static final Attack MIRRORMOVE = new Attack(0, 0, FLYING, "Mirror Move", STATUS, 20, CLEVER, -1);
	public static final Attack SELFDESTRUCT = new Attack(200, 100, NORMAL, "Self-Destruct", PHYSICAL, 5, BEAUTIFUL, -1){
		protected List<TurnOutcome> afterAttacks(BattlePokemon user, List<BattlePokemon> team, List<BattlePokemon> enemy, List<BattlePokemon> targets, List<TurnOutcome> currentOutcomes) {
			Pokemon attacker = user.getPokemon();
			attacker.damage(attacker.getMaxHP());
			
			List<TurnOutcome> r = new ArrayList<>();
			r.add(new TurnOutcome(){
				@Override
				public List<OutcomePart> getOutcome(Battle battle) {
					List<OutcomePart> r = new ArrayList<>();
					r.add(new DamageUpdate(battle, user, 0));
					r.add(new FaintUpdate(battle, user));
					return r;
				}
			});
			
			return r;
		}
	};
	public static final Attack EGGBOMB = new Attack(100, 75, NORMAL, "Egg Bomb", PHYSICAL, 10, CUTE, -1);
	public static final Attack LICK = new Attack(30, 100, GHOST, "Lick", PHYSICAL, 30, CUTE, -1);
	public static final Attack SMOG = new Attack(30, 70, POISON, "Smog", SPECIAL, 20, TOUGH, -1);
	public static final Attack SLUDGE = new Attack(65, 100, POISON, "Sludge", SPECIAL, 20, TOUGH, -1);
	public static final Attack BONECLUB = new Attack(65, 85, GROUND, "Bone Club", PHYSICAL, 20, TOUGH, -1);
	public static final Attack FIREBLAST = new Attack(110, 85, FIRE, "Fire Blast", SPECIAL, 5, BEAUTIFUL, -1);
	public static final Attack WATERFALL = new Attack(80, 100, WATER, "Waterfall", PHYSICAL, 15, TOUGH, -1);
	public static final Attack CLAMP = new Attack(35, 85, WATER, "Clamp", PHYSICAL, 15, TOUGH, -1);
	public static final Attack SWIFT = new Attack(60, 0, NORMAL, "Swift", SPECIAL, 20, COOL, -1);
	public static final Attack SKULLBASH = new Attack(130, 100, NORMAL, "Skull Bash", PHYSICAL, 10, TOUGH, -1);
	public static final Attack SPIKECANNON = new Attack(20, 100, NORMAL, "Spike Cannon", PHYSICAL, 15, COOL, -1);
	public static final Attack CONSTRICT = new Attack(10, 100, NORMAL, "Constrict", PHYSICAL, 35, TOUGH, -1);
	public static final Attack AMNESIA = new Attack(0, 0, Type.PSYCHIC, "Amnesia", STATUS, 20, CUTE, -1);
	public static final Attack KINESIS = new Attack(0, 80, Type.PSYCHIC, "Kinesis", STATUS, 15, CLEVER, -1);
	public static final Attack SOFTBOILED = new Attack(0, 0, NORMAL, "Soft-Boiled", STATUS, 10, CUTE, -1);
	public static final Attack HIGHJUMPKICK = new Attack(130, 90, FIGHTING, "High Jump Kick", PHYSICAL, 10, COOL, -1);
	public static final Attack GLARE = new Attack(0, 100, NORMAL, "Glare", STATUS, 30, TOUGH, -1);
	public static final Attack DREAMEATER = new Attack(100, 100, Type.PSYCHIC, "Dream Eater", SPECIAL, 15, CLEVER, -1);
	public static final Attack POISONGAS = new Attack(0, 90, POISON, "Poison Gas", STATUS, 40, CLEVER, -1);
	public static final Attack BARRAGE = new Attack(15, 85, NORMAL, "Barrage", PHYSICAL, 20, CUTE, -1);
	public static final Attack LEECHLIFE = new Attack(20, 100, BUG, "Leech Life", PHYSICAL, 15, CLEVER, -1);
	public static final Attack LOVELYKISS = new Attack(0, 75, NORMAL, "Lovely Kiss", STATUS, 10, BEAUTIFUL, -1);
	public static final Attack SKYATTACK = new Attack(140, 90, FLYING, "Sky Attack", PHYSICAL, 5, COOL, -1);
	public static final Attack TRANSFORM = new Attack(0, 0, NORMAL, "Transform", STATUS, 10, CLEVER, -1);
	public static final Attack BUBBLE = new Attack(40, 100, WATER, "Bubble", SPECIAL, 30, CUTE, -1);
	public static final Attack DIZZYPUNCH = new Attack(70, 100, NORMAL, "Dizzy Punch", PHYSICAL, 10, CUTE, -1);
	public static final Attack SPORE = new Attack(0, 100, GRASS, "Spore", STATUS, 15, BEAUTIFUL, -1);
	public static final Attack FLASH = new Attack(0, 100, NORMAL, "Flash", STATUS, 20, BEAUTIFUL, -1);
	public static final Attack PSYWAVE = new Attack(0, 100, Type.PSYCHIC, "Psywave", SPECIAL, 15, CLEVER, -1);
	public static final Attack SPLASH = new Attack(0, 0, NORMAL, "Splash", STATUS, 40, CUTE, -1);
	public static final Attack ACIDARMOR = new Attack(0, 0, POISON, "Acid Armor", STATUS, 20, TOUGH, -1);
	public static final Attack CRABHAMMER = new Attack(100, 90, WATER, "Crabhammer", PHYSICAL, 10, TOUGH, -1);
	public static final Attack EXPLOSION = new Attack(250, 100, NORMAL, "Explosion", PHYSICAL, 5, BEAUTIFUL, -1);
	public static final Attack FURYSWIPES = new Attack(18, 80, NORMAL, "Fury Swipes", PHYSICAL, 15, TOUGH, 5);
	public static final Attack BONEMERANG = new Attack(50, 90, GROUND, "Bonemerang", PHYSICAL, 10, TOUGH, -1);
	public static final Attack REST = new Attack(0, 0, Type.PSYCHIC, "Rest", STATUS, 10, CUTE, -1);
	public static final Attack ROCKSLIDE = new Attack(75, 90, ROCK, "Rock Slide", PHYSICAL, 10, TOUGH, -1);
	public static final Attack HYPERFANG = new Attack(80, 90, NORMAL, "Hyper Fang", PHYSICAL, 15, COOL, -1);
	public static final Attack SHARPEN = new Attack(0, 0, NORMAL, "Sharpen", STATUS, 30, CUTE, -1);
	public static final Attack CONVERSION = new Attack(0, 0, NORMAL, "Conversion", STATUS, 30, BEAUTIFUL, -1);
	public static final Attack TRIATTACK = new Attack(80, 100, NORMAL, "Tri Attack", SPECIAL, 10, BEAUTIFUL, -1);
	public static final Attack SUPERFANG = new Attack(0, 90, NORMAL, "Super Fang", PHYSICAL, 10, TOUGH, -1);
	public static final Attack SLASH = new Attack(70, 100, NORMAL, "Slash", PHYSICAL, 20, COOL, -1);
	public static final Attack SUBSTITUTE = new Attack(0, 0, NORMAL, "Substitute", STATUS, 10, CUTE, -1);
	public static final Attack STRUGGLE = new Attack(50, 100, NORMAL, "Struggle", PHYSICAL, 1, TOUGH, -1); // acc was at 0 for whatever reason. Changed to 100
	
	
	public static Attack getAttack(String name){
		name = name.toLowerCase();
		name = name.replaceAll(" ", "");
		name = name.replaceAll("-", "");
		name = name.replaceAll("_", "");
		
		
		for(Attack a: getAttacks()){
			String aname = a.getName();
			aname = aname.toLowerCase();
			aname = aname.replaceAll(" ", "");
			aname = aname.replaceAll("-", "");
			aname = aname.replaceAll("_", "");
			if(name.equals(aname)){
				return a;
			}
		}
		
		return null;
	}
	
	public static List<Attack> getAttacks(){
		Field[] fields = AttackList.class.getDeclaredFields();
		
		List<Attack> r = new ArrayList<>();
		
		for(Field f : fields){
			if(!Modifier.isStatic(f.getModifiers())){
				continue;
			}
			Object o = null;
			
			try {
				o = f.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if(o instanceof Attack){
				r.add((Attack) o);
			}
		}
		
		return r;
		
	}
	
	
}
