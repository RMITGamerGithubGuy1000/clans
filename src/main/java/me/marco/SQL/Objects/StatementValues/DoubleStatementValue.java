package me.marco.SQL.Objects.StatementValues;

import me.marco.SQL.Objects.StatementValue;

import java.sql.Types;

public class DoubleStatementValue extends StatementValue {

    public DoubleStatementValue(double value) {
        super(value);
    }

    @Override
    public int getType() {
        return Types.DOUBLE;
    }

}