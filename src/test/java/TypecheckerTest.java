import java.util.*;

import parser.*;
import parser.defs.*;
import parser.statements.*;
import parser.expressions.*;

import tokenizer.*;
import tokenizer.tokens.*;

import typechecker.*;
import typechecker.types.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypecheckerTest {

    public static void testTypechecking(Program p) throws TypeErrorException {
        final Typechecker myTypechecker = new Typechecker(p);
    }
	
	@Test
	public void TypeTest() throws TypeErrorException {
		System.out.println("Testing All Types");
		//bool
		BoolType bool = new BoolType();
		bool.toString();
		bool.hashCode();
		//string
		StringType str = new StringType();
		str.toString();
		//bool.hashCode();
		//int
		IntType i = new IntType();
		i.toString();
		i.hashCode();
		//void
		VoidType v = new VoidType();
		v.toString();
		v.hashCode();
		v.equals(v);
		//object
		ObjectType o = new ObjectType("michael");
		o.toString();
		o.hashCode();
		o.equals(o);
		o.equals(i);
		//functions
		FunctionType f = new FunctionType(new IntType(),new IntType());
		FunctionType t = new FunctionType(new VoidType(),new IntType());
		FunctionType x = new FunctionType(new IntType(),new VoidType());
		f.toString();
		f.hashCode();
		f.equals(f);
		f.equals(i);
		t.equals(f);
		t.equals(x);
		x.equals(f);
	}

    @Test
    public void testInherit() {
        Type mytype = new IntType();
        assertTrue(mytype instanceof IntType);
    }

    @Test
    public void testSimplestProgram() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testValidPrimitiveDeclaration() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("int", "a"));
        mainStatements.add(new VariableDeclarationStmt("int", "b", new IntegerExp(1)));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("String", "a"));
        mainStatements.add(new VariableDeclarationStmt("String", "b", new StringExp("text")));

        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        myTypechecker = new Typechecker(myProgram);
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("Bool", "a"));
        mainStatements.add(new VariableDeclarationStmt("Bool", "b",
            new EqualEqualExp(new IntegerExp(1), new IntegerExp(1))));
        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testInvalidPrimitiveDeclaration() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("int", "a", new StringExp("text")));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("TypeErrorException should have been thrown");
        } catch (TypeErrorException e) {}
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("String", "a", new IntegerExp(1)));
        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("TypeErrorException should have been thrown");
        } catch (TypeErrorException e) {}
    }

    @Test
    public void testValidPrimitiveAssignment() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("int", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new IntegerExp(1)));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("String", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new StringExp("text")));
        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        myTypechecker = new Typechecker(myProgram);
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("Bool", "a"));
        mainStatements.add(new VariableAssignmentStmt("a",
            new EqualEqualExp(new IntegerExp(1), new IntegerExp(1))));
        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testInvalidPrimitiveAssignment() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("int", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new StringExp("text")));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("TypeErrorException should have been thrown");
        } catch (TypeErrorException e) {}
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("String", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new IntegerExp(1)));
        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("TypeErrorException should have been thrown");
        } catch (TypeErrorException e) {}
    }

    @Test
    public void testPrintStatements() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new IntegerExp(1)));
        mainStatements.add(new ClogStmt(new StringExp("text")));
        mainStatements.add(new CloglnStmt(new IntegerExp(1)));
        mainStatements.add(new CloglnStmt(new StringExp("text")));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testPlusStatements() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new PlusExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new MinusExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new ModulusExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new MultiplicationExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new DivisionExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new GreaterThanExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new GreaterThanOrEqualExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new LessThanExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ClogStmt(new LessThanOrEqualExp(new IntegerExp(1), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testWhileLoops() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new WhileStmt(new EqualEqualExp(new IntegerExp(1), new IntegerExp(1)),
            new BlockStmt(new ArrayList < Statement > ())));

        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testIfElse() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new IfStmt(new EqualEqualExp(new IntegerExp(1), new IntegerExp(1)),
            new BlockStmt(new ArrayList < Statement > ())));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
        mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new IfElseStmt(new EqualEqualExp(new IntegerExp(1), new IntegerExp(1)),
            new BlockStmt(new ArrayList < Statement > ()), new BlockStmt(new ArrayList < Statement > ())));
        myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testValidSubtypingAssignment2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < Statement > methodStmts = new ArrayList < Statement > ();
        methodStmts.add(new CloglnStmt(new StringExp("Hello World")));
        methods.add(new MethodDef("int", "methodTest", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(methodStmts), new IntegerExp(0)));
        methods.add(new MethodDef("int", "methodTest", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(methodStmts), new IntegerExp(0)));
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        classdefs.add(parent);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testValidSubtypingAssignment() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "ParentClass", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadSubtyping1() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        fields.add(new VariableDeclarationStmt("int", "x"));
        fields.add(new VariableDeclarationStmt("int", "x"));
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "ParentClass", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadSubtyping2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("b", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "ParentClass", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadSubtyping4() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "b"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "ParentClass", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadSubtyping3() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClas", "ParentClass", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadInheritance1() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
	
    @Test(expected = TypeErrorException.class)
    public void testBadInheritance2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "ChildClass", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testInvalidSubtypingAssignment() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef child = new ClassDef("ChildClass", "", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testDuplicateVariableName() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        classdefs.add(parent);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testDuplicateClassDefinition() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        mainStatements.add(new VariableDeclarationStmt("ParentClass", "a"));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef dup = new ClassDef("ParentClass", "", fields, constructor, methods);
        ClassDef dup1 = new ClassDef("ParentClass", "", fields, constructor, methods);
        classdefs.add(dup);
        classdefs.add(dup1);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new MethodCallExp("rust", "cust", new ArrayList < Expression > (Arrays.asList(new VariableExp("x"), new VariableExp("x"))))));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testMethodStuff() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new MethodCallExp("rust", "cust", new ArrayList < Expression > (Arrays.asList(new VariableExp("x"), new VariableExp("x"))))));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testMethodStuff2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new MethodCallExp("this", "cust", new ArrayList < Expression > (Arrays.asList(new VariableExp("x"), new VariableExp("x"))))));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testMethodStuff3() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("int", "b"));
        mainStatements.add(new VariableDeclarationStmt("int", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new VariableExp("b")));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        fields.add(new VariableDeclarationStmt("int", "a"));
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadIf() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new IfStmt(new PlusExp(new IntegerExp(1), new IntegerExp(1)),
            new BlockStmt(new ArrayList < Statement > ())));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
    @Test(expected = TypeErrorException.class)
    public void testBadWhile() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new WhileStmt(new StringExp("r"),
            new BlockStmt(new ArrayList < Statement > ())));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testValidPolymorphismMethodCall() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("ChildClass", "a", new NewExp("ChildClass", new ArrayList < Expression > ())));
        mainStatements.add(new VariableDeclarationStmt("int", "x", new MethodCallExp("a", "parentMethod", new ArrayList < Expression > ())));
        ArrayList < VariableDeclarationStmt > fields = new ArrayList < VariableDeclarationStmt > ();
        Constructor constructor = new
        Constructor(fields, new BlockStmt(new ArrayList < Statement > ()));
        ArrayList < MethodDef > methods = new ArrayList < MethodDef > ();
        ArrayList < ClassDef > classdefs = new ArrayList < ClassDef > ();
        ClassDef child = new ClassDef("ChildClass", "ParentClass", fields, constructor, new ArrayList < MethodDef > ());
        MethodDef parentMethod = new MethodDef("int", "parentMethod", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0));
        methods.add(parentMethod);
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);
        classdefs.add(parent);
        classdefs.add(child);
        Program myProgram = new Program(classdefs,
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadPlus() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new PlusExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadPlus2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new PlusExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }


    @Test(expected = TypeErrorException.class)
    public void testBadMinus() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new MinusExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadMinus2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new MinusExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadModulus() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new ModulusExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadModulus2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new ModulusExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadMultiplication() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new MultiplicationExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadMultiplication2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new MultiplicationExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadDivision() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new DivisionExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadDivision2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new DivisionExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadGreaterThan() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new GreaterThanExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
    @Test(expected = TypeErrorException.class)
    public void testBadGreaterThan2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new GreaterThanExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadGreaterThanOrEqual() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new GreaterThanOrEqualExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadGreaterThanOrEqual2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new GreaterThanOrEqualExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadLessThan() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new LessThanExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadLessThan2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new LessThanExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test(expected = TypeErrorException.class)
    public void testBadLessThanOrEqual() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new LessThanOrEqualExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
	
    @Test(expected = TypeErrorException.class)
    public void testBadLessThanOrEqual2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new LessThanOrEqualExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
	
    @Test(expected = TypeErrorException.class)
    public void testBadEqualEqual() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new EqualEqualExp(new IntegerExp(1), new StringExp("1"))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
	
    @Test(expected = TypeErrorException.class)
    public void testBadEqualEqual2() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new ClogStmt(new EqualEqualExp(new StringExp("1"), new IntegerExp(1))));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
	
    @Test(expected = TypeErrorException.class)
    public void testBadPrimitiveDeclaration() throws TypeErrorException {
        List < Statement > mainStatements = new ArrayList < Statement > ();
        mainStatements.add(new VariableDeclarationStmt("rust", "a"));
        Program myProgram = new Program(new ArrayList < ClassDef > (),
            new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (),
                new BlockStmt(mainStatements), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }


    @Test(expected = TypeErrorException.class)
    public void testBadMethod() throws TypeErrorException {
        Program myProgram = new Program(new ArrayList < ClassDef > (), new MethodDef("void", "main", new ArrayList < VariableDeclarationStmt > (),
            new BlockStmt(new ArrayList < Statement > (Arrays.asList(new ReturnVoidStmt())))));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }
	
    @Test(expected = TypeErrorException.class)
    public void ConstructorTesting() throws TypeErrorException {
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
        Typechecker myTypechecker = new Typechecker(expected);
    }
	
    @Test(expected = TypeErrorException.class)
    public void ConstructorTesting2() throws TypeErrorException {
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
        Typechecker myTypechecker = new Typechecker(expected);
    }

    @Test(expected = TypeErrorException.class)
    public void FieldTest() throws TypeErrorException {
        ArrayList < ClassDef > classDefs = new ArrayList < ClassDef > ();
        ArrayList < VariableDeclarationStmt > fieldDefs = new ArrayList < VariableDeclarationStmt > ();
        fieldDefs.add(new VariableDeclarationStmt("int", "a"));
        fieldDefs.add(new VariableDeclarationStmt("int", "a"));
        ArrayList < VariableDeclarationStmt > constructorParams = new ArrayList < VariableDeclarationStmt > ();
        constructorParams.add(new VariableDeclarationStmt("int", "b"));
        constructorParams.add(new VariableDeclarationStmt("String", "y"));
        ArrayList < Statement > constructorStmts = new ArrayList < Statement > ();
        constructorStmts.add(new VariableAssignmentStmt("a", new VariableExp("b")));
        constructorStmts.add(new VariableAssignmentStmt("x", new VariableExp("y")));
        classDefs.add(new ClassDef("TestClass", fieldDefs, new Constructor(constructorParams, new BlockStmt(constructorStmts)), new ArrayList < MethodDef > ()));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList < VariableDeclarationStmt > (), new BlockStmt(new ArrayList < Statement > ()), new IntegerExp(0)));
        Typechecker myTypechecker = new Typechecker(expected);
    }
}