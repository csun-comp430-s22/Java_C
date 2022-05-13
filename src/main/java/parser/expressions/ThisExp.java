package parser.expressions;

public class ThisExp implements Expression {
    public final Expression exp;

    public ThisExp(final Expression exp) {
        this.exp = exp;
    }
    public String toString() {
        return "ThisExp("+exp+")";
    }

    public boolean equals(final Expression exp) {
        return exp instanceof ThisExp;
    }
}
