package me.marco.SQL.Objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Transaction extends Query {

    private List<Statement> statements;

    public Transaction(List<Statement> statements) {
        super(null);
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void execute(Connection conn) {
        PreparedStatement statement = null;
        try {
            conn.setAutoCommit(false);
            Transaction transaction = (Transaction) this;
            for (Statement s : transaction.getStatements()) {
                statement = conn.prepareStatement(s.getQuery());
                for (int i = 1; i <= s.getValues().length; i++) {
                    StatementValue val = s.getValues()[i - 1];
                    statement.setObject(i, val.getValue(), val.getType());
                }
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}