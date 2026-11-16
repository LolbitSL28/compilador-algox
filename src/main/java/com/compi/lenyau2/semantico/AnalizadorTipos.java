package com.compi.lenyau2.semantico;

public class AnalizadorTipos {

    public static boolean esNumerico(TipoDato t) {
        return t == TipoDato.INT || t == TipoDato.REAL;
    }

    public static TipoDato aritmetico(String op, TipoDato izq, TipoDato der) {
        if (izq == TipoDato.ERROR || der == TipoDato.ERROR)
            return TipoDato.ERROR;
        if (op.equals("+") && izq == TipoDato.STRING && der == TipoDato.STRING)
            return TipoDato.STRING;
        if (izq == TipoDato.INT && der == TipoDato.INT)
            return TipoDato.INT;
        if (esNumerico(izq) && esNumerico(der))
            return TipoDato.REAL;
        return TipoDato.ERROR;
    }

    public static TipoDato relacional(TipoDato izq, TipoDato der) {
        if (izq == TipoDato.ERROR || der == TipoDato.ERROR)
            return TipoDato.ERROR;
        if (esNumerico(izq) && esNumerico(der))
            return TipoDato.INT;
        if (izq == TipoDato.STRING && der == TipoDato.STRING)
            return TipoDato.INT;
        return TipoDato.ERROR;
    }

    public static TipoDato logico(TipoDato izq, TipoDato der) {
        if (izq == TipoDato.ERROR || der == TipoDato.ERROR)
            return TipoDato.ERROR;
        if (izq == TipoDato.INT && der == TipoDato.INT)
            return TipoDato.INT;
        return TipoDato.ERROR;
    }

    public static TipoDato not(TipoDato t) {
        if (t == TipoDato.ERROR)
            return TipoDato.ERROR;
        if (t == TipoDato.INT)
            return TipoDato.INT;
        return TipoDato.ERROR;
    }

    public static boolean asignable(TipoDato tipoVar, TipoDato tipoExpr) {
        if (tipoExpr == TipoDato.ERROR)
            return true;
        return tipoVar == tipoExpr || (tipoVar == TipoDato.REAL && tipoExpr == TipoDato.INT);
    }

    public static boolean esCondicionValida(TipoDato t) {
        if (t == TipoDato.ERROR)
            return true;
        return esNumerico(t);
    }
}