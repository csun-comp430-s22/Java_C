package tokenizer.tokens;

public class IntegerToken implements Token {
    public final int value;

    public IntegerToken(final int value) {
        this.value = value;
    }

    public String toString(){
        return "IntegerToken(" + value + ")";
    }
}