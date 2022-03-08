package tokenizer;

import java.util.List;
import java.util.ArrayList;
import tokenizer.tokens.*;

public class Tokenizer {
    private final String input;
    private int offset;

    public Tokenizer(final String input) {
        this.input = input;
        offset = 0;
    }

    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhitespace();
        if (offset < input.length()) {
            retval = tryTokenizeVariableOrKeyword();
            if (retval == null) {
                if (input.startsWith("(", offset)) {
                    offset += 1;
                    retval = new LeftParenToken();
                } else if (input.startsWith(")", offset)) {
                    offset += 1;
                    retval = new RightParenToken();
                } else if (input.startsWith("{", offset)) {
                    offset += 1;
                    retval = new LCurlyToken();
                } else if (input.startsWith("}", offset)) {
                    offset += 1;
                    retval = new RCurlyToken();
                } else if (input.startsWith("[", offset)) {
                    offset += 1;
                    retval = new LBracketToken();
                } else if (input.startsWith("]", offset)) {
                    offset += 1;
                    retval = new RBracketToken();
                } else if (input.startsWith(";", offset)) {
                    offset += 1;
                    retval = new SemicolonToken();
                } else if (input.startsWith(".", offset)) {
                    offset += 1;
                    retval = new DotOperatorToken();
                } else if (input.startsWith(",", offset)) {
                    offset += 1;
                    retval = new CommaToken();
                } else if (input.startsWith(":", offset)) {
                    offset += 1;
                    retval = new ColonToken();
                } else if (input.startsWith("+", offset)) {
                    offset += 1;
                    retval = new PlusToken();
                } else if (input.startsWith("-", offset)) {
                    offset += 1;
                    retval = new MinusToken();
                } else if (input.startsWith("*", offset)) {
                    offset += 1;
                    retval = new MultiplicationToken();
                } else if (input.startsWith("/", offset)) {
                    offset += 1;
                    retval = new DivisionToken();
                } else if (input.startsWith("%", offset)) {
                    offset += 1;
                    retval = new ModulusToken();
                } else if (input.startsWith("<", offset)) {
                    if (offset + 1 < input.length() && input.charAt(offset + 1) == '=') {
                        offset += 2;
                        retval = new LessThanOrEqualToken();
                    } else {
                        offset += 1;
                        retval = new LessThanToken();
                    }
                } else if (input.startsWith(">", offset)) {
                    if (offset + 1 < input.length() && input.charAt(offset + 1) == '=') {
                        offset += 2;
                        retval = new GreaterThanOrEqualToken();
                    } else {
                        offset += 1;
                        retval = new GreaterThanToken();
                    }
                } else if (input.startsWith("=", offset)) {
                    if (offset + 1 < input.length() && input.charAt(offset + 1) == '=') {
                        offset += 2;
                        retval = new EqualEqualToken();
                    } else {
                        offset += 1;
                        retval = new EqualToken();
                    }
                } else if (input.startsWith("\"", offset)) {
                    offset += 1;
                    String frag = getStringLiteral(input, offset);
                    offset += frag.length();
                    retval = new StringLiteralToken(frag);
                    offset += 1;
                } else if (Character.isDigit(input.charAt(0))) {
                    if (Character.isWhitespace(input.charAt(offset)))
                        offset++;
                    else {
                        String frag = getInt(input, offset);
                        offset += frag.length();
                        retval = (new IntegerToken(Integer.parseInt(frag)));
                    }
                } else {
                    throw new TokenizerException();
                }
            }
        }
        return retval;
    }

    public Token tryTokenizeVariableOrKeyword() {
        skipWhitespace();
        String name = "";

        if (offset < input.length() &&
            Character.isLetter(input.charAt(offset))) {
            name += input.charAt(offset);
            offset++;

            while (offset < input.length() && Character.isLetterOrDigit(input.charAt(offset))) {
                name += input.charAt(offset);
                offset++;
            }
            if (name.equals("String"))
                return (new StringKeywordToken());
            else if (name.equals("class"))
                return new ClassKeywordToken();
            else if (name.equals("constructor"))
                return new ConstructorKeywordToken();
            else if (name.equals("else"))
                return new ElseToken();
            else if (name.equals("for"))
                return new ForToken();
            else if (name.equals("if"))
                return new IfToken();
            else if (name.equals("Int"))
                return new IntKeywordToken();
            else if (name.equals("new"))
                return new NewToken();
            else if (name.equals("clogln"))
                return new CloglnToken();
            else if (name.equals("clog"))
                return new ClogToken();
            else if (name.equals("return"))
                return new ReturnToken();
            else if (name.equals("this"))
                return new ThisToken();
            else if (name.equals("Void"))
                return new VoidToken();
            else if (name.equals("while"))
                return new WhileToken();
            else if (name.equals("true"))
                return new BoolValueToken(true);
            else if (name.equals("false"))
                return (new BoolValueToken(false));
            else if (name.equals("Bool"))
                return new BoolKeywordToken();
            else if (name.equals("length"))
                return new LengthToken();
            else
                return new IdentifierToken(name);
        } else {
            return null;
        }
    }

    public void skipWhitespace() {
        while (offset < input.length() && Character.isWhitespace(input.charAt(offset)))
            offset++;
    }

    public static String getStringLiteral(String s, int i) throws TokenizerException {
        int j = i;
        while (j < s.length())
            if (s.charAt(j) != '\"')
                j++;
            else
                return s.substring(i, j);
        throw new TokenizerException(); //string doesnt null-terminate or has another issue.
    }

    public static String getInt(String s, int i) {
        int j = i;
        while (j < s.length())
            if (Character.isDigit(s.charAt(j)))
                j++;
            else
                return s.substring(i, j);
        return s.substring(i, j);
    }

    public List < Token > tokenize() throws TokenizerException {
        final List < Token > tokens = new ArrayList < Token > ();
        Token token = tokenizeSingle();

        while (token != null) {
            tokens.add(token);
            token = tokenizeSingle();
        }
        return tokens;
    }
}