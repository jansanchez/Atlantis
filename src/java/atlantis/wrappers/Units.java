package atlantis.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import jnibwapi.Position;
import jnibwapi.Unit;
import atlantis.util.RUtilities;

/**
 * This class is wrapper for ArrayList<Unit>. It allows some helpful methods to be executed upon group of units like
 * sorting etc.
 */
public class Units {

	private ArrayList<Unit> units = new ArrayList<>();

	/**
	 * This mapping can be used to store extra values assigned to units e.g. if units reprents mineral fields, we can
	 * easily store info how many workers are gathering each mineral field thanks to this mapping.
	 */
	private HashMap<Unit, Double> unitValues;

	// =====================================================================

	public Units() {
	}

	// =====================================================================
	// Basic functionality methods

	public Units addUnit(Unit unitToAdd) {
		units.add(unitToAdd);
		return this;
	}

	public Units addUnits(Collection<Unit> unitsToAdd) {
		units.addAll(unitsToAdd);
		return this;
	}

	public Units removeUnits(Collection<Unit> unitsToRemove) {
		units.removeAll(unitsToRemove);
		return this;
	}

	public Units removeUnit(Unit unitToRemove) {
		units.remove(unitToRemove);
		return this;
	}

	public int size() {
		return units.size();
	}

	public boolean isEmpty() {
		return units.isEmpty();
	}

	public Unit first() {
		return isEmpty() ? null : units.get(0);
	}

	public Unit get(int index) {
		return units.get(index);
	}

	// =========================================================
	// Special methods

	/**
	 * Shuffle units to have random sequence in the list.
	 */
	public Units shuffle() {
		Collections.shuffle(units);
		return this;
	}

	/**
	 * Returns random units.
	 */
	public Unit getRandom() {
		return (Unit) RUtilities.getRandomListElement(units);
	}

	/**
	 * Sorts all units according to the distance to <b>position</b>. If <b>nearestFirst</b> is true, then after sorting
	 * first unit will be the one closest to given position.
	 */
	public Units sortByDistanceTo(final Position position, final boolean nearestFirst) {
		Collections.sort(units, new Comparator<Unit>() {
			@Override
			public int compare(Unit u1, Unit u2) {
				return position.distanceTo(u1) < position.distanceTo(u2) ? (nearestFirst ? -1 : 1) : (nearestFirst ? 1
						: -1);
			}
		});

		return this;
	}

	// =========================================================
	// Value mapping methods

	public void changeValueBy(Unit unit, double deltaValue) {
		ensureValueMappingExists();
		if (unitValues.containsKey(unit)) {
			unitValues.put(unit, unitValues.get(unit) + deltaValue);
		} else {
			unitValues.put(unit, deltaValue);
		}
	}

	public void setValueFor(Unit unit, double newValue) {
		ensureValueMappingExists();
		unitValues.put(unit, newValue);
	}

	public double getValueFor(Unit unit) {
		if (unitValues == null) {
			return 0;
		} else {
			return unitValues.get(unit);
		}
	}

	public Unit getUnitWithLowestValue() {
		ensureValueMappingExists();

		if (units.isEmpty()) {
			return null;
		}

		Unit bestUnit = units.get(0);
		double bestValue = unitValues.get(bestUnit);

		for (Unit unit : units) {
			if (unitValues.get(unit) < bestValue) {
				bestValue = unitValues.get(unit);
				bestUnit = unit;
			}
		}

		return bestUnit;
	}

	private void ensureValueMappingExists() {
		unitValues = new HashMap<>();
		for (Unit unit : units) {
			unitValues.put(unit, 0.0);
		}
	}

	// =========================================================
	// Override methods

	@Override
	public String toString() {
		String string = "Units (" + units.size() + "):\n";

		for (Unit unit : units) {
			string += "   - " + unit.getType() + " (ID:" + unit.getID() + ")\n";
		}

		return string;
	}

	// =========================================================
	// Auxiliary

	public void print() {
		System.out.println("Units in list:");
		for (Unit unit : list()) {
			System.out.println(unit + " // Dist to main base: " + (unit.distanceTo(SelectUnits.mainBase())));
		}
		System.out.println();
	}

	// =========================================================
	// Getters

	/**
	 * Returns iterable collection of units in this object.
	 */
	public Collection<Unit> list() {
		ArrayList<Unit> copy = new ArrayList<Unit>();
		copy.addAll(units);
		return copy;
	}

	/**
	 * Returns iterable ArrayList of units in this object.
	 */
	public ArrayList<Unit> arrayList() {
		ArrayList<Unit> copy = new ArrayList<Unit>();
		copy.addAll(units);
		return copy;
	}

	/**
	 * @return iterator object for inner collection with the units.
	 * 
	 */
	public Iterator<Unit> iterator() {
		return units.iterator();
	}

}
