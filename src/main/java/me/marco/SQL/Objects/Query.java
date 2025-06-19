package me.marco.SQL.Objects;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Query {

    private Statement stmt;
    public Query(Statement stmt) {
        this.stmt = stmt;
    }

    public Statement getStatment() {
        return stmt;
    }

    public void setStatment(Statement stmt) {
        this.stmt = stmt;
    }

    public void execute(Connection conn) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(this.stmt.getQuery());
            for (int i = 1; i <= this.stmt.getValues().length; i++) {
                StatementValue val = this.stmt.getValues()[i - 1];
                statement.setObject(i, val.getValue(), val.getType());
            }
            statement.executeUpdate();
            statement.close();;
        } catch (SQLException ex) {
            System.out.println(statement);
            ex.printStackTrace();
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}