package tokenizer.tokens;

public class WhileToken	implements Token {
	public boolean equals(final Object other) {
        return other instanceof WhileToken;
    }
	public int hashCode() {
        return 32;
    }
    public String toString(){
        return "WhileToken";
    }
}