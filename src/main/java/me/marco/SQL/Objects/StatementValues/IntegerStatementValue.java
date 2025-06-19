package me.marco.SQL.Objects.StatementValues;

import me.marco.SQL.Objects.StatementValue;

import java.sql.Types;

public class IntegerStatementValue extends StatementValue {

    public IntegerStatementValue(int value) {
        super(value);
    }

    @Override
    public int getType() {
        return Types.INTEGER;
    }

}