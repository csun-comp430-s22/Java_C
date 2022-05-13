package tokenizer.tokens;

public class ReturnToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof ReturnToken;
    }
	public int hashCode() {
        return 25;
    }
    public String toString(){
        return "ReturnToken";
    }
}