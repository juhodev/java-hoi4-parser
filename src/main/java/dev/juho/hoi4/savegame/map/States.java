package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.nodes.BooleanNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.StringNode;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class States {

	private List<State> stateList;

	public States() {
		this.stateList = new ArrayList<>();
	}

	public void build(ObjectNode statesNode) {
		Iterator it = statesNode.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			String key = (String) pair.getKey();
			ObjectNode obj = (ObjectNode) pair.getValue();

			State state = new State();
			state.setId(Integer.parseInt(key));

			if (obj.has("controller")) {
				StringNode controllerNode = (StringNode) obj.get("controller");
				String tagString = controllerNode.getValue();

				if (Utils.hasEnum(CountryTag.values(), tagString)) {
					state.setController(CountryTag.valueOf(tagString));
				} else {
					Logger.getInstance().log(Logger.ERROR, "couldn't find country tag " + tagString);
				}
			}

			if (obj.has("owner")) {
				StringNode ownerNode = (StringNode) obj.get("owner");
				String tagString = ownerNode.getValue();

				if (Utils.hasEnum(CountryTag.values(), tagString)) {
					state.setOwner(CountryTag.valueOf(tagString));
				} else {
					Logger.getInstance().log(Logger.ERROR, "couldn't find country tag " + tagString);
				}
			}

			if (obj.has("manpower_pool")) {
				readManpowerPool((ObjectNode) obj.get("manpower_pool"), state);
			}

			if (obj.has("demilitarized")) {
				BooleanNode demilitarizedNode = (BooleanNode) obj.get("demilitarized");
				state.setDemilitarized(demilitarizedNode.getValue());
			}

			if (obj.has("is_border_conflict")) {
				BooleanNode isBorderConflictNode = (BooleanNode) obj.get("is_border_conflict");
				state.setBorderConflict(isBorderConflictNode.getValue());
			}

			if (obj.has("state_category")) {
				StringNode stateCategoryNode = (StringNode) obj.get("state_category");
				state.setStateCategory(stateCategoryNode.getValue());
			}

			stateList.add(state);
		}
	}

	public List<State> getStateList() {
		return stateList;
	}

	private void readManpowerPool(ObjectNode manpowerPool, State state) {
		IntegerNode availableNode = (IntegerNode) manpowerPool.get("available");
		IntegerNode lockedNode = (IntegerNode) manpowerPool.get("locked");
		IntegerNode totalNode = (IntegerNode) manpowerPool.get("total");

		State.ManpowerPool pool = new State.ManpowerPool();
		pool.available = availableNode.getValue();
		pool.locked = lockedNode.getValue();
		pool.total = totalNode.getValue();
		state.setManpowerPool(pool);
	}
}
