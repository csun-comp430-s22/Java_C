package parser.statements;
import java.util.List;

public class BlockStmt implements Statement {
    public final List<Statement> body;

    public BlockStmt(final List<Statement> body) {
        this.body = body;
    }

    public String toString() {
        return "BlockStmt( " + bodyString() + ")";  
    }

    private String bodyString(){
        String ret = "";
        if(body.size() >= 1)
            ret += body.get(0).toString();
        for(int i = 1; i < body.size(); i++)
            ret += ", " + body.get(i).toString();
        return ret;
    }

    public boolean equals(final Statement exp) {
        return (this.toString()).equals(exp.toString());
    }
}
