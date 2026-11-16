package com.compi.lenyau2.semantico;

public enum TipoDato {
    INT, REAL, STRING, ERROR;

    public static TipoDato desdeString(String tipo) {
        if (tipo == null)
            return ERROR;
        switch (tipo.toLowerCase()) {
            case "int":
                return INT;
            case "real":
                return REAL;
            case "string":
                return STRING;
            default:
                return ERROR;
        }
    }
}