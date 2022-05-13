package parser.defs;

public class ParseResult <A> { //parser result
    public final A result;
    public final int offset;
    public ParseResult(final A result, final int offset) {
        this.result = result;
        this.offset = offset;
    }
}