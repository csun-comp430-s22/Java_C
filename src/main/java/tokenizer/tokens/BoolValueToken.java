package tokenizer.tokens;

public class BoolValueToken implements Token {
    public final boolean value;
	
	public boolean equals(final Object other) {
        return other instanceof BoolValueToken;
    }
	public int hashCode() {
        return 34;
    }

    public BoolValueToken(final boolean value) {
        this.value = value;
    }

    public String toString(){
        return "BoolValueToken(" + value + ")";
    }
}