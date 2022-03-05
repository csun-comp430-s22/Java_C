package tokenizer.tokens;

public class BoolValueToken implements Token {
    public final boolean value;

    public BoolValueToken(final boolean value) {
        this.value = value;
    }

    public String toString(){
        return "BoolValueToken(" + value + ")";
    }
}