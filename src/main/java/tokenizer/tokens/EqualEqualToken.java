package tokenizer.tokens;

public class EqualEqualToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof EqualEqualToken;
    }
	public int hashCode() {
        return 4;
    }
    public String toString(){
        return "EqualEqualToken";
    }
}
