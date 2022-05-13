package parser.defs;

import java.util.ArrayList;
import parser.statements.VariableDeclarationStmt;

public class ClassDef {

    public final String className;
    public final String parent;
    public final ArrayList<VariableDeclarationStmt> fields;
    public final Constructor constructor;
    public final ArrayList<MethodDef> methods;

    public ClassDef(final String className, final String parent, final ArrayList<VariableDeclarationStmt> fields,
            final Constructor constructor, final ArrayList<MethodDef> methods) {
        this.className = className;
        this.parent = parent;
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

    public ClassDef(final String className, final ArrayList<VariableDeclarationStmt> fields, final Constructor constructor,
            final ArrayList<MethodDef> methods) {
        this.className = className;
        this.parent = "";
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

    public String toString() {
        return "ClassDef(" + className + ", " + parent + ", " + fieldsString() + ", " + constructor + ", "+ methodsString() + ")";
    }

    private String fieldsString() {
        String ret = "";
        if (fields.size() >= 1) {
            ret += fields.get(0).toString();
        }
        for (int i = 1; i < fields.size(); i++) {
            ret += ", " + fields.get(i).toString();
        }
        return ret;
    }

    private String methodsString() {
        String ret = "";
        if (methods.size() >= 1) {
            ret += methods.get(0).toString();
        }
        for (int i = 1; i < methods.size(); i++) {
            ret += ", " + methods.get(i).toString();
        }
        return ret;
    }

    public boolean equals(ClassDef classDef) {
        return (this.toString()).equals(classDef.toString());
    }
}