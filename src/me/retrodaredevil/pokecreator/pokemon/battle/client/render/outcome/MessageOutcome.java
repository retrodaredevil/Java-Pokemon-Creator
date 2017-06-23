package me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome;

import java.util.ArrayList;
import java.util.List;

import me.retrodaredevil.pokecreator.pokemon.battle.Battle;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.MessagePart;
import me.retrodaredevil.pokecreator.pokemon.battle.client.render.outcome.part.OutcomePart;
import me.retrodaredevil.pokecreator.pokemon.battle.turn.Turn.TurnOutcome;

/**
 * @author retro
 *
 */
public class MessageOutcome implements TurnOutcome{

	private final List<OutcomePart> r = new ArrayList<>();
	
	/**
	 * used if the only outcome is a message
	 */
	public MessageOutcome(Battle b, List<String> messages){
		r.add(new MessagePart(b, messages));
	}
	
	
	@Override
	public List<OutcomePart> getOutcome(Battle battle) {
		return r;
	}

}
