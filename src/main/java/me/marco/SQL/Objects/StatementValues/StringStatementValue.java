package me.marco.SQL.Objects.StatementValues;

import me.marco.SQL.Objects.StatementValue;

import java.sql.Types;

public class StringStatementValue extends StatementValue {

    public StringStatementValue(String value) {
        super(value);
    }

    @Override
    public int getType() {
        return Types.VARCHAR;
    }

}