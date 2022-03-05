package tokenizer.tokens;

public class RCurlyToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof RCurlyToken;
    }
	public int hashCode() {
        return 24;
    }
    public String toString(){
        return "RCurlyToken";
    }
}