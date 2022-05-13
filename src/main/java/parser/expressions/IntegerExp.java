package parser.expressions;

import tokenizer.tokens.IntegerToken;

public class IntegerExp implements Expression {
    public final int value;

    public IntegerExp(IntegerToken value) {
        this.value = value.value;
    }
    public IntegerExp(int value) {
        this.value = value;
    }

    public String toString() {
        return "IntegerExp(" + value + ")";
    }

    public boolean equals(Expression exp){
        if(exp instanceof IntegerExp){
            IntegerExp castExp = (IntegerExp) exp;
            return (castExp.value == value);
        }
        else
            return false;
    }
}