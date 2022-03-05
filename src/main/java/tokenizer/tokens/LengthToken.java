package tokenizer.tokens;
public class LengthToken implements Token {
		public boolean equals(final Object other) {
        return other instanceof LengthToken;
    }
	public int hashCode() {
        return 15;
    }
    public String toString(){
        return "LengthToken";
    }
}