package parser.defs;
import java.util.ArrayList;

public class Program{
    public final ArrayList<ClassDef> classDefs;
    public final MethodDef entryPoint;

    public Program(final ArrayList<ClassDef> classDefs, final MethodDef entryPoint){
        this.classDefs = classDefs;
        this.entryPoint = entryPoint;
    }

    public String toString(){
        return "Program(" + classDefsString() + ", " + entryPoint + ")";
    }

    private String classDefsString() {
        String ret = "";
        if (classDefs.size() >= 1) {
            ret += classDefs.get(0).toString();
        }
        for (int i = 1; i < classDefs.size(); i++) {
            ret += ", " + classDefs.get(i).toString();
        }
        return ret;
    }

    public boolean equals(Program program) {
        return toString().equals(program.toString());
    }
}