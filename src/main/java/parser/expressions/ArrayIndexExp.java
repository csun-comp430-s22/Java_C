package parser.expressions;

import tokenizer.tokens.*;

public class ArrayIndexExp implements Expression {

    public final String name;

    public ArrayIndexExp(final String name) {
        this.name = name;
    }

    public ArrayIndexExp(IdentifierToken name) {
        this.name = name.name;
    }

    public String toString() {
        return "ArrayIndexExp(" + name + ")";
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }

}