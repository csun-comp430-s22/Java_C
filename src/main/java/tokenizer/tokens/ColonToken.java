package tokenizer.tokens;

public class ColonToken	implements Token {
	public boolean equals(final Object other) {
        return other instanceof ColonToken;
    }
	public int hashCode() {
        return 38;
    }
    public String toString(){
        return "ColonToken";
    }
}