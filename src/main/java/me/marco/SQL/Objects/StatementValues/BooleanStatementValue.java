package me.marco.SQL.Objects.StatementValues;

import me.marco.SQL.Objects.StatementValue;

import java.sql.Types;

public class BooleanStatementValue extends StatementValue {

    public BooleanStatementValue(boolean value) {
        super(value);
    }

    @Override
    public int getType() {
        return Types.TINYINT;
    }

}