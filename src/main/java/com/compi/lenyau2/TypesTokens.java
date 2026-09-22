package com.compi.lenyau2;

public enum TypesTokens {
    IDENTIFICADOR(100), ENTERO(101), DECIMAL(102),
    SUMA(103), RESTA(104), MULTIPLICACION(105), DIVISION(106),
    IGUAL(107), DIFERENTE(108), MAYOR_QUE(109), MENOR_QUE(110), MAYOR_IGUAL(111), MENOR_IGUAL(112),
    ASIGNACION(113), PUNTO_Y_COMA(114), COMA(115),
    LLAVE_IZQUIERDA(116), LLAVE_DERECHA(117),
    PARENTESIS_IZQUIERDO(118), PARENTESIS_DERECHO(119),
    CADENA(120),
    AND(200), OR(201), NOT(202),
    IF(203), THEN(204), ELSE(205), WHILE(206), DO(207), REPEAT(208), UNTIL(209), TO(210), STEP(211),
    EXIT(212), WRITE(213), READ(214),
    INT(215), REAL(216), STRING(217);

    private int number;

    TypesTokens(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }
}
