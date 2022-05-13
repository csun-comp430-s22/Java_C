package tokenizer.tokens;

public class EqualToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof EqualToken;
    }
	public int hashCode() {
        return 5;
    }
    public String toString(){
        return "EqualToken";
    }
}
