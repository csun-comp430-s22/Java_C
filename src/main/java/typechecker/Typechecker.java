package typechecker;
import java.util.*;
import typechecker.types.*;
import parser.*;
import parser.defs.*;
import parser.statements.*;
import parser.expressions.*;

public class Typechecker {
    // Things to track:
    // 1.) Variables in scope, and their types
    // 2.) Classes available, parameters constructors take, methods they have, what their parent
    //     class is.
    // Sorts of queries we want to make to class information:
    // 1. Is this a valid class?
    // 2. For this class, what are the argument types for the constructor?
    // 3. Does this class support a given method?  If so, what are the parameter
    //    types for the method?
    //    - Need to take inheritance into account
    // 4. Is this given class a subclass of another class?
    // 5. Does our class hierarchy form a tree?	
    private final Map < String, ClassDef > classes;
    //public final Program program;
    // recommended: ClassName -> All Methods on the Class
    // recommended: ClassName -> ParentClass
    public Typechecker(final Program program) throws TypeErrorException {
        classes = new HashMap < String, ClassDef > ();
        for (final ClassDef classdef: program.classDefs) { //check for duplicate classes.
            if (!classes.containsKey(classdef.className))
                classes.put(classdef.className, classdef);
            else throw new TypeErrorException("Duplicate class definition: " + classdef.className);
        }
        typeofProgram(program);
    }

    // Doesn't handle access modifiers right now; would be to know which class we
    // are calling from.
    //
    // class Base extends Object {
    //   public void basePublic() {}
    //   protected void baseProtected() {}
    //   private void basePrivate() {}
    // }
    // class Sub extends Base {
    //   public void foobar() {
    //     this.basePublic();  // should be ok
    //     this.baseProtected(); // should be ok
    //     this.basePrivate(); // should give an error
    //   }
    // }
    // class SomeOtherClass extends Object {
    //   public void test() {
    //     Sub sub = new Sub();
    //     sub.basePublic(); // should be ok
    //     sub.baseProtected(); // should give an error
    //     sub.basePrivate(); // should give an error
    //   }
    // }
    //
    // doesn't handle inherited methods
    // for every class:
    //   - Methods on that class
    //   - Methods on the parent of that class

    public void typeofProgram(final Program program) throws TypeErrorException {
        for (final ClassDef classdef: program.classDefs)
            typeofClass(classdef);
        typeofMethod(program.entryPoint, new HashMap < String, MethodDef > (), new HashMap < String, Type > ());
    }

    // check methods
    // TODO - this is broken - doesn't check for methods with duplicate names (FIXED)
    //
    // int foo(int x) { ... }
    // int foo(bool b) { ... }

    // 1.) target should be a class.
    // 2.) target needs to have the methodname method
    // 3.) need to know the expected parameter types for the method
    //
    // exp.methodname(exp*)
    // target.methodName(params)

    public void typeofClass(final ClassDef classdef) throws TypeErrorException {
        final Map < String, Type > variables = new HashMap < String, Type > ();
        ClassDef parentDef = null;
        if (classdef.parent != "") {
            parentDef = classes.get(classdef.parent);
            classdef.fields.addAll(parentDef.fields);
        }
        for (final VariableDeclarationStmt parameter: classdef.fields) {
            if (!variables.containsKey(parameter.name)) {
                Type paramType = toString(parameter.type);
                variables.put(parameter.name, paramType);
            } else throw new TypeErrorException("Duplicate parameter name");
        }
        if (classdef.parent != "") {
            if (classdef.className.equals(classdef.parent))
                throw new TypeErrorException("Class inheriting itself");
        }
        final Map < String, Type > constructorParameters = new HashMap < String, Type > ();
        for (final VariableDeclarationStmt parameter: classdef.constructor.parameters) {
            if (!constructorParameters.containsKey(parameter.name)) {
                Type paramType = toString(parameter.type);
                constructorParameters.put(parameter.name, paramType);
            } else throw new TypeErrorException("Duplicate parameter names");
        }
        final Map < String, MethodDef > methods = new HashMap < String, MethodDef > ();
        if (classdef.parent != "") {
            parentDef = classes.get(classdef.parent);
            classdef.methods.addAll(parentDef.methods);
        }
        for (final MethodDef method: classdef.methods) {
            if (!methods.containsKey(method.name))
                methods.put(method.name, method);
            else throw new TypeErrorException("Duplicate method names");
        }
        typeofStmts(variables, methods, classdef.constructor.body);
        for (final MethodDef method: classdef.methods)
            typeofMethod(method, methods, variables);
    }

    public void typeofMethod(MethodDef method, Map < String, MethodDef > classMethods, Map < String, Type > classFields) throws TypeErrorException {
        final Map < String, Type > variables = new HashMap < String, Type > ();
        variables.putAll(classFields);
        for (final VariableDeclarationStmt parameter: method.parameters) {
            if (!variables.containsKey(parameter.name)) {
                Type paramType = toString(parameter.type);
                variables.put(parameter.name, paramType);
            } else throw new TypeErrorException("Duplicate parameter names");
        }
        final Map < String, Type > finalGamma = typeofStmts(variables, classMethods, method.body);
        final Type actualReturnType = typeofExp(finalGamma, classMethods, method.returnExp);
        if (!actualReturnType.equals(toString(method.type)))
            throw new TypeErrorException("Return type mismatch");
    }

    private void typeofParams(final Map < String, Type > initial, final List < VariableDeclarationStmt > formalParams, final List < Expression > actualParams, final Map < String, MethodDef > classMethods) throws TypeErrorException {
        if (formalParams.size() == actualParams.size()) {
            final Iterator < VariableDeclarationStmt > formalIterator = formalParams.iterator();
            final Iterator < Expression > actualIterator = actualParams.iterator();
            while (formalIterator.hasNext() && actualIterator.hasNext()) {
                final VariableDeclarationStmt formalParam = formalIterator.next();
                final Expression actualParam = actualIterator.next();
                final Type actualType = typeofExp(initial, classMethods, actualParam);
                if (!actualType.equals(toString(formalParam.type)))
                    throw new TypeErrorException("Types incompatible in parameters");
            }
            assert(!formalIterator.hasNext());
            assert(!actualIterator.hasNext());
        } else throw new TypeErrorException("Wrong number of parameters");
    }

    public Map < String, Type > typeofStmts(Map < String, Type > initial, Map < String, MethodDef > classMethods, final BlockStmt body) throws TypeErrorException {
        for (final Statement s: body.body)
            initial = typeofStmt(initial, classMethods, s);
        return initial;
    }

    public Map < String, Type > typeofStmt(final Map < String, Type > initial, Map < String, MethodDef > classMethods, final Statement s) throws TypeErrorException {
        if (s instanceof IfElseStmt) {
            final IfElseStmt asIf = (IfElseStmt) s;
            final Type guardType = typeofExp(initial, classMethods, asIf.condition);
            if (guardType instanceof BoolType && asIf.falseBranch != null) {
                typeofStmts(initial, classMethods, asIf.trueBranch);
                typeofStmts(initial, classMethods, asIf.falseBranch);
            }
            return initial;
        } else if (s instanceof IfStmt) {
            final IfStmt asIf = (IfStmt) s;
            final Type guardType = typeofExp(initial, classMethods, asIf.condition);
            if (guardType instanceof BoolType)
                typeofStmts(initial, classMethods, asIf.trueBranch);
            else throw new TypeErrorException("guard of if is not a boolean");
            return initial;
        } else if (s instanceof CloglnStmt) {
            final CloglnStmt asClogln = (CloglnStmt) s;
            final Type guardType = typeofExp(initial, classMethods, asClogln.output);
            return initial;
        } else if (s instanceof ClogStmt) {
            final ClogStmt asClog = (ClogStmt) s;
            final Type guardType = typeofExp(initial, classMethods, asClog.output);
            return initial;
        } else if (s instanceof ReturnStmt) {
            final ReturnStmt asReturn = (ReturnStmt) s;
            final Type guardType = typeofExp(initial, classMethods, asReturn.value);
            if (!(guardType instanceof Type))
                throw new TypeErrorException("method must have return value of " + guardType);
            return initial;
        } else if (s instanceof ReturnVoidStmt) {
            final VoidType guardType = new VoidType();
            if (!(guardType instanceof Type))
                throw new TypeErrorException("method must have return value of " + guardType);
            return initial;
        } else if (s instanceof WhileStmt) {
            final WhileStmt asWhile = (WhileStmt) s;
            final Type guardType = typeofExp(initial, classMethods, asWhile.condition);
            if (guardType instanceof BoolType)
                typeofStmts(initial, classMethods, asWhile.body);
            else throw new TypeErrorException("guard of while is not a boolean");
            return initial;
        } else if (s instanceof VariableDeclarationStmt) {
            VariableDeclarationStmt varDec = (VariableDeclarationStmt) s;
            final Map < String, Type > newinitial = new HashMap < String, Type > ();
            newinitial.putAll(initial);
            Type varType = toString(varDec.type);
            if (!newinitial.containsKey(varDec.name)) {
                if (varDec.value == null) {
                    newinitial.put(varDec.name, toString(varDec.type));
                    return newinitial;
                } else if (varType.equals(typeofExp(initial, classMethods, varDec.value))) {
                    newinitial.put(varDec.name, toString(varDec.type));
                    return newinitial;
                } else throw new TypeErrorException("Invalid variable type");
            } else throw new TypeErrorException("Duplicate variable names");
        } else if (s instanceof VariableAssignmentStmt) {
            VariableAssignmentStmt varDec = (VariableAssignmentStmt) s;
            Type varType = initial.get(varDec.name);
            Type valueType = typeofExp(initial, classMethods, varDec.value);
            if (initial.containsKey(varDec.name)) {
                if (varType.equals(valueType))
                    return initial;
                else if (valueType instanceof ObjectType) {
                    String childName = ((ObjectType) valueType).name;
                    if (classes.containsKey(childName)) {
                        String childinheritance = classes.get(childName).parent;
                        if (childinheritance.length() > 0) {
                            Type parentType = toString(childinheritance);
                            if (varType.equals(parentType))
                                return initial;
                            else throw new TypeErrorException("Invalid variable type");
                        } else throw new TypeErrorException("Invalid variable type");
                    } else throw new TypeErrorException("Invalid variable type");
                } else throw new TypeErrorException("Invalid variable type");
            } else throw new TypeErrorException("Variable does not exist");
        }
        throw new TypeErrorException("Unrecognized statement");
    }

    public Type typeofExp(final Map < String, Type > initial, final Map < String, MethodDef > classMethods, final Expression e) throws TypeErrorException {
        // (leftType, exp.op, rightType) match {
        //   case (IntType, PlusOp, IntType) => IntType
        //   case (IntType, LessThanOp | EqualsOp, IntType) => Booltype
        //   case _ => throw new TypeErrorException("Operator mismatch")
        // }
        if (e instanceof IntegerExp)
            return new IntType();
        if (e instanceof StringExp)
            return new StringType();
        if (e instanceof VariableExp) {
            VariableExp variable = (VariableExp) e;
            return initial.get(variable.name);
        } else if (e instanceof PlusExp) {
            PlusExp asBinOpExp = (PlusExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new IntType();
            else throw new TypeErrorException("Operand type mismatch for +");
        } else if (e instanceof MinusExp) {
            MinusExp asBinOpExp = (MinusExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new IntType();
            else throw new TypeErrorException("Operand type mismatch for -");
        } else if (e instanceof ModulusExp) {
            ModulusExp asBinOpExp = (ModulusExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new IntType();
            else throw new TypeErrorException("Operand type mismatch for %");
        } else if (e instanceof MultiplicationExp) {
            MultiplicationExp asBinOpExp = (MultiplicationExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new IntType();
            else throw new TypeErrorException("Operand type mismatch for *");
        } else if (e instanceof DivisionExp) {
            DivisionExp asBinOpExp = (DivisionExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new IntType();
            else throw new TypeErrorException("Operand type mismatch for /");
        } else if (e instanceof GreaterThanExp) {
            GreaterThanExp asBinOpExp = (GreaterThanExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new BoolType();
            else throw new TypeErrorException("Operand type mismatch for >");
        } else if (e instanceof GreaterThanOrEqualExp) {
            GreaterThanOrEqualExp asBinOpExp = (GreaterThanOrEqualExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new BoolType();
            else throw new TypeErrorException("Operand type mismatch for >=");
        } else if (e instanceof LessThanExp) {
            LessThanExp asBinOpExp = (LessThanExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new BoolType();
            else throw new TypeErrorException("Operand type mismatch for <");
        } else if (e instanceof LessThanOrEqualExp) {
            LessThanOrEqualExp asBinOpExp = (LessThanOrEqualExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new BoolType();
            else throw new TypeErrorException("Operand type mismatch for <=");
        } else if (e instanceof EqualEqualExp) {
            EqualEqualExp asBinOpExp = (EqualEqualExp) e;
            final Type leftType = typeofExp(initial, classMethods, asBinOpExp.exp1);
            final Type rightType = typeofExp(initial, classMethods, asBinOpExp.exp2);
            if (leftType instanceof IntType && rightType instanceof IntType)
                return new BoolType();
            else throw new TypeErrorException("Operand type mismatch for ==");
        } else if (e instanceof VariableExp) {
            final VariableExp variable = (VariableExp) e;
            if (initial.containsKey(variable.name)) {
                final Type expected = initial.get(variable.name);
                return expected;
            } else throw new TypeErrorException("Used variable not in scope: " + variable.name);
        } else if (e instanceof MethodCallExp) {
            final MethodCallExp call = (MethodCallExp) e;
            String objectName = call.objectName;
            if (objectName == "this" && classMethods.containsKey(call.name)) {
                final MethodDef methodDef = classMethods.get(objectName);
                typeofParams(initial, methodDef.parameters, call.parameters, classMethods);
                return toString(methodDef.type);
            } else if (initial.containsKey(objectName)) {
                final Type expected = initial.get(objectName);
                String objectType;
                if (expected instanceof ObjectType) {
                    objectType = ((ObjectType) expected).name;
                    ClassDef classDef = classes.get(objectType);
                    MethodDef methodDef = null;
                    for (final MethodDef method: classDef.methods) {
                        if (method.name.equals(call.name))
                            methodDef = method;
                    }
                    if (methodDef != null) {
                        typeofParams(initial, methodDef.parameters, call.parameters, classMethods);
                        return toString(methodDef.type);
                    } else throw new TypeErrorException("Unrecognized method: " + call.name);
                } else throw new TypeErrorException("Unrecognized method: " + call.name);
            } else throw new TypeErrorException("Unrecognized method: " + call.name);
        } else if (e instanceof NewExp) {
            NewExp asNewExp = (NewExp) e;
            if (classes.containsKey(asNewExp.classname))
                return toString(asNewExp.classname);
            else throw new TypeErrorException("Unrecognized classname");
        }
        throw new TypeErrorException("Unrecognized expression: " + e);
    }

    public Type toString(String input) throws TypeErrorException {
        if (input.equals("int")) return new IntType();
        else if (input.equals("Bool")) return new BoolType();
        else if (input.equals("String")) return new StringType();
        else if (input.equals("Void")) return new VoidType();
        else if (classes.containsKey(input)) return new ObjectType(input);
        else throw new TypeErrorException("Unrecognized type: " + input);
    }
}