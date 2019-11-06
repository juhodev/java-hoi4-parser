package dev.juho.hoi4.savegame.country.data.combat;

import java.util.ArrayList;
import java.util.List;

public class CombatHistory {

	private List<CombatDataEntry> combatDataEntries;

	public CombatHistory() {
		this.combatDataEntries = new ArrayList<>();
	}

	public void add(CombatDataEntry entry) {
		combatDataEntries.add(entry);
	}

	public int getSize() {
		return combatDataEntries.size();
	}

	public String getFirstDate() {
		return combatDataEntries.get(0).getCombatData().getDate();
	}

	public String getLastDate() {
		return combatDataEntries.get(combatDataEntries.size() - 1).getCombatData().getDate();
	}

	public int getTotalManpowerLost() {
		int count = 0;

		for (CombatDataEntry entry : combatDataEntries) {
			count += entry.getCombatData().getAttacker().getManpowerLost();
			count += entry.getCombatData().getDefender().getManpowerLost();
		}

		return count;
	}

	public List<Integer> getCombatProvinces() {
		List<Integer> provinceList = new ArrayList<>();

		for (CombatDataEntry entry : combatDataEntries) {
			provinceList.add(entry.getCombatData().getProvince());
		}

		return provinceList;
	}

}
