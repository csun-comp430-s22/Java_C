package parser.expressions;

public class LessThanOrEqualExp implements Expression {

    public final Expression exp1;
    public final Expression exp2;

    public LessThanOrEqualExp(final Expression exp1, final Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public String toString() {
        return "LessThanOrEqualExp(" + exp1 + ", " + exp2 + ")";
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }

}