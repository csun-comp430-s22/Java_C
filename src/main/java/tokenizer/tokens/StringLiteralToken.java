package tokenizer.tokens;

public class StringLiteralToken implements Token{
    public final String string;

    public StringLiteralToken(final String string){
        this.string = string;
    }
    public String toString(){
        return "StringLiteralToken(" + string + ")";
    }
}