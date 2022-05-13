
package parser.statements;

public class ReturnVoidStmt implements Statement {
    public String toString() {
        return "ReturnVoidStmt";
    }

    public boolean equals(Statement stmt) {
        return stmt instanceof ReturnVoidStmt;
    }
}