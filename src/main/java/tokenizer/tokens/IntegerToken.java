package tokenizer.tokens;

public class IntegerToken implements Token {
    public final int value;
	
	public boolean equals(final Object other) {
        return other instanceof IntegerToken;
    }
	public int hashCode() {
        return 10;
    }

    public IntegerToken(final int value) {
        this.value = value;
    }

    public String toString(){
        return "IntegerToken(" + value + ")";
    }
}