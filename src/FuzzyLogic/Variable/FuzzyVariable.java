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

    public String getName() {
        return name;
    }

    public Map<T, MembershipFunction> getSets() {
        return Collections.unmodifiableMap(sets);
    }
}
