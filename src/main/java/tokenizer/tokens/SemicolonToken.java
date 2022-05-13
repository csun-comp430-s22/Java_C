package tokenizer.tokens;

public class SemicolonToken	implements Token {
	public boolean equals(final Object other) {
        return other instanceof SemicolonToken;
    }
	public int hashCode() {
        return 27;
    }
    public String toString(){
        return "SemicolonToken";
    }
}