package parser;
import java.util.*;
import tokenizer.*;
import tokenizer.tokens.*;
import parser.defs.*;
import parser.expressions.*;
import parser.statements.*;

public class Parser {
    private Token[] tokens; //token array member

    public Parser(Token[] tokens) { //parser constructor
        this.tokens = tokens;
    }

    public ParseResult <WhileStmt> parseWhileStmt(final int initial) throws ParseException, TokenizerException { //parse for a while loop statement
        int offset = initial;
        Expression condition;
        BlockStmt body;
        if (tokens[offset] instanceof WhileToken)
            offset++;
        if (tokens[offset] instanceof LeftParenToken)
            offset++;
        ParseResult <Expression> expResult = parseExp(offset);
        condition = expResult.result;
        offset = expResult.offset;
        if (tokens[offset] instanceof RightParenToken)
            offset++;
        ParseResult <Statement> stmtResult = parseStmt(offset);
        body = (BlockStmt) stmtResult.result;
        offset = stmtResult.offset;
        return new ParseResult <WhileStmt> ((new WhileStmt(condition, body)), offset);
    }

    public ParseResult <Statement> parseIfStmt(final int initial) throws ParseException, TokenizerException { //parse for an if/else statement
        int offset = initial;
        Expression condition;
        BlockStmt trueBranch;
        BlockStmt falseBranch;
        if (tokens[offset] instanceof IfToken)
            offset++;
        if (tokens[offset] instanceof LeftParenToken)
            offset++;
        ParseResult <Expression> expResult = parseExp(offset);
        condition = expResult.result;
        offset = expResult.offset;
        if (tokens[offset] instanceof RightParenToken)
            offset++;
        ParseResult <Statement> stmtResult = parseStmt(offset);
        trueBranch = (BlockStmt) stmtResult.result;
        offset = stmtResult.offset;
        if (tokens[offset] instanceof ElseToken) {
            offset++;
            stmtResult = parseStmt(offset);
            falseBranch = (BlockStmt) stmtResult.result;
            offset = stmtResult.offset;
            return new ParseResult <Statement> (new IfElseStmt(condition, trueBranch, falseBranch), offset);
        }
        return new ParseResult <Statement> (new IfStmt(condition, trueBranch), offset);
    }

    public ParseResult <BlockStmt> parseBlockStmt(final int initial) throws ParseException, TokenizerException { //parse a statement in a curly brace scope
        ArrayList <Statement> block = new ArrayList <Statement> ();
        int offset = initial;
        if (tokens[offset] instanceof LCurlyToken)
            offset++;
        while (!(tokens[offset] instanceof RCurlyToken)) {
            ParseResult <Statement> result = parseStmt(offset);
            System.out.println(result.result.toString());
            block.add(result.result);
            offset = result.offset;
        }
        if (tokens[offset] instanceof RCurlyToken)
            offset++;
        return new ParseResult <BlockStmt> (new BlockStmt(block), offset);
    }

    public ParseResult <VariableDeclarationStmt> parseVarDec(final int initial) throws ParseException, TokenizerException { //parse vardec
        String type = null;
        String name;
        Expression value = null;
        int offset = initial;
        if (tokens[offset] instanceof IntKeywordToken) {
            type = "int";
            offset++;
        } else if (tokens[offset] instanceof BoolKeywordToken) {
            type = "Bool";
            offset++;
        } else if (tokens[offset] instanceof StringKeywordToken) {
            type = "String";
            offset++;
        } else if (tokens[offset] instanceof IdentifierToken) { 
            type = ((IdentifierToken) tokens[offset]).name;
            offset++;			
        }
        if (tokens[offset] instanceof IdentifierToken) {
            name = ((IdentifierToken) tokens[offset]).name;
            offset++;
			if (tokens[offset] instanceof LBracketToken) {
				offset++;
				if (tokens[offset] instanceof IntegerToken) 
					offset++;
				else 
					throw new ParseException("Expected array index integer token; received: " + tokens[offset].toString());
				if (tokens[offset] instanceof RBracketToken) 
					offset++;
				else 
					throw new ParseException("Expected array right bracket token; received: " + tokens[offset].toString());
			}
        } else
            throw new ParseException("Expected IdentifierToken for variable name; received " + tokens[offset].toString());
        if (tokens[offset] instanceof EqualToken) {
			offset++;
            ParseResult <Expression> result = parseExp(offset);
            value = result.result;
            offset = result.offset;
        }
        if (value != null) 
            return new ParseResult <VariableDeclarationStmt> (new VariableDeclarationStmt(type, name, value), offset);
        else 
            return new ParseResult <VariableDeclarationStmt> (new VariableDeclarationStmt(type, name), offset);
    }

    public ParseResult <VariableAssignmentStmt> parseVarAssignment(final int initial) throws ParseException, TokenizerException { //parse for variable assignment
        String name = null;
        Expression value = null;
        int offset = initial;
        if (tokens[offset] instanceof IdentifierToken) {
            name = ((IdentifierToken) tokens[offset]).name;
            offset++;
			if (tokens[offset] instanceof LBracketToken) {
				offset++;
				if (tokens[offset] instanceof IntegerToken) 
					offset++;
				else 
					throw new ParseException("Expected array index integer token; received: " + tokens[offset].toString());
				if (tokens[offset] instanceof RBracketToken) 
					offset++;
				else 
					throw new ParseException("Expected array right bracket token; received: " + tokens[offset].toString());	
			}
        }
        if (tokens[offset] instanceof EqualToken) {
            offset++;
            ParseResult <Expression> result = parseExp(offset);
            value = result.result;
            offset = result.offset;
        }
        return new ParseResult <VariableAssignmentStmt> (new VariableAssignmentStmt(name, value), offset);
    }

    public ParseResult <Constructor> parseConstructor(final int initial) throws ParseException, TokenizerException { //parse constructor creation
        int offset = initial;
        ArrayList <VariableDeclarationStmt> parameters = new ArrayList <VariableDeclarationStmt> ();
        BlockStmt body;
        if (tokens[offset] instanceof ConstructorKeywordToken)
            offset++;
        if (tokens[offset] instanceof LeftParenToken)
            offset++;
        else
            throw new ParseException("Expected LeftParenToken; received " + tokens[offset].toString());
        if (!(tokens[offset] instanceof RightParenToken)) {
            ParseResult <VariableDeclarationStmt> result = parseVarDec(offset);
            parameters.add(result.result);
            offset = result.offset;
            while (tokens[offset] instanceof CommaToken) {
                offset++;
                result = parseVarDec(offset);
                parameters.add(result.result);
                offset = result.offset;
            }
        }
        if (tokens[offset] instanceof RightParenToken)
            offset++;
        ParseResult <BlockStmt> result = parseBlockStmt(offset);
        body = result.result;
        offset = result.offset;
        return new ParseResult <Constructor> (new Constructor(parameters, body), offset);
    }

    public ParseResult <MethodDef> parseMethodDef(final int initial) throws ParseException, TokenizerException { //parse method decl
        int offset = initial;
        String type;
        String name;
        ArrayList <VariableDeclarationStmt> parameters = new ArrayList <VariableDeclarationStmt> ();
        BlockStmt body;
        Expression returnExp = null;
        if (tokens[offset] instanceof IntKeywordToken) {
            type = "int";
            offset++;
        } else if (tokens[offset] instanceof BoolKeywordToken) {
            type = "Bool";
            offset++;
        } else if (tokens[offset] instanceof StringKeywordToken) {
            type = "String";
            offset++;
        } else if (tokens[offset] instanceof VoidToken) {
            type = "Void";
            offset++;
        } else if (tokens[offset] instanceof IdentifierToken) {
            type = ((IdentifierToken) tokens[offset]).name;
            offset++;
        } else
            throw new ParseException("Expected token indicating method return type; received " + tokens[offset].toString());
        if (tokens[offset] instanceof IdentifierToken) {
            name = ((IdentifierToken) tokens[offset]).name;
            offset++;
        } else
            throw new ParseException("Expected IdentifierToken for method name; received " + tokens[offset].toString());
        if (tokens[offset] instanceof LeftParenToken)
            offset++;
        if (tokens[offset] instanceof RightParenToken)
            offset++;
        else
            throw new ParseException("Expected RightParenToken; received " + tokens[offset].toString());
        ParseResult <BlockStmt> result = parseBlockStmt(offset);
        body = result.result;
        offset = result.offset;
        List <Statement> bodyStmts = body.body;
        if (bodyStmts.size()> 0) {
            Statement returnStmt = bodyStmts.get(bodyStmts.size() - 1);
            if (returnStmt instanceof ReturnStmt) {
                returnExp = ((ReturnStmt) returnStmt).value;
                bodyStmts.remove(bodyStmts.size() - 1);
            } 
        }
        return new ParseResult <MethodDef> (new MethodDef(type, name, parameters, (BlockStmt) body, returnExp), offset);
    }

    public ParseResult <ClassDef> parseClassDefinition(final int initial) throws ParseException, TokenizerException { //defining a class
        String className;
        String parentClass = "";
        ArrayList <VariableDeclarationStmt> fields = new ArrayList <VariableDeclarationStmt> ();
        Constructor constructor = null;
        ArrayList <MethodDef> methods = new ArrayList <MethodDef> ();
        int offset = initial;
        if (tokens[offset] instanceof ClassKeywordToken)
            offset++;
        if (tokens[offset] instanceof IdentifierToken) {
            className = ((IdentifierToken) tokens[offset]).name;
            offset++;
        } else
            throw new ParseException("Expected IdentifierToken for className; received " + tokens[offset].toString());
        if (tokens[offset] instanceof ColonToken) {
            offset++;
            if (tokens[offset] instanceof IdentifierToken) {
                parentClass = ((IdentifierToken) tokens[offset]).name;
                offset++;
            }
        }
        if (tokens[offset] instanceof LCurlyToken)
            offset++;
        while (!(tokens[offset] instanceof ConstructorKeywordToken)) {
            ParseResult <VariableDeclarationStmt> result = parseVarDec(offset);
            fields.add(result.result);
            offset = result.offset;
            if (tokens[offset] instanceof SemicolonToken)
                offset++;
        }
      if (tokens[offset] instanceof ConstructorKeywordToken) {
            ParseResult <Constructor> result = parseConstructor(offset);
            constructor = result.result;
            offset = result.offset;
        } 
        while (!(tokens[offset] instanceof RCurlyToken)) {
            ParseResult <MethodDef> result = parseMethodDef(offset);
            methods.add(result.result);
            offset = result.offset;
        }
        if (tokens[offset] instanceof RCurlyToken)
            offset++;
        if (parentClass.isEmpty())
            return new ParseResult <ClassDef> (new ClassDef(className, fields, constructor, methods), offset);
        else
            return new ParseResult <ClassDef> (new ClassDef(className, parentClass, fields, constructor, methods), offset);
    }

    public Program parseProgram() throws ParseException, TokenizerException { //program entrypoint
        int offset = 0;
        ArrayList <ClassDef> classDefs = new ArrayList <ClassDef> ();
        while (tokens[offset] instanceof ClassKeywordToken) {
            ParseResult <ClassDef> result = parseClassDefinition(offset);
            classDefs.add(result.result);
            offset = result.offset;
        }
        final ParseResult <MethodDef> result = parseMethodDef(offset);
        if (result.offset == tokens.length) return new Program(classDefs, result.result);
        else throw new ParseException("Extrenneous tokens at the end.");
    }
	
	public ParseResult <Expression> parseExp(final int initial) throws ParseException, TokenizerException { //parse through expressions
        int offset = initial;
        int parenBalance = 0;

        while (!(tokens[offset] instanceof SemicolonToken) && offset != tokens.length) {
            if (tokens[offset] instanceof LeftParenToken)
                parenBalance++;
            if (tokens[offset] instanceof RightParenToken) {
                parenBalance--;
                if (parenBalance <0) break;
            } 
            offset++;
        } 
        return new ParseResult <Expression> (parseSubExp(getExp(initial, offset, Arrays.asList(tokens))), offset);
    }

    public ArrayList <Token> getExp(int left, int right, List <Token> tokensArray) { //get expresions
        ArrayList <Token> subExp = new ArrayList <Token> ();
        while (left != right) {
            subExp.add(tokensArray.get(left));
            left++;
        }
        return (subExp);
    }

    public ParseResult <Statement> parseStmt(final int initial) throws ParseException, TokenizerException { //parse for simpler statements
        int offset = initial;
        Statement myStmt = null;
        if (tokens[offset] instanceof ClogToken) {
            offset++;
            if (tokens[offset] instanceof LeftParenToken) {
                offset++;
                ParseResult <Expression> expressionResult = parseExp(offset);
                myStmt = new ClogStmt(expressionResult.result);
                offset = expressionResult.offset;
            }
            if (tokens[offset] instanceof RightParenToken)
                offset++;
            if (tokens[offset] instanceof SemicolonToken)
                offset++;
        } else if (tokens[offset] instanceof CloglnToken) {
            offset++;
            if (tokens[offset] instanceof LeftParenToken) {
                offset++;
                ParseResult <Expression> expressionResult = parseExp(offset);
                myStmt = new CloglnStmt(expressionResult.result);
                offset = expressionResult.offset;
            }
            if (tokens[offset] instanceof RightParenToken)
                offset++;
            if (tokens[offset] instanceof SemicolonToken)
                offset++;
        } else if (tokens[offset] instanceof WhileToken) {
            ParseResult <WhileStmt> result = parseWhileStmt(offset);
            myStmt = result.result;
            offset = result.offset;
        } else if (tokens[offset] instanceof IfToken) {
            ParseResult <Statement> result = parseIfStmt(offset);
            myStmt = result.result;
            offset = result.offset;
        } else if (tokens[offset] instanceof LCurlyToken) {
            ParseResult <BlockStmt> result = parseBlockStmt(offset);
            myStmt = result.result;
            offset = result.offset;
        } else if (tokens[offset] instanceof IntKeywordToken || tokens[offset] instanceof BoolKeywordToken || tokens[offset] instanceof StringKeywordToken) {
            ParseResult <VariableDeclarationStmt> result = parseVarDec(offset);
            myStmt = result.result;
            offset = result.offset;
            if (tokens[offset] instanceof SemicolonToken)
                offset++;
        } else if (tokens[offset] instanceof IdentifierToken) {
			ParseResult <VariableAssignmentStmt> result = parseVarAssignment(offset);
            myStmt = result.result;
            offset = result.offset;
            if (tokens[offset] instanceof SemicolonToken)
                offset++;
        } else if (tokens[offset] instanceof ReturnToken) {
            offset++;
            if (!(tokens[offset] instanceof SemicolonToken)) {
                ParseResult <Expression> expressionResult = parseExp(offset);
                myStmt = new ReturnStmt(expressionResult.result);
                offset = expressionResult.offset;
                if (tokens[offset] instanceof SemicolonToken)
                    offset++;
            }
        } else 
            throw new ParseException("Unexpected Token " + tokens[offset].toString() + " received while parsing a statement.");
        return new ParseResult <Statement> (myStmt, offset);
    }
	
    public Expression parseSubExp(ArrayList <Token> tokensArray) throws ParseException, TokenizerException { //parsing for subexpressions
        int right = tokensArray.size() - 1;
        int left = tokensArray.size() - 1;
        int paren = 0;
        int ops = 0;
        int neg = 0;
        int nums = 0;
        for (left = 0; left <tokensArray.size() - 1; left++) {
            if (tokensArray.get(left) instanceof LessThanToken)
                return new LessThanExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof GreaterThanToken)
                return new GreaterThanExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof LessThanOrEqualToken)
                return new LessThanOrEqualExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof GreaterThanOrEqualToken)
                return new GreaterThanOrEqualExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof EqualEqualToken)
                return new EqualEqualExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
        }
        for (int i = tokensArray.size() - 1; i>= 0; i--) {
            if (tokensArray.get(i) instanceof RightParenToken) 
                paren++;
            if (tokensArray.get(i) instanceof LeftParenToken) 
                paren--;
        }
        if (paren % 2 != 0)
            throw new ParseException("Missing Parentheses");
        for (int i = tokensArray.size() - 1; i>= 0; i--) {
            if (tokensArray.get(i) instanceof IntegerToken || tokensArray.get(i) instanceof IdentifierToken)
                nums++;
            if ((tokensArray.get(i) instanceof MinusToken) && i == 0)
                neg++;
        }
        for (left = tokensArray.size() - 1; left>= 0; left--) {
            if (tokensArray.get(left) instanceof RightParenToken)
                paren++;
            if (tokensArray.get(left) instanceof LeftParenToken)
                paren--;
            if (tokensArray.get(left) instanceof PlusToken && paren == 0)
                return new PlusExp(parseSubExp(getExp(0, left, tokensArray)),
                    parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof MinusToken && paren == 0 && left != 0)
                if (!(tokensArray.get(left - 1) instanceof MinusToken))
                    return new MinusExp(parseSubExp(getExp(0, left, tokensArray)), parseSubExp(getExp(left + 1, right + 1, tokensArray)));
        }
        for (left = tokensArray.size() - 1; left>= 0; left--) {
            if (tokensArray.get(left) instanceof RightParenToken)
                paren += 1;
            if (tokensArray.get(left) instanceof LeftParenToken)
                paren--;
            if (tokensArray.get(left) instanceof MultiplicationToken && paren == 0)
                return new MultiplicationExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof DivisionToken && paren == 0)
                return new DivisionExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
            if (tokensArray.get(left) instanceof ModulusToken && paren == 0)
                return new ModulusExp(parseSubExp(getExp(0, left, tokensArray)),parseSubExp(getExp(left + 1, right + 1, tokensArray)));
        }
        int parsenew = 0;
        if (tokensArray.get(parsenew) instanceof NewToken && tokensArray.size() - 1> parsenew) {
            String classname;
            List <Expression> parameters = new ArrayList <Expression> ();
            parsenew++;
            if (tokensArray.get(parsenew) instanceof IdentifierToken && tokensArray.size() - 1> parsenew) {
                classname = ((IdentifierToken) tokensArray.get(parsenew)).name;
                parsenew++;
            } else 
                throw new ParseException("Missing Classname after new keyword");
            if (tokensArray.get(parsenew) instanceof LeftParenToken && tokensArray.size() - 1> parsenew) 
                parsenew++;
            else 
                throw new ParseException("Missing LeftParen after classname");
            if (tokensArray.get(parsenew) instanceof RightParenToken)
                return new NewExp(classname, parameters);
            else {
                for (int i = parsenew; i <tokensArray.size(); i++) {
                    if (tokensArray.get(i) instanceof CommaToken || tokensArray.get(i) instanceof RightParenToken) {
                        parameters.add(parseSubExp(getExp(parsenew, i, tokensArray)));
                        parsenew = i + 1;
                    }
                }
            }
            return new NewExp(classname, parameters);
        }
		int parsemethod = 0;
		if (tokensArray.size() == 4 && tokensArray.get(0) instanceof IdentifierToken) 	//parsing for tokensArray expressions added! 
            return new ArrayIndexExp((IdentifierToken) tokensArray.get(left+1)); /* IdentifierToken [ exp ] */
        else if (tokensArray.get(parsemethod) instanceof IdentifierToken && tokensArray.size() - 1> parsemethod) {
            String objectName;
            String name;
            List <Expression> parameters = new ArrayList <Expression> ();
            objectName = ((IdentifierToken) tokensArray.get(parsemethod)).name;
            parsemethod++;
            if (tokensArray.get(parsemethod) instanceof DotOperatorToken && tokensArray.size() - 1> parsemethod) {
                parsemethod++;
                name = ((IdentifierToken) tokensArray.get(parsemethod)).name;
                parsemethod++;
            } else 
                throw new ParseException("Missing Identifier after dot operator");
            if (tokensArray.get(parsemethod) instanceof LeftParenToken && tokensArray.size() - 1> parsemethod)
                parsemethod++;
            if (tokensArray.get(parsemethod) instanceof RightParenToken && tokensArray.size() - 1> parsemethod)
                return new MethodCallExp(objectName, name, parameters);
            else {
                for (int i = parsemethod; i <tokensArray.size(); i++) {
                    if (tokensArray.get(i) instanceof CommaToken || tokensArray.get(i) instanceof RightParenToken) {
                        parameters.add(parseSubExp(getExp(parsemethod, i, tokensArray)));
                        parsemethod = i + 1;
                    }
                }
            }
            return new MethodCallExp(objectName, name, parameters);
        }
        for (left = tokensArray.size() - 1; left> 0; left--) 
            if (tokensArray.get(left) instanceof RightParenToken) 
                return new ParenthesizedExp(parseSubExp(getExp(1, tokensArray.size() - 1, tokensArray)));
        if (tokensArray.get(0) instanceof ThisToken && tokensArray.size() - 1> 0) 
            if (tokensArray.get(1) instanceof DotOperatorToken && tokensArray.size() - 1> 1) 
                return new ThisExp(parseSubExp(getExp(2, tokensArray.size(), tokensArray)));
        if (tokensArray.size() == 2 && tokensArray.get(0) instanceof MinusToken) {
            IntegerToken numb = (IntegerToken) tokensArray.get(left + 1);
            return new IntegerExp(numb.value * -1);
        }
        if (tokensArray.size() == 1 && tokensArray.get(0) instanceof IntegerToken) 
            return new IntegerExp((IntegerToken) tokensArray.get(left));
        if (tokensArray.size() == 1 && tokensArray.get(0) instanceof IdentifierToken) 
            return new VariableExp((IdentifierToken) tokensArray.get(left));
		
        if (tokensArray.size() == 1 && tokensArray.get(0) instanceof StringLiteralToken) 
            return new StringExp(((StringLiteralToken) tokensArray.get(parsemethod)).string);
        return null;
    }
}