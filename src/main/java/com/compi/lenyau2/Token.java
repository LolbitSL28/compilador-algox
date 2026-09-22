package com.compi.lenyau2;

public class Token {
    private TypesTokens type;
    private String value;
    private int line;

    public Token(TypesTokens type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TypesTokens getType() {
        return type;
    }

    public int getTypeTokenNumber() {
        return type.getNumber();
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

}
