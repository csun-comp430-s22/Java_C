package tokenizer.tokens;

public class DotOperatorToken implements Token	{
	public boolean equals(final Object other) {
        return other instanceof DotOperatorToken;
    }
	public int hashCode() {
        return 2;
    }
    public String toString(){
        return "DotOperatorToken";
    }
}

