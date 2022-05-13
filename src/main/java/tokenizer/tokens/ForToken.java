package tokenizer.tokens;

public class 	ForToken	implements Token	{
	public boolean equals(final Object other) {
        return other instanceof ForToken;
    }
	public int hashCode() {
        return 6;
    }
    public String toString(){
        return "ForToken";
    }
}