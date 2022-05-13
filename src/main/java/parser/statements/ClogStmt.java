package parser.statements;
import parser.expressions.Expression;
public class ClogStmt implements Statement {
    public final Expression output;

    public ClogStmt(Expression output) {
        this.output = output;
    }

    public String toString() {
        return "ClogStmt(" + output + ")";
    }

    public boolean equals(Statement stmt) {
        return toString().equals(stmt.toString());
    }
}