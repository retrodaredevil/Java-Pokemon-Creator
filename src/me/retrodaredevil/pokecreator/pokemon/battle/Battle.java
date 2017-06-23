package me.retrodaredevil.pokecreator.pokemon.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import me.retrodaredevil.pokecreator.Screen;
import me.retrodaredevil.pokecreator.pokemon.Pokemon;
import me.retrodaredevil.pokecreator.pokemon.attack.Attack;
import me.retrodaredevil.pokecreator.pokemon.battle.VStat.StatType;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BackgroundBubble;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.BattleAnimation;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.StatOutcome;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.PokemonPart.BattleSide;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.parts.PokemonPart.SideLocation;
import me.retrodaredevil.pokecreator.pokemon.battle.trainer.Batteler;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.AttackTurn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;
import me.retrodaredevil.pokecreator.resources.ResourcePack;
import me.retrodaredevil.pokecreator.resources.ServerType;
import me.retrodaredevil.pokecreator.util.MultiListIterator;
import me.retrodaredevil.pokecreator.util.Pending;

/**
 * @author retro
 *
 */
public class Battle implements Screen{

	private final boolean doubleBattle;

//	private List<List<BattlePokemon>> team1 = new ArrayList<>();
//	private List<List<BattlePokemon>> team2 = new ArrayList<>();
	
	private List<BBatteler> b1 = null;
	private List<BBatteler> b2 = null;
	
	private boolean over = false;
	

	private BackgroundBubble background;
	private volatile BattleAnimation battleAnimation;
	//private volatile boolean initialized = false;
	
	private String message = "A wild %p% appeared!";
	
	public Battle(boolean doubleBattle, List<Batteler> b1, List<Batteler> b2, BackgroundBubble background) {
		long start = System.currentTimeMillis();
		this.doubleBattle = doubleBattle;
		this.background = background;
		
		List<Batteler> bs = b1;
		for(int i = 0; i < bs.size(); i++){
			
			bs.get(i).setBattle(this);
			
			if(i + 1 >= bs.size() && bs == b1){
				i = -1;
				bs = b2;
			}
			
		}
		
		
		this.b1 = this.convertBattelers(b1, doubleBattle, BattleSide.TEAM1);
		this.b2 = this.convertBattelers(b2, doubleBattle, BattleSide.TEAM2);
		
		
		Iterator<BBatteler> it = this.multiListIterator();
		int size1 = 0;
		int size2 = 0;
		
		int max1 = 1;
		int max2 = 1;
		if(this.isDoubleBattle()){
			max1 = 2;
			max2 = 2;

			max1 /= this.getBattelers(BattleSide.TEAM1).size();
			max2 /= this.getBattelers(BattleSide.TEAM2).size();
		}

		
		while(it.hasNext()){
			BBatteler b = it.next();
			BattleSide side = b.getSide();
			int size = size1;
			int max = max1;
			if(side == BattleSide.TEAM2){
				size = size2;
				max = max2;
			}
			
			for(BattlePokemon p : b.getBattlePokemon()){
				if(size > max){
					p.takeBack();
					break;
				}
				SideLocation loc = p.getSideLocation();
				if(loc.isUnspecified()){
					p.takeBack();
					break;
				}
				p.sendOut(p.getSideLocation());
			
			
			}
		}
		System.out.println("Battle.java took: " + (System.currentTimeMillis() - start));
		
	}
	
	private List<BBatteler> convertBattelers(List<Batteler> bas,boolean d, BattleSide side){
		List<BBatteler> r = new ArrayList<>();
		
		int size = bas.size();
		
		for(int i = 0; i < size; i++){
			Batteler b = bas.get(i);
			
			SideLocation force = null;
			if(size > 1){
				if(i == 0){
					force = SideLocation.LEFT_DOUBLE;
				} else {
					force = SideLocation.RIGHT_DOUBLE;
					if(i > 2){
						System.out.println("Multiple pokemon's sidelocations are right. No more than two battelers should be on one team size: " + size);
					}
				}
			}
			
			r.add(new BBatteler(b, side, d, force));
		}
		return r;
	}
	public List<BattlePokemon> getPokemonOut(){
		List<BattlePokemon> r = new ArrayList<>();
		
		Iterator<BBatteler> it = this.multiListIterator();
		while(it.hasNext()){
			BBatteler b = it.next();
			for(BattlePokemon p : b.getBattlePokemon()){
				if(p.isOut()){
					r.add(p);
				}
			}
		}
		return r;
	}
	public MultiListIterator<BBatteler> multiListIterator(){
		return new MultiListIterator<BBatteler>(Arrays.asList(b1, b2));
	}
	

	/**
	 * 
	 * return the message to set. %p% is the enemy pokemon. Uses Multiple %p%s to show multiple pokemon in double battle situations.
	 */
	public String getMessage(){
		return this.message;
	}
	/**
	 * 
	 * @param message the message to set. %p% is the enemy pokemon. Use Multiple %p%s to show multiple pokemon in double battle situations.
	 */
	public void setMessage(String message){
		this.message = message;
	}
	
	/**
	 * This should only be used by a client. When called, it initializes things like images
	 * @param container
	 * @throws SlickException
	 */
	@Override
	public void init(GameContainer container) throws SlickException {
		this.battleAnimation = new BattleAnimation(this);
		battleAnimation.init(container);
		//this.initialized = true;
		System.out.println("Battle init called.");
	}
	
	@Override
	public void render(GameContainer c, Graphics g, ResourcePack currentPack) throws SlickException {
		if(battleAnimation != null){
			this.battleAnimation.render(c, g, currentPack);
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(ServerType.getType().isClient() && battleAnimation != null){
//			if(!this.initialized){ // called in ClientTrainer
//				long start = System.currentTimeMillis();
//				this.init(container);
//				System.out.println("init of Battle.java (calls BattleAnimation.java constructor): " + (System.currentTimeMillis() - start));
//			}
			battleAnimation.update(container, delta);
		}
		//System.out.println("size of team2 bbatteleres: " + this.getBattelers(BattleSide.TEAM2).size());
		
		tryturn : if(!this.isOver()){
			List<Turn> turns = null;
			BattleSide side = BattleSide.TEAM1;

			
			Iterator<BBatteler> it = this.multiListIterator();
			
			while(it.hasNext()){
				
				BBatteler b = it.next();
				//System.out.println("Side: " + side);
				//System.out.println("running through with: " + side);
				
				for(BattlePokemon p : b.getBattlePokemon()){

					Pending<Turn> t = p.getPendingTurn(b); // this method also checks to see if a move is forced
					
					if(t == null){
						t = b.getBatteler().getTurn(null, p, false);


						if(t == null){
							NullPointerException e = new NullPointerException("The pending for a turn is null. i: " + "idk" + " team: " + side + " Batteler type: " + b.getBatteler().getClass().getSimpleName());
							System.out.println("here");
							System.out.println(e.getMessage());

							throw e;
						}

						p.setPending(t); // TODO create a loop and make the get 0 thing better // I'm not really sure what this means
						//System.out.println("Set a pending. Answer: " + t.getAnswer());
					}

					if(t.hasAnswer()){
						if(turns == null){
							turns = new ArrayList<>(); // maybe save a little bit of memory and only create one if the first batteler has choosen something
						}
						//System.out.println("There's an answer.");
						turns.add(t.getAnswer());
					} else {
						break tryturn;
					}
				}
			}
			this.doTurns(turns);


			Iterator<BBatteler> it2 = this.multiListIterator();
			
			while(it2.hasNext()){
				BBatteler b = it2.next();
				
				for(BattlePokemon p : b.getBattlePokemon()){
					p.setPending(null); // if they do the turns, we don't want them to keep redoing the same turn.
					//System.out.println("Setting a pending to null...");
				}
				
				
				
				
				
			}
			
			
			
		}
		
		
	}
	
	private void doTurns(List<Turn> turns){
		List<Turn> order = new ArrayList<>();
		
		{ // order
			int low;
			List<Turn> highest = new ArrayList<>();


			while(order.size() < turns.size()){
				low = Integer.MIN_VALUE;
				for(Turn turn : turns){
					if(!order.contains(turn)){
						if(turn.getPriority() > low){
							highest.clear();
							highest.add(turn);
							low = turn.getPriority();
						} else if(turn.getPriority() == low){
							highest.add(turn);
						}
					}
				}
				order.add(highest.get((int) (highest.size() * Math.random())));
				
				
				highest.clear();
			}
		}
		
		List<TurnOutcome> outcomes = new ArrayList<>();
		for(Turn turn : order){ 
			//BBatteler b = turn.getBBattleer();
			
			BattlePokemon user = turn.getUser();
			List<BattlePokemon> team = new ArrayList<>();
			
			
			for(BBatteler bat : this.getBattelers(user.getBattleSide())){
				for(BattlePokemon p : bat.getBattlePokemon()){
					team.add(p);
				}
			}
			List<BattlePokemon> enemy = new ArrayList<>();
			
			for(BBatteler bat : this.getBattelers((user.getBattleSide() == BattleSide.TEAM1 ? BattleSide.TEAM2 : BattleSide.TEAM1))){
				for(BattlePokemon p : bat.getBattlePokemon()){
					enemy.add(p);
				}
			}
			
			System.out.println(user.getPokemon().getShortName() + " used: " + turn.getName());
			TurnOutcome outcome = turn.doTurn(user, team, enemy, this);
			
			outcomes.add(outcome);
			if(this.isOver()){
				this.endBattle();
				break;
			}
		}

		Iterator<BBatteler> it2 = this.multiListIterator();
		while(it2.hasNext()){
			BBatteler bb = it2.next();
			Batteler b = bb.getBatteler();
			for(TurnOutcome out : outcomes){
				b.tell(out, this);
			}
		}
		
	}
	private void endBattle(){ // TODO
		
	}

	public boolean isOver(){
		
		if(this.over){
			return true;
		}
		
		Iterator<BBatteler> it = this.multiListIterator();
		while(it.hasNext()){
			BBatteler b = it.next();
			if(!b.getBatteler().hasUseablePokemon()){ // TODO when double battling is a thing make it so this will only stop the battle if one side is out of usabel opkmeon
				over = true;
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public Screen getNext() {
		if(this.isOver()){
			return null;
		}
		return this;
	}
	@Override
	public void stop() {
		// TODO force a battle stop
	}



	public BattleAnimation getBattleAnimation() {
		return this.battleAnimation;
	}
	public BackgroundBubble getBackgroundType(){
		return this.background;
	}
	
	public boolean isDoubleBattle(){
		return this.doubleBattle;
	}

	/**
	 * @see #getBattelers(BattleSide)
	 * @return may not work in the future
	 */
	@Deprecated
	public List<BBatteler> getBattelers1(){
		return b1;
	}
	
	/**
	 * @see #getBattelers(BattleSide)
	 * @return may not work in the future
	 */
	@Deprecated
	public List<BBatteler> getBattelers2(){
		return b2;
	}
	public BBatteler getBBatteler(Batteler get) {
		
		if(get == null){
			System.out.println("Someone is trying to get a BBatteler froma a null Batteler. Returning null");
			return null;
		}
		
		for(BBatteler b : this.getBattelers1()){
			if(b.getBatteler() == get){
				return b;
			}
		}
		for(BBatteler b : this.getBattelers2()){
			if(b.getBatteler() == get){
				return b;
			}
		}
		
		System.out.println("returned null batteler. Probably not a good thing");
		return null;
	}
	
	/**
	 * 
	 * @param side the team or side to get
	 * @return returns the list of battelers on one team
	 */
	public List<BBatteler> getBattelers(BattleSide side){
		if(side == BattleSide.TEAM1){
			return this.getBattelers1();
		}
		return this.getBattelers2();
	}
	
	public void setOver(EndReason reason){
		this.over = true;
	}
	
	
	public static enum EndReason {
		FAINT, RUN, ERROR, UNKNOWN;
	}
	

	
	public static class BBatteler {
		
		private final Batteler b;
		private final BattleSide side;
		
		private List<BattlePokemon> mon = new ArrayList<>();
		
		private StandLocation stand = StandLocation.MIDDLE;
		
		public BBatteler (Batteler b, BattleSide side, boolean d, SideLocation force){
			this.b = b;
			this.side = side;
			
			if(force != null){
				if(force == SideLocation.LEFT_DOUBLE){
					stand = StandLocation.LEFT;
				} else {
					stand = StandLocation.RIGHT;
				}
			}
			
			List<Pokemon> mons = b.getActive();
			
			SideLocation loc = null;
			if(!d){
				loc = SideLocation.MIDDLE; // if it's not a double battle, every pokemon, if sent out, will be in the middle
			} else {
				if(force != null){
					loc = force; // force will be used if there is like a battle with four trainers where one trainer get's one side
				} else {
					loc = SideLocation.UNSPECIFIED; // this is a normal double battle
				}
			}
			
			
			
			for(int j = 0; j < mons.size(); j++){
				Pokemon p = mons.get(j);
				
				if(d && loc == SideLocation.UNSPECIFIED && j < 2){ // they are in the first 2 spots
					if(j == 0){
						loc = SideLocation.U_LEFT_DOUBLE;
					} else {
						loc = SideLocation.U_RIGHT_DOUBLE;
					}
				}
				this.mon.add(new BattlePokemon(p, loc, side));
			}
			
		}
		
	
		
		
		public List<BattlePokemon> getBattlePokemon(){
			return this.mon;
		}
		public StandLocation getStand(){
			return stand;
		}
		
		public BattleSide getSide(){
			return side;
		}
		
		public Batteler getBatteler(){
			return b;
		}
		
		
	}
	public static enum StandLocation {
		MIDDLE, LEFT, RIGHT;
	}
	
	
	public static class BattlePokemon{
		
		private List<String> tags = new ArrayList<>();
		
		private Pokemon mon;
		
		private BattlePokemon aim;
		
		private SideLocation loc;
		private boolean out;
		
		private final BattleSide side;
		
		private final VStat vStat; // final change unless not needed

		private Pending<Turn> turn;
		private boolean sayUsed = true;
		
		
		/**
		 * 
		 * @param mon the pokemon
		 * @param loc can be any SideLocation including  unspecified
		 */
		private BattlePokemon(Pokemon mon, SideLocation loc, BattleSide side){
			this.mon = mon;
			vStat = new VStat();
			
			this.loc = loc;
			this.side = side;
			
		}
		public void sendOut(SideLocation loc){
			out = true;
		}
		public boolean sayUsed(){
			return this.sayUsed;
		}
		public void setSayUsed(boolean b){
			this.sayUsed = b;
		}
		
		
		public void takeBack(){
			out = false;
			if(loc != null && !loc.isUnspecified() && loc != SideLocation.MIDDLE){
				loc = null;
			}
		}
		
		public boolean isOut(){
			return out;
		}
		
		public Pending<Turn> getPendingTurn(BBatteler b){
			if(!this.getPokemon().isFainted()){
				for(final Attack a : this.getPokemon().getAttacks()){
					if(a.forceUse(this)){
						final AttackTurn turn = new AttackTurn(a, this, b);
						return new Pending<Turn>(){
							@Override
							public Turn getAnswer() {
								return turn;
							}
						};
					}
				}
			}
			
			return this.turn;
		}
		public void setPending(Pending<Turn> turn){
			this.turn = turn;
		}
		
		public BattlePokemon getAim(){
			return aim;
		}
		public void setAim(BattlePokemon mon){
			this.aim = mon;
		}
		
		public Pokemon getPokemon(){
			return mon;
		}
		public boolean hasTag(String tag){
			return this.tags.contains(tag);
		}
		public void addTag(String tag){
			this.tags.add(tag);
		}
		public void removeTag(String tag){
			this.tags.remove(tag);
		}
//		public VStat getVStat(){
//			return vStat;
//		}



		public BattleSide getBattleSide() {
			return side;
		}
		public SideLocation getSideLocation(){
			return loc;
		}
		
		
		public int getUnmodifiedStat(StatType type) {
			return this.getPokemon().getStat(type);
		}
		public int getStat(StatType type){
			int value = this.getUnmodifiedStat(type);
			
			double mult = this.vStat.getMultiplier(type, 0);
			System.out.println("type: " + type.getName() + " mult: " + mult);
			return (int) (value * mult); // TODO
		}

		public double getAccuracyMultiplier(BattlePokemon target){
			int stage = vStat.getStage(StatType.EVASIVNESS);
			
			int acc = target.vStat.getStage(StatType.ACCURACY);
			
			double r = vStat.getMultiplier(StatType.ACCURACY, stage);
			System.out.println("r: " + r + " acc: " + acc+ " evas: " + stage);
			return r;
		}
		public List<TurnOutcome> adjustStat(StatType type, int levels, Battle battle, boolean primary) {
			List<TurnOutcome> r = new ArrayList<>();
			
			
			
			r.add((TurnOutcome)new StatOutcome(this, vStat.increase(type, levels), primary, type, levels > 0));
			
			return r;
		}
		
		
		
		
	}


	




	
}
