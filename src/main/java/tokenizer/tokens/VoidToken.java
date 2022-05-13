package tokenizer.tokens;

public class VoidToken implements Token {
	
	public boolean equals(final Object other) {
        return other instanceof VoidToken;
    }
	public int hashCode() {
        return 31;
    }
	
    public String toString(){
        return "VoidToken";
    }
}