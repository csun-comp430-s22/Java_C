package tokenizer.tokens;

public class LCurlyToken implements Token {
		public boolean equals(final Object other) {
        return other instanceof LCurlyToken;
    }
	public int hashCode() {
        return 13;
    }
    public String toString(){
        return "LCurlyToken";
    }
}