package FuzzyLogic.Variable;

import FuzzyLogic.Membership.MembershipFunction;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class FuzzyVariable<T extends Enum<T>> {
    protected final String name;
    protected double value;
    protected final EnumMap<T, MembershipFunction> sets;

    protected FuzzyVariable(String name, Class<T> domainClass) {
        this.name = name;
        this.sets = new EnumMap<>(domainClass);
        defineMembershipFunctions();
    }

    protected abstract void defineMembershipFunctions();

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public double getMembership(T label) {
        MembershipFunction mf = sets.get(label);
        if (mf == null) {
            throw new IllegalArgumentException("Unknown linguistic term: " + label);
        }
        return mf.getMembership(value);
    }

    /**
     * Get membership value for a specific x value (for defuzzification).
     * Unlike getMembership(T), this evaluates the MF at position x instead of the stored value.
     */
    public double getMembership(T label, double x) {
        MembershipFunction mf = sets.get(label);
        if (mf == null) {
            throw new IllegalArgumentException("Unknown linguistic term: " + label);
        }
        return mf.getMembership(x);
    }

    public String getName() {
        return name;
    }

    public Map<T, MembershipFunction> getSets() {
        return Collections.unmodifiableMap(sets);
    }

    // Convenience: resolve a linguistic term name (case-insensitive) to the enum label
    // and return membership for this variable's current value.
    public double getMembership(String labelName) {
        if (labelName == null) {
            throw new IllegalArgumentException("Label name cannot be null");
        }
        for (T key : sets.keySet()) {
            if (key.name().equalsIgnoreCase(labelName) || key.toString().equalsIgnoreCase(labelName)) {
                return getMembership(key);
            }
        }
        throw new IllegalArgumentException("Unknown linguistic term: " + labelName + " for variable: " + name);
    }

    // Explicit named helper to avoid overload ambiguity from generics.
    public double getMembershipByName(String labelName) {
        return getMembership(labelName);
    }

    public double getCentroidOf(String labelName) {
        if (labelName == null) {
            throw new IllegalArgumentException("Label name cannot be null");
        }
        for (T key : sets.keySet()) {
            if (key.name().equalsIgnoreCase(labelName) || key.toString().equalsIgnoreCase(labelName)) {
                MembershipFunction mf = sets.get(key);
                if (mf == null) {
                    throw new IllegalArgumentException("No MF for label " + labelName + " on variable " + name);
                }
                return mf.getCentroid();
            }
        }
        throw new IllegalArgumentException("Unknown linguistic term: " + labelName + " for variable: " + name);
    }

    public double getMeanOfMaximumOf(String labelName) {
        if (labelName == null) {
            throw new IllegalArgumentException("Label name cannot be null");
        }
        for (T key : sets.keySet()) {
            if (key.name().equalsIgnoreCase(labelName) || key.toString().equalsIgnoreCase(labelName)) {
                MembershipFunction mf = sets.get(key);
                if (mf == null) {
                    throw new IllegalArgumentException("No MF for label " + labelName + " on variable " + name);
                }
                return mf.getMeanOfMaximum();
            }
        }
        throw new IllegalArgumentException("Unknown linguistic term: " + labelName + " for variable: " + name);
    }

    public MembershipFunction getMembershipFunction(String labelName) {
        if (labelName == null) {
            throw new IllegalArgumentException("Label name cannot be null");
        }
        for (T key : sets.keySet()) {
            if (key.name().equalsIgnoreCase(labelName) || key.toString().equalsIgnoreCase(labelName)) {
                MembershipFunction mf = sets.get(key);
                if (mf == null) {
                    throw new IllegalArgumentException("No MF for label " + labelName + " on variable " + name);
                }
                return mf;
            }
        }
        throw new IllegalArgumentException("Unknown linguistic term: " + labelName + " for variable: " + name);
    }
}
