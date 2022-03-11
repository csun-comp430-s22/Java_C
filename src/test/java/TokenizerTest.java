import java.util.*;
import tokenizer.*;
import tokenizer.tokens.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenizerTest {
	
    public void assertTokenizes(final String input, final Token[] expected) throws TokenizerException {
        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
		for(int i=0;i<received.size();i++) {
			assertEquals(received.get(i).hashCode(), expected[i].hashCode());
			assertEquals(received.get(i).toString(), expected[i].toString());
		}
		assertArrayEquals(expected, received.toArray(new Token[received.size()]));
    }
	
	//test happy cases!
	
    @Test
    public void testEmptyString() throws TokenizerException {
        assertTokenizes("", new Token[0]);
    }
	
	@Test
    public void testIntHashcode() throws TokenizerException {
        assertEquals(new IntegerToken(777).hashCode(),777 );
    }
	
	@Test
    public void testStringHashcode() throws TokenizerException {
        assertEquals(new StringLiteralToken("hey").hashCode(),"hey".hashCode());
    }

    @Test
    public void testOnlyWhitespace() throws TokenizerException {
        assertTokenizes("    ", new Token[0]);
    }

    @Test
    public void testTrueByItself() throws TokenizerException {
        assertTokenizes("true", new Token[] { new BoolValueToken(true) });
    }
	
    @Test
    public void testStringKeyword() throws TokenizerException {
        assertTokenizes("String", new Token[] {new StringKeywordToken() });
	}
	
	@Test
	public void testClassKeyword() throws TokenizerException {
        assertTokenizes("class", new Token[] {new ClassKeywordToken()});
	}
	
	@Test
	public void testConstructorKeyword() throws TokenizerException {
        assertTokenizes("constructor", new Token[] {new ConstructorKeywordToken()});
	}
	
	@Test
	public void testElseKeyword() throws TokenizerException {
        assertTokenizes("else", new Token[] {new ElseToken()});
	}
	
	@Test
	public void testForKeyword() throws TokenizerException {
        assertTokenizes("for", new Token[] {new ForToken()});
	}
	
	@Test
	public void testIfKeyword() throws TokenizerException {
        assertTokenizes("if", new Token[] {new IfToken()});
	}
	
	@Test
	public void testIntKeyword() throws TokenizerException {
        assertTokenizes("Int", new Token[] {new IntKeywordToken()});
	}
	
	@Test
	public void testNewKeyword() throws TokenizerException {
        assertTokenizes("new", new Token[] {new NewToken()});
	}
	
	@Test
	public void testCloglnKeyword() throws TokenizerException {
        assertTokenizes("clogln", new Token[] {new CloglnToken()});
	}
	
	@Test
	public void testClogKeyword() throws TokenizerException {
        assertTokenizes("clog", new Token[] {new ClogToken()});
	}
	
	@Test
	public void testLengthKeyword() throws TokenizerException {
        assertTokenizes("length", new Token[] {new LengthToken()});
	}
	
	@Test
	public void testReturnKeyword() throws TokenizerException {
        assertTokenizes("return", new Token[] {new ReturnToken()});
	}
	
	@Test
	public void testThisKeyword() throws TokenizerException {
        assertTokenizes("this", new Token[] {new ThisToken()});
	}
	
	@Test
	public void testVoidKeyword() throws TokenizerException {
        assertTokenizes("Void", new Token[] {new VoidToken()});
	}
	
	@Test
	public void testWhileKeyword() throws TokenizerException {
        assertTokenizes("while", new Token[] {new WhileToken()});
	}
	
	@Test
	public void testBoolKeyword() throws TokenizerException {
        assertTokenizes("Bool", new Token[] {new BoolKeywordToken()});
	}
	
	@Test
	public void testTrueKeyword() throws TokenizerException {
        assertTokenizes("true", new Token[] {new BoolValueToken(true)});
	}
	
	@Test
	public void testFalseKeyword() throws TokenizerException {
        assertTokenizes("false", new Token[] {new BoolValueToken(false)});
    }
	
	@Test
    public void testIdentifiers() throws TokenizerException {
        assertTokenizes("Circle", new Token[] {new IdentifierToken("Circle")});
    }
	
    @Test 
    public void testIntegers() throws TokenizerException {
        assertTokenizes("1234567890", new Token[] {new IntegerToken(1234567890)});
	}
	
    @Test
    public void testStringLiteral() throws TokenizerException {
        assertTokenizes("\"test\"", new Token[] {new StringLiteralToken("test")});
    }

    @Test
    public void testDotOperator() throws TokenizerException {
        assertTokenizes(".", new Token[] {new DotOperatorToken()});
	}
	
	@Test
	public void testPlusOperator() throws TokenizerException {
        assertTokenizes("+", new Token[] {new PlusToken()});
	}
	
	@Test
	public void testMinusOperator() throws TokenizerException {
        assertTokenizes("-", new Token[] {new MinusToken()});
	}
	
	@Test
	public void testMultiplicationOperator() throws TokenizerException {
        assertTokenizes("*", new Token[] {new MultiplicationToken()});
	}
	
	@Test
	public void testDivisionOperator() throws TokenizerException {
        assertTokenizes("/", new Token[] {new DivisionToken()});
	}
	
	@Test
	public void testModulusOperator() throws TokenizerException {
        assertTokenizes("%", new Token[] {new ModulusToken()});
	}
	
	@Test
	public void testLTOperator() throws TokenizerException {
        assertTokenizes("<", new Token[] {new LessThanToken()});
	}
	
	@Test
	public void testLTEQOperator() throws TokenizerException {
        assertTokenizes("<=", new Token[] {new LessThanOrEqualToken()});
	}
	
	@Test
	public void testGTEQOperator() throws TokenizerException {
        assertTokenizes(">=", new Token[] {new GreaterThanOrEqualToken()});
	}
	
	@Test
	public void testGTOperator() throws TokenizerException {
        assertTokenizes(">", new Token[] {new GreaterThanToken()});
	}
	
	@Test
	public void testEqualOperator() throws TokenizerException {
        assertTokenizes("=", new Token[] {new EqualToken()});
	}
	
	@Test
	public void testEqualToOperator() throws TokenizerException {
        assertTokenizes("==", new Token[] {new EqualEqualToken()});
    }

    @Test
    public void TestLeftParenToken() throws TokenizerException {
        assertTokenizes("(", new Token[] {new LeftParenToken()});
	}
	
	@Test
	public void TestRightParenToken() throws TokenizerException {
        assertTokenizes(")", new Token[] {new RightParenToken()});
	}
	
	@Test
	public void TestLeftCurlyToken() throws TokenizerException {
        assertTokenizes("{", new Token[] {new LCurlyToken()});
	}
	
	@Test
    public void TestRightCurlyToken() throws TokenizerException {    
		assertTokenizes("}", new Token[] {new RCurlyToken()});
    }
	
	@Test
    public void TestLeftBracketToken() throws TokenizerException {    
		assertTokenizes("[", new Token[] {new LBracketToken()});
    }
	
	@Test
    public void TestRightBracketToken() throws TokenizerException {    
		assertTokenizes("]", new Token[] {new RBracketToken()});
    }
	
	@Test
	public void TestSemicolonToken() throws TokenizerException {    
		assertTokenizes(";", new Token[] {new SemicolonToken()});
    }
	
	@Test
	public void TestCommaToken() throws TokenizerException {   
		assertTokenizes(",", new Token[] {new CommaToken()});
    }
	
	@Test
	public void TestColonToken() throws TokenizerException {    
		assertTokenizes(":", new Token[] {new ColonToken()});
    }

	@Test
	public void TestMakeVariable() throws TokenizerException {    
		assertTokenizes("String x = \"777\";", new Token[] {new StringKeywordToken(),new IdentifierToken("x"),new EqualToken(), new StringLiteralToken("777"),new SemicolonToken()});
    }
	
	//test BAD tokens!
	
	@Test(expected = java.lang.AssertionError.class)
    public void testBadIdentifier() throws TokenizerException {
        assertEquals(new IdentifierToken("class"), new StringLiteralToken("class"));
    }
	
	@Test(expected = TokenizerException.class)
    public void testInvalid() throws TokenizerException {
        assertTokenizes("$", null);
    }
	
	@Test(expected = java.lang.AssertionError.class)
    public void testBadInt() throws TokenizerException {
		assertEquals(new IntegerToken(1), new IntegerToken(2));
    }
	
	@Test(expected = java.lang.AssertionError.class)
    public void testBadIntString() throws TokenizerException {
		assertEquals(new IntegerToken(1), new StringLiteralToken("1"));
    }
	
	@Test(expected = java.lang.AssertionError.class)
    public void testBadStringLiteral() throws TokenizerException {
        assertEquals(new StringLiteralToken("tes"), new StringLiteralToken("test"));
    }
	
	@Test(expected = java.lang.AssertionError.class)
    public void testBadStringInt() throws TokenizerException {
        assertEquals(new StringLiteralToken("1"), new IntegerToken(1));
    }
}