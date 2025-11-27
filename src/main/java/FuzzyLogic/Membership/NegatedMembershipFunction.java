package FuzzyLogic.Membership;

public class NegatedMembershipFunction implements MembershipFunction {
    private final MembershipFunction delegate;

    public NegatedMembershipFunction(MembershipFunction delegate) {
        this.delegate = delegate;
    }

    @Override
    public double getMembership(double x) {
        return 1.0 - delegate.getMembership(x);
    }

    @Override
    public double getCentroid() {
        return delegate.getCentroid();
    }

    @Override
    public double getMeanOfMaximum() {
        return delegate.getMeanOfMaximum();
    }
}

