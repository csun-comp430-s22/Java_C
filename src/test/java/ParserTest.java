import java.util.*;
import parser.*;
import parser.statements.*;
import parser.defs.*;
import parser.expressions.*;
import tokenizer.*;
import tokenizer.tokens.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    //TEST HELPER METHODS
	
	public void testParse(String programString, Program expected) throws TokenizerException, ParseException {
        final Tokenizer tokenizer = new Tokenizer(programString);
        final List < Token > tokens = tokenizer.tokenize();
        Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
        Parser myparser = new Parser(tokenArray);
        Program actual = myparser.parseProgram();
        assertEquals(expected.toString(), actual.toString());
    }

    public void testStatementParse(String stmtString, Statement expected) throws TokenizerException, ParseException {
        String programString = "int main(){" + stmtString + " return 0; }";
        final Tokenizer tokenizer = new Tokenizer(programString);
        List < Token > tokens = tokenizer.tokenize();
        Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
        Parser myparser = new Parser(tokenArray);
        Program program = myparser.parseProgram();
        Statement actual = program.entryPoint.body.body.get(0);
        assertEquals(expected.toString(), actual.toString());
    }

    public void testExpressionParse(String expString, Expression expected) throws TokenizerException, ParseException {
        String programString = "int main(){ return " + expString + "; }";
        final Tokenizer tokenizer = new Tokenizer(programString);
        List < Token > tokens = tokenizer.tokenize();
        Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
        Parser myparser = new Parser(tokenArray);
        Program program = myparser.parseProgram();
        Expression actual = program.entryPoint.returnExp;
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEqualsOpExp() throws TokenizerException, ParseException {
        // 1 + 1 == 1 + 1
        final PlusExp first = new PlusExp(new IntegerExp(1), new IntegerExp(1));
        final PlusExp second = new PlusExp(new IntegerExp(1), new IntegerExp(1));
        assertEquals(first.toString(), second.toString());
    }

    @Test
    public void testPrimaryVariable() throws TokenizerException, ParseException {
        Token[] token = {
            new IdentifierToken("x")
        };
        final Parser parser = new Parser(token);
        assertEquals((new ParseResult < Expression > (new VariableExp("x"), 1)).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 1, Arrays.asList(token))), 1).result.toString()
        );
    }

    @Test
    public void testPrimaryInteger() throws TokenizerException, ParseException {
        Token[] token = {
            new IntegerToken(123)
        };
        final Parser parser = new Parser(token);
        assertEquals((new ParseResult < Expression > (new IntegerExp(123), 1)).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 1, Arrays.asList(token))), 1).result.toString()
        );
    }

    @Test
    public void testLessThanMultiOperator() throws TokenizerException, ParseException {
        // 1 < 2 < 3 ==> (1 < 2) < 3
        Token[] token = {
            new IntegerToken(1),
            new LessThanToken(),
            new IntegerToken(2),
            new LessThanToken(),
            new IntegerToken(3)
        };
        final Parser parser = new Parser(token);
        final LessThanExp expected = new LessThanExp(new IntegerExp(1), new LessThanExp(new IntegerExp(2), new IntegerExp(3)));
        assertEquals(new ParseResult < Expression > (expected, 5).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 5, Arrays.asList(token))), 5).result.toString()
        );
    }

    @Test
    public void testLessThanSingleOperator() throws TokenizerException, ParseException {
        // 1 < 2
        Token[] token = {
            new IntegerToken(1),
            new LessThanToken(),
            new IntegerToken(2)
        };
        final Parser parser = new Parser(token);
        final LessThanExp expected = new LessThanExp(new IntegerExp(1), new IntegerExp(2));
        assertEquals(new ParseResult < Expression > (expected, 3).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 3, Arrays.asList(token))), 3).result.toString()
        );
    }

    @Test
    public void testLessThanMixedOperator() throws TokenizerException, ParseException {
        // 1 < 2 + 3 ==> 1 < (2 + 3)
        Token[] token = {
            new IntegerToken(1),
            new LessThanToken(),
            new IntegerToken(2),
            new PlusToken(),
            new IntegerToken(3)
        };
        final Parser parser = new Parser(token);
        final LessThanExp expected = new LessThanExp(new IntegerExp(1),
            new PlusExp(new IntegerExp(2),
                new IntegerExp(3)));
        assertEquals(new ParseResult < Expression > (expected, 5).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 5, Arrays.asList(token))), 5).result.toString()
        );
    }

    @Test
    public void testPrimaryParens() throws TokenizerException, ParseException {
        Token[] token = {
            new LeftParenToken(),
            new IntegerToken(123),
            new RightParenToken()
        };
        final Parser parser = new Parser(token);
        assertEquals(new ParseResult < Expression > (new ParenthesizedExp(new IntegerExp(123)), 3).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 3, Arrays.asList(token))), 3).result.toString()
        );
    }

    @Test
    public void testAdditiveExpSingleOperator() throws TokenizerException, ParseException {
        // 1 + 2
        Token[] token = {
            new IntegerToken(1),
            new PlusToken(),
            new IntegerToken(2)
        };
        final Parser parser = new Parser(token);
        assertEquals(new ParseResult < Expression > (new PlusExp(new IntegerExp(1), new IntegerExp(2)), 3).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 3, Arrays.asList(token))), 3).result.toString()
        );
    }

    @Test
    public void testAdditiveExpMultiOperator() throws TokenizerException, ParseException {
        // 1 + 2 - 3 ==> (1 + 2) - 3
        Token[] token = {
            new IntegerToken(1),
            new PlusToken(),
            new IntegerToken(2),
            new MinusToken(),
            new IntegerToken(3)
        };
        final Parser parser = new Parser(token);
        final MinusExp expected = new MinusExp(new PlusExp(new IntegerExp(1),
                new IntegerExp(2)),
            new IntegerExp(3));
        assertEquals(new ParseResult < Expression > (expected, 5).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 5, Arrays.asList(token))), 5).result.toString()
        );
    }

    @Test
    public void testLessThanExpOnlyAdditive() throws TokenizerException, ParseException {
        Token[] token = {
            new IntegerToken(123)
        };
        final Parser parser = new Parser(token);
        assertEquals(new ParseResult < Expression > (new IntegerExp(123), 1).result.toString(),
            new ParseResult < Expression > (parser.parseSubExp(parser.getExp(0, 1, Arrays.asList(token))), 1).result.toString()
        );
    }

    //HAPPY CASE TESTS

    @Test
    public void testParseExpression() throws TokenizerException, ParseException {
        String mystring;
        Expression expected;
        Expression actual;
        Token[] tokens;
        Parser myparser;
        mystring = "1/2;";
        expected = new DivisionExp(new IntegerExp(1), new IntegerExp(2));
        Tokenizer tokenizer = new Tokenizer(mystring);
        List < Token > tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
        mystring = "1/2*3;";
        expected = new MultiplicationExp(expected, new IntegerExp(3));
        tokenizer = new Tokenizer(mystring);
        tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
        mystring = "1/2*3/4;";
        expected = new DivisionExp(expected, new IntegerExp(4));
        tokenizer = new Tokenizer(mystring);
        tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
        mystring = "1/2*3/4*5;";
        expected = new MultiplicationExp(expected, new IntegerExp(5));
        tokenizer = new Tokenizer(mystring);
        tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
        mystring = "9*8+1/2*3/4*5;";
        expected = new PlusExp(new MultiplicationExp(new IntegerExp(9), new IntegerExp(8)), expected);
        tokenizer = new Tokenizer(mystring);
        tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
        mystring = "9*8+1/2*3/4*5+6;";
        expected = new PlusExp(expected, new IntegerExp(6));
        tokenizer = new Tokenizer(mystring);
        tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
        mystring = "9*8+1/2*3/4*5+6-7;";
        expected = new MinusExp(expected, new IntegerExp(7));
        tokenizer = new Tokenizer(mystring);
        tokensList = tokenizer.tokenize();
        tokens = tokensList.toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert(actual.equals(expected));
		Program expectedt = new Program(new ArrayList < ClassDef > (), new MethodDef("Bool", "", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
		expectedt.equals(expectedt);
   }

    @Test
    public void testMainOnly() throws TokenizerException, ParseException {
        String programString = "int main(){ return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testVoidMethod() throws TokenizerException, ParseException {
        String programString = "Void main(){ Bool x = true; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testStringMethod() throws TokenizerException, ParseException {
        String programString = "String main(){ Bool x = true; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("String", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testArray() throws TokenizerException, ParseException {
        String programString = "Void main(){ int x[5]; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("int", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testArrayVariableAssignment() throws TokenizerException, ParseException {
        String programString = "int main(){x[2]=6; return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableAssignmentStmt("x", new IntegerExp(6))))),
                new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testArrayIndexing() throws TokenizerException, ParseException {
        String programString = "int main(){ clogln(exp[exp]); return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new CloglnStmt(new ArrayIndexExp("exp"))))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testArrayIndexing2() throws TokenizerException, ParseException {
        String programString = "int main(){ clogln(exp[2]); return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new CloglnStmt(new ArrayIndexExp("exp"))))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testBoolMethod() throws TokenizerException, ParseException {
        String programString = "Bool main(){ Bool x = true; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Bool", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void MethodDefTest() throws TokenizerException, ParseException {
        String programString = "class TestClass{constructor(){} int methodTest(){ clogln(\"Hello World\"); return 0;}} " + "int main(){ return 0;}"; // one method
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        ArrayList < MethodDef > methodDefs = new ArrayList < MethodDef > ();
        ArrayList < Statement > methodStmts = new ArrayList < Statement > ();
        methodStmts.add(new CloglnStmt(new StringExp("Hello World")));
        methodDefs.add(new MethodDef("int", "methodTest", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(methodStmts), new IntegerExp(0)));
        classDefs.add(new ClassDef("TestClass", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), methodDefs));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
        programString = "class TestClass{constructor(){} int methodA(){return 0;} TestClass methodB(){return 0;}} " + "int main(){return 0;}"; // two methods
        classDefs = new ArrayList < ClassDef > ();
        methodDefs = new ArrayList < MethodDef > ();
        methodDefs.add(new MethodDef("int", "methodA", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        methodDefs.add(new MethodDef("TestClass", "methodB", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        classDefs.add(new ClassDef("TestClass", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), methodDefs));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void ClassDefTest() throws TokenizerException, ParseException {
        String programString;
        Program expected;
        programString = "class TestClass{constructor(){}}" + "int main(){return 0;}"; //One Class
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        classDefs.add(new ClassDef("TestClass", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
        programString = "class TestClassA{constructor(){}} class TestClassB{constructor(){}} " + "int main(){return 0;}"; //Two Classes
        classDefs = new ArrayList < ClassDef > ();
        classDefs.add(new ClassDef("TestClassA", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        classDefs.add(new ClassDef("TestClassB", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void InheritanceTest() throws TokenizerException, ParseException {
        String programString = "class TestClass:ParentClass{constructor(){}} " + "int main(){ return 0; }";
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        classDefs.add(new ClassDef("TestClass", "ParentClass", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void FieldTest() throws TokenizerException, ParseException {
        String programString = "class TestClass{int field1 = 1; String field2; TestClass field3 = new TestClass(); constructor(){} } " + "int main(){ return 0;}";
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        ArrayList < VariableDeclarationStmt > fieldDefs = new ArrayList < VariableDeclarationStmt > ();
        fieldDefs.add(new VariableDeclarationStmt("int", "field1", new IntegerExp(1)));
        fieldDefs.add(new VariableDeclarationStmt("String", "field2"));
        fieldDefs.add(new VariableDeclarationStmt("TestClass", "field3", new NewExp("TestClass", new ArrayList < Expression > ())));
        classDefs.add(new ClassDef("TestClass", fieldDefs, new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void ConstructorTesting() throws TokenizerException, ParseException {
        String programString = "class TestClass{int a; String x; constructor(int b, String y){a = b; x = y;} } " + "int main(){ return 0;}";
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        ArrayList < VariableDeclarationStmt > fieldDefs = new ArrayList < VariableDeclarationStmt > ();
        fieldDefs.add(new VariableDeclarationStmt("int", "a"));
        fieldDefs.add(new VariableDeclarationStmt("String", "x"));
        ArrayList < VariableDeclarationStmt > constructorParams = new ArrayList < VariableDeclarationStmt > ();
        constructorParams.add(new VariableDeclarationStmt("int", "b"));
        constructorParams.add(new VariableDeclarationStmt("String", "y"));
        ArrayList < Statement > constructorStmts = new ArrayList < Statement > ();
        constructorStmts.add(new VariableAssignmentStmt("a", new VariableExp("b")));
        constructorStmts.add(new VariableAssignmentStmt("x", new VariableExp("y")));
        classDefs.add(new ClassDef("TestClass", fieldDefs, new Constructor(constructorParams, new BlockStmt(constructorStmts)), new ArrayList < MethodDef > ()));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void HelloWorldTest() throws TokenizerException, ParseException {
        String programString = "int main(){" + "String mystring = \"Hello World!\";" + "clogln(mystring);" + "return 0;" + "}";
        ArrayList < Statement > stmtList = new ArrayList < Statement > ();
        stmtList.add(new VariableDeclarationStmt("String", "mystring", new StringExp("Hello World!")));
        stmtList.add(new CloglnStmt(new VariableExp("mystring")));
        BlockStmt mainBody = new BlockStmt(stmtList);
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), mainBody, new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void StatementTesting() throws TokenizerException, ParseException {
        String programString = "int main(){" +
            "int myInt;" +
            "String myString = \"text\";" +
            "myInt = 1;" +
            "clog(myString);" +
            "clogln(myString);" +
            "while(myInt > 1){myInt = myInt - 1; clogln(\"While Loop Text\");}" +
            "if(myInt == 1){clogln(\"true branch text\");} else {clogln(\"false branch text\");}" +
            "if(myInt == 0){clogln(\"true branch text\");}" +
            "return 0;" +
            "}";
        ArrayList < Statement > stmtList = new ArrayList < Statement > ();
        stmtList.add(new VariableDeclarationStmt("int", "myInt"));
        stmtList.add(new VariableDeclarationStmt("String", "myString", new StringExp("text")));
        stmtList.add(new VariableAssignmentStmt("myInt", new IntegerExp(1)));
        stmtList.add(new ClogStmt(new VariableExp("myString")));
        stmtList.add(new CloglnStmt(new VariableExp("myString")));
        ArrayList < Statement > whileStmts = new ArrayList < Statement > ();
        whileStmts.add(new VariableAssignmentStmt("myInt", new MinusExp(new VariableExp("myInt"), new IntegerExp(1))));
        whileStmts.add(new CloglnStmt(new StringExp("While Loop Text")));
        stmtList.add(new WhileStmt(new GreaterThanExp(new VariableExp("myInt"), new IntegerExp(1)),
            new BlockStmt(whileStmts)));
        ArrayList < Statement > ifElseTrueStmts = new ArrayList < Statement > ();
        ifElseTrueStmts.add(new CloglnStmt(new StringExp("true branch text")));
        ArrayList < Statement > ifElseFalseStmts = new ArrayList < Statement > ();
        ifElseFalseStmts.add(new CloglnStmt(new StringExp("false branch text")));
        stmtList.add(new IfElseStmt(new EqualEqualExp(new VariableExp("myInt"), new IntegerExp(1)),
            new BlockStmt(ifElseTrueStmts),
            new BlockStmt(ifElseFalseStmts)));
        ArrayList < Statement > ifStmts = new ArrayList < Statement > ();
        ifStmts.add(new CloglnStmt(new StringExp("true branch text")));
        stmtList.add(new IfStmt(new EqualEqualExp(new VariableExp("myInt"), new IntegerExp(0)),
            new BlockStmt(ifStmts)));
        BlockStmt mainBody = new BlockStmt(stmtList);
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), mainBody, new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void testNegativeNumberExp() throws TokenizerException, ParseException {
        testExpressionParse("-3", new IntegerExp(-3));
    }

    @Test
    public void testThisMethodCallExp() throws TokenizerException, ParseException {
        ThisExp callExp = new ThisExp(new VariableExp("name"));
        testExpressionParse("this.name", callExp);
    }

    @Test
    public void testStringExp() throws TokenizerException, ParseException {
        testExpressionParse("\"hello\"", new StringExp("hello"));
    }

    @Test
    public void testLessThanExp() throws TokenizerException, ParseException {
        testExpressionParse("5<3", new LessThanExp(new IntegerExp(5), new IntegerExp(3)));
    }

    @Test
    public void testLessThanOrEqualExp() throws TokenizerException, ParseException {
        testExpressionParse("5<=3", new LessThanOrEqualExp(new IntegerExp(5), new IntegerExp(3)));
    }

    @Test
    public void testGreaterThanExp() throws TokenizerException, ParseException {
        testExpressionParse("5>3", new GreaterThanExp(new IntegerExp(5), new IntegerExp(3)));
    }

    @Test
    public void testGreaterThanOrEqualExp() throws TokenizerException, ParseException {
        testExpressionParse("5>=3", new GreaterThanOrEqualExp(new IntegerExp(5), new IntegerExp(3)));
    }

    @Test
    public void testEqualEqualExp() throws TokenizerException, ParseException {
        testExpressionParse("5==3", new EqualEqualExp(new IntegerExp(5), new IntegerExp(3)));
    }

    @Test
    public void testModulusExp() throws TokenizerException, ParseException {
        testExpressionParse("5%3", new ModulusExp(new IntegerExp(5), new IntegerExp(3)));
    }

    @Test
    public void testNewExp() throws TokenizerException, ParseException {
        List < Expression > params = new ArrayList < Expression > ();
        params.add(new IntegerExp(5));
        params.add(new IntegerExp(3));
        testExpressionParse("new Class(5,3)", new NewExp("Class", params));
    }

    @Test
    public void testMethodCallExp() throws TokenizerException, ParseException {
        List < Expression > params = new ArrayList < Expression > ();
        params.add(new IntegerExp(5));
        MethodCallExp callExp = new MethodCallExp("obj", "method", params);
        testExpressionParse("obj.method(5)", callExp);
    }
	
	@Test
    public void testMethodCallExp2() throws TokenizerException, ParseException {
		Token[] token =  {
			new IdentifierToken("obj"),
			new DotOperatorToken(),
			new IdentifierToken("method"),
			new LeftParenToken(),
			new IntegerToken(5),
			new IntegerToken(6),
			new RightParenToken(),
			new IdentifierToken("hi"),
			new SemicolonToken()
		};
		Parser parser = new Parser(token); 
		parser.parseSubExp(new ArrayList<Token>(Arrays.asList(token))
		);
    }

    //TEST MISC

    @Test
    public void AllHelperTest() throws TokenizerException, ParseException {
        IntegerExp subject = new IntegerExp(5);
        ParenthesizedExp subject2 = new ParenthesizedExp(subject);
        StringExp subject3 = new StringExp("subject");
        ThisExp subject4 = new ThisExp(new IntegerExp(6));
        subject4.equals(new ThisExp(new IntegerExp(6)));
        subject3.equals(new StringExp("miher"));
        subject2.toString();
        subject2.equals(subject);
		subject.equals(subject);
        subject.equals(new IntegerExp(4));
        subject.equals(new StringExp("miher"));
        GreaterThanExp subject5 = new GreaterThanExp(new IntegerExp(5), new IntegerExp(4));
        subject5.equals(subject5);
        LessThanExp subject6 = new LessThanExp(new IntegerExp(4), new IntegerExp(5));
        subject6.equals(subject6);
        VariableExp subject7 = new VariableExp("x");
        subject7.equals(subject7);
        ArrayIndexExp kevin = new ArrayIndexExp("x");
        kevin.equals(kevin);
        ModulusExp subject8 = new ModulusExp(new IntegerExp(4), new IntegerExp(5));
        subject8.equals(subject8);
        EqualEqualExp subject9 = new EqualEqualExp(new IntegerExp(4), new IntegerExp(5));
        subject9.equals(subject9);
        LessThanOrEqualExp subject10 = new LessThanOrEqualExp(new IntegerExp(4), new IntegerExp(5));
        subject10.equals(subject10);
        GreaterThanOrEqualExp subject11 = new GreaterThanOrEqualExp(new IntegerExp(4), new IntegerExp(5));
        subject11.equals(subject11);
        VariableDeclarationStmt x = new VariableDeclarationStmt("int", "x");
        x.equals(x);
        BlockStmt blk = new BlockStmt(new ArrayList < Statement > ());
        blk.equals(blk);
        IfElseStmt ife = new IfElseStmt(new StringExp("subject"), blk, blk);
        ife.equals(ife);
        VariableAssignmentStmt vas = new VariableAssignmentStmt("michael", new IntegerExp(7));
        vas.equals(vas);
        IfStmt if1 = new IfStmt(new StringExp("subject"), blk);
        if1.equals(if1);
        ReturnVoidStmt ret = new ReturnVoidStmt();
        ret.equals(ret);
        ret.toString();
        WhileStmt w = new WhileStmt(new StringExp("subject"), blk);
        w.equals(w);
        ReturnStmt r = new ReturnStmt(new StringExp("subject"));
        r.equals(r);
        ClogStmt cl = new ClogStmt(new StringExp("subject"));
        cl.equals(cl);
        CloglnStmt cln = new CloglnStmt(new StringExp("subject"));
        cln.equals(cln);
        NewExp new1 = new NewExp("rust", new ArrayList < Expression > (Arrays.asList(new IntegerExp(0))));
        new1.equals(new1);
        MethodCallExp met = new MethodCallExp("rust", "cust", new ArrayList < Expression > (Arrays.asList(new VariableExp("x"), new VariableExp("y"))));
        met.equals(met);
		met.toString();
		MethodDef rusty = new MethodDef("rust","rust",new ArrayList<VariableDeclarationStmt>(Arrays.asList(new VariableDeclarationStmt("int", "x"),new VariableDeclarationStmt("int", "y"))),blk,new IntegerExp(0));
		rusty.toString();
		rusty.equals(rusty);
		ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        ArrayList < MethodDef > methodDefs = new ArrayList < MethodDef > ();
        ArrayList < Statement > methodStmts = new ArrayList < Statement > ();
        methodStmts.add(new CloglnStmt(new StringExp("Hello World")));
        methodDefs.add(new MethodDef("int", "methodTest", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(methodStmts), new IntegerExp(0)));
        classDefs.add(new ClassDef("TestClass", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), methodDefs));
		classDefs.get(0).equals(classDefs.get(0));
		Constructor cons = new Constructor(new ArrayList<VariableDeclarationStmt>(Arrays.asList(new VariableDeclarationStmt("int", "x"),new VariableDeclarationStmt("int", "y"))), blk);
		cons.equals(cons);
	}

    //BAD TESTS
	
	@Test(expected = ParseException.class)
    public void testUnevenParensException() throws TokenizerException, ParseException {
        testExpressionParse("((5)", null);
    }
	
	@Test(expected = ParseException.class)
    public void testNewExpExceptionMissingName() throws TokenizerException, ParseException {
        List < Expression > params = new ArrayList < Expression > ();
        testExpressionParse("new()", new NewExp("Class", params));
    }

    @Test(expected = ParseException.class)
    public void testNewExpExceptionMissingParens() throws TokenizerException, ParseException {
        testExpressionParse("new Class 5", new NewExp("Class", new ArrayList < Expression > ()));
    }

    @Test(expected = ParseException.class)
    public void testNoParenMethodCallExp() throws TokenizerException, ParseException {
        List < Expression > params = new ArrayList < Expression > ();
        params.add(new IntegerExp(5));
        MethodCallExp callExp = new MethodCallExp("obj", "method", params);
        testExpressionParse("obj.method 5)", callExp);
    }

    @Test(expected = ParseException.class)
    public void noIdentifierMethod() throws TokenizerException, ParseException {
        String programString = "Bool (){ Bool x = true; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Bool", "", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void noTypeMethod() throws TokenizerException, ParseException {
        String programString = "(){ Bool x = true; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "name", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }


    @Test(expected = java.lang.NullPointerException.class)
    public void testReturnVoid() throws TokenizerException, ParseException {
        String programString = "void main(){return;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new ReturnVoidStmt())))));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testMess() throws TokenizerException, ParseException {
        String programString = "int main(){}[]-+=void./*;(+=)factorial++{ x.Bool x()}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void BadConstructorTesting() throws TokenizerException, ParseException {
        String programString = "class TestClass{int a; String x; constructor)int b, String y{a = b; x = y;} } " + "int main(){ return 0;}";
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        ArrayList < VariableDeclarationStmt > fieldDefs = new ArrayList < VariableDeclarationStmt > ();
        fieldDefs.add(new VariableDeclarationStmt("int", "a"));
        fieldDefs.add(new VariableDeclarationStmt("String", "x"));
        ArrayList < VariableDeclarationStmt > constructorParams = new ArrayList < VariableDeclarationStmt > ();
        constructorParams.add(new VariableDeclarationStmt("int", "b"));
        constructorParams.add(new VariableDeclarationStmt("String", "y"));
        ArrayList < Statement > constructorStmts = new ArrayList < Statement > ();
        constructorStmts.add(new VariableAssignmentStmt("a", new VariableExp("b")));
        constructorStmts.add(new VariableAssignmentStmt("x", new VariableExp("y")));
        classDefs.add(new ClassDef("TestClass", fieldDefs, new Constructor(constructorParams, new BlockStmt(constructorStmts)), new ArrayList < MethodDef > ()));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadMethodCallExp() throws TokenizerException, ParseException {
        List < Expression > params = new ArrayList < Expression > ();
        params.add(new IntegerExp(5));
        MethodCallExp callExp = new MethodCallExp("obj", "method", params);
        testExpressionParse("obj.", callExp);
    }

    @Test(expected = ParseException.class)
    public void testNoParen() throws TokenizerException, ParseException {
        String programString = "Void main({ Bool x = true; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("Bool", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void BadClassDefTest() throws TokenizerException, ParseException {
        String programString;
        Program expected;
        programString = "class{constructor(){}}" + "int main(){return 0;}";
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        classDefs.add(new ClassDef("TestClass", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
        programString = "class TestClassA{constructor(){}} class TestClassB{constructor(){}} " + "int main(){return 0;}"; //Two Classes
        classDefs = new ArrayList < ClassDef > ();
        classDefs.add(new ClassDef("TestClassA", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        classDefs.add(new ClassDef("TestClassB", new ArrayList < VariableDeclarationStmt > (), new Constructor(new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ())), new ArrayList < MethodDef > ()));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadVariableAssignment1() throws TokenizerException, ParseException {
        String programString = "int main(){x[]=6; return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableAssignmentStmt("x", new IntegerExp(6))))),
                new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadVariableAssignment2() throws TokenizerException, ParseException {
        String programString = "int main(){x[5=6; return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableAssignmentStmt("x", new IntegerExp(6))))),
                new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadVariableAssignment3() throws TokenizerException, ParseException {
        String programString = "int main(){x5]=6; return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableAssignmentStmt("x", new IntegerExp(6))))),
                new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadVarDec2() throws TokenizerException, ParseException {
        String programString = "int main(){int=6 x=6; return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("int", "x"), new VariableAssignmentStmt("x", new IntegerExp(6))))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test (expected = ParseException.class)
    public void testBadVarDec4() throws TokenizerException, ParseException {
        String programString = "int main(){int=6;  return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableAssignmentStmt("x", new IntegerExp(6))))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test (expected = ParseException.class)
    public void testBadVarDec5() throws TokenizerException, ParseException {
        String programString = "int main(){int=6; return 0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("int","x", new IntegerExp(6))))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadArray1() throws TokenizerException, ParseException {
        String programString = "Void main(){ String x5]; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("String", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadArray2() throws TokenizerException, ParseException {
        String programString = "Void main(){ String x[5; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("String", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test(expected = ParseException.class)
    public void testBadArray3() throws TokenizerException, ParseException {
        String programString = "Void main(){ String x[]; return  0;}";
        Program expected = new Program(new ArrayList < ClassDef > (), new MethodDef("Void", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > (Arrays.asList(new VariableDeclarationStmt("String", "x")))), new IntegerExp(0)));
        testParse(programString, expected);
    }
}