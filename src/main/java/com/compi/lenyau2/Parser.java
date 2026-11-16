package com.compi.lenyau2;

import java.util.ArrayList;
import java.util.List;

import com.compi.lenyau2.ast.*;

public class Parser {
    private static class ParseError extends RuntimeException {
    }

    private List<Token> tokens;
    private List<String> errores = new ArrayList<>();
    private int position;
    private Token actual;

    public Parser(TokenList listaTokens) {
        this.tokens = new ArrayList<>();
        TokenList current = listaTokens;
        while (current != null) {
            tokens.add(current.getToken());
            current = current.getNext();
        }
        this.position = 0;
        this.actual = tokens.isEmpty() ? null : tokens.get(0);
    }

    public List<String> getErrores() {
        return errores;
    }

    private void avanzar() {
        position++;
        actual = (position < tokens.size()) ? tokens.get(position) : null;
    }

    private boolean check(TypesTokens tipo) {
        return actual != null && actual.getType() == tipo;
    }

    private boolean aceptar(TypesTokens tipo) {
        if (check(tipo)) {
            avanzar();
            return true;
        }
        return false;
    }

    private Token esperar(TypesTokens tipo, String mensajeError) {
        if (check(tipo)) {
            Token t = actual;
            avanzar();
            return t;
        }
        throw error(mensajeError + " (se encontro "
                + (actual == null ? "EOF" : actual.getType() + " '" + actual.getValue() + "'") + ")");
    }

    private ParseError error(String msg) {
        int linea = (actual != null) ? actual.getLine() : -1;
        errores.add("Error sintactico [linea " + linea + "]: " + msg);
        return new ParseError();
    }

    private void sincronizar() {
        avanzar();

        while (actual != null) {
            if (tokens.get(position - 1).getType() == TypesTokens.PUNTO_Y_COMA)
                return;

            switch (actual.getType()) {
                case IF:
                case WHILE:
                case REPEAT:
                case FOR_:
                case INT:
                case REAL:
                case STRING:
                case EXIT:
                case WRITE:
                case READ:
                    return;
                default:
                    avanzar();
            }
        }
    }

    public Programa parse() {
        List<NodoAST> sentencias = listaSentencia();
        if (actual != null) {
            error("Se esperaba fin de archivo");
        }
        return new Programa(sentencias);
    }

    private List<NodoAST> listaSentencia() {
        List<NodoAST> lista = new ArrayList<>();
        while (canStart()) {
            try {
                lista.add(sentencia());
            } catch (ParseError ex) {
                sincronizar();
            }
        }
        return lista;
    }

    private boolean canStart() {
        if (actual == null)
            return false;
        switch (actual.getType()) {
            case IDENTIFICADOR:
            case INT:
            case REAL:
            case STRING:
            case IF:
            case WHILE:
            case REPEAT:
            case FOR_:
            case EXIT:
            case WRITE:
            case READ:
            case LLAVE_IZQUIERDA:
                return true;
            default:
                return false;
        }
    }

    private NodoAST sentencia() {
        if (actual == null)
            error("Se esperaba una sentencia");

        switch (actual.getType()) {
            case INT:
            case REAL:
            case STRING:
                return sentenciaDeclaracion();
            case IF:
                return sentenciaSi();
            case WHILE:
                return sentenciaMientras();
            case REPEAT:
                return sentenciaRepetir();
            case FOR_:
                return sentenciaDesde();
            case EXIT:
                return sentenciaSalir();
            case WRITE:
                return sentenciaEscribir();
            case READ:
                return sentenciaLeer();
            case LLAVE_IZQUIERDA:
                return bloque();
            case IDENTIFICADOR:
                return asignacion();
            default:
                throw error("Token inesperado al inicio de sentencia: " + actual.getType());
        }
    }

    private Asignacion asignacion() {
        Token id = esperar(TypesTokens.IDENTIFICADOR, "Se esperaba un identificador");
        esperar(TypesTokens.ASIGNACION, "Se esperaba ':='");
        NodoAST expr = expresion();
        esperar(TypesTokens.PUNTO_Y_COMA, "Se esperaba ';'");
        Asignacion a = new Asignacion(id.getValue(), expr);
        a.linea = id.getLine();
        return a;
    }

    private Declaracion sentenciaDeclaracion() {
        Token tipoTok = actual;
        String tipo;
        if (aceptar(TypesTokens.INT))
            tipo = "int";
        else if (aceptar(TypesTokens.REAL))
            tipo = "real";
        else if (aceptar(TypesTokens.STRING))
            tipo = "string";
        else {
            throw error("Se esperaba un tipo");
        }

        List<String> ids = listaIdentificadores();
        esperar(TypesTokens.PUNTO_Y_COMA, "Se esperaba ';'");

        Declaracion d = new Declaracion(tipo, ids);
        d.linea = tipoTok.getLine();
        return d;
    }

    private List<String> listaIdentificadores() {
        List<String> ids = new ArrayList<>();
        Token t = esperar(TypesTokens.IDENTIFICADOR, "Se esperaba un identificador");
        if (t != null)
            ids.add(t.getValue());

        while (aceptar(TypesTokens.COMA)) {
            Token t2 = esperar(TypesTokens.IDENTIFICADOR, "Se esperaba un identificador después de ','");
            if (t2 != null)
                ids.add(t2.getValue());
        }
        return ids;
    }

    private Si sentenciaSi() {
        Token tok = esperar(TypesTokens.IF, "Se esperaba 'if'");
        NodoAST cond = expresion();
        esperar(TypesTokens.THEN, "Se esperaba 'then'");
        Bloque entonces = bloque();
        Bloque sino = null;
        if (aceptar(TypesTokens.ELSE)) {
            sino = bloque();
        }
        Si s = new Si(cond, entonces, sino);
        s.linea = tok.getLine();
        return s;
    }

    private Mientras sentenciaMientras() {
        Token tok = esperar(TypesTokens.WHILE, "Se esperaba 'while'");
        NodoAST cond = expresion();
        esperar(TypesTokens.DO, "Se esperaba 'do'");
        Bloque cuerpo = bloque();
        Mientras m = new Mientras(cond, cuerpo);
        m.linea = tok.getLine();
        return m;
    }

    private Repetir sentenciaRepetir() {
        Token tok = esperar(TypesTokens.REPEAT, "Se esperaba 'repeat'");
        Bloque cuerpo = bloque();
        esperar(TypesTokens.UNTIL, "Se esperaba 'until'");
        NodoAST cond = expresion();
        esperar(TypesTokens.PUNTO_Y_COMA, "Se esperaba ';'");
        Repetir r = new Repetir(cuerpo, cond);
        r.linea = tok.getLine();
        return r;
    }

    private Desde sentenciaDesde() {
        Token tok = esperar(TypesTokens.FOR_, "Se esperaba 'for'");
        Token id = esperar(TypesTokens.IDENTIFICADOR, "Se esperaba un identificador");
        esperar(TypesTokens.ASIGNACION, "Se esperaba ':='");
        NodoAST ini = expresion();
        esperar(TypesTokens.TO, "Se esperaba 'to'");
        NodoAST fin = expresion();
        NodoAST paso = null;
        if (aceptar(TypesTokens.STEP)) {
            paso = expresion();
        }
        esperar(TypesTokens.DO, "Se esperaba 'do'");
        Bloque cuerpo = bloque();
        Desde d = new Desde(id.getValue(), ini, fin, paso, cuerpo);
        d.linea = tok.getLine();
        return d;
    }

    private Salir sentenciaSalir() {
        Token tok = esperar(TypesTokens.EXIT, "Se esperaba 'exit'");
        esperar(TypesTokens.PUNTO_Y_COMA, "Se esperaba ';'");
        Salir s = new Salir();
        s.linea = tok.getLine();
        return s;
    }

    private Escribir sentenciaEscribir() {
        Token tok = esperar(TypesTokens.WRITE, "Se esperaba 'write'");
        esperar(TypesTokens.PARENTESIS_IZQUIERDO, "Se esperaba '('");
        NodoAST e = expresion();
        esperar(TypesTokens.PARENTESIS_DERECHO, "Se esperaba ')'");
        esperar(TypesTokens.PUNTO_Y_COMA, "Se esperaba ';'");
        Escribir w = new Escribir(e);
        w.linea = tok.getLine();
        return w;
    }

    private Leer sentenciaLeer() {
        Token tok = esperar(TypesTokens.READ, "Se esperaba 'read'");
        esperar(TypesTokens.PARENTESIS_IZQUIERDO, "Se esperaba '('");
        Token id = esperar(TypesTokens.IDENTIFICADOR, "Se esperaba un identificador");
        esperar(TypesTokens.PARENTESIS_DERECHO, "Se esperaba ')'");
        esperar(TypesTokens.PUNTO_Y_COMA, "Se esperaba ';'");
        Leer l = new Leer(id.getValue());
        l.linea = tok.getLine();
        return l;
    }

    private Bloque bloque() {
        Token tok = esperar(TypesTokens.LLAVE_IZQUIERDA, "Se esperaba '{'");
        List<NodoAST> sentencias = listaSentencia();
        esperar(TypesTokens.LLAVE_DERECHA, "Se esperaba '}'");
        Bloque b = new Bloque(sentencias);
        b.linea = tok.getLine();
        return b;
    }

    private NodoAST expresion() {
        NodoAST izq = expresionAnd();
        while (check(TypesTokens.OR)) {
            int linea = actual.getLine();
            avanzar();
            NodoAST der = expresionAnd();
            NodoAST b = new Binary("or", izq, der);
            b.linea = linea;
            izq = b;
        }
        return izq;
    }

    private NodoAST expresionAnd() {
        NodoAST izq = expresionNot();
        while (check(TypesTokens.AND)) {
            int linea = actual.getLine();
            avanzar();
            NodoAST der = expresionNot();
            NodoAST b = new Binary("and", izq, der);
            b.linea = linea;
            izq = b;
        }
        return izq;
    }

    private NodoAST expresionNot() {
        if (check(TypesTokens.NOT)) {
            int linea = actual.getLine();
            avanzar();
            NodoAST e = expresionNot();
            NodoAST u = new Unary("not", e);
            u.linea = linea;
            return u;
        }
        return expresionRelacional();
    }

    private NodoAST expresionRelacional() {
        NodoAST izq = expresionSimple();
        int linea = (actual != null) ? actual.getLine() : -1;
        String op = operadorRelacional();
        if (op != null) {
            NodoAST der = expresionSimple();
            NodoAST b = new Binary(op, izq, der);
            b.linea = linea;
            return b;
        }
        return izq;
    }

    private String operadorRelacional() {
        if (actual == null)
            return null;
        switch (actual.getType()) {
            case IGUAL:
                avanzar();
                return "=";
            case DIFERENTE:
                avanzar();
                return "<>";
            case MENOR_QUE:
                avanzar();
                return "<";
            case MENOR_IGUAL:
                avanzar();
                return "<=";
            case MAYOR_QUE:
                avanzar();
                return ">";
            case MAYOR_IGUAL:
                avanzar();
                return ">=";
            default:
                return null;
        }
    }

    private NodoAST expresionSimple() {
        NodoAST izq = termino();
        while (true) {
            if (check(TypesTokens.SUMA)) {
                int linea = actual.getLine();
                avanzar();
                NodoAST b = new Binary("+", izq, termino());
                b.linea = linea;
                izq = b;
            } else if (check(TypesTokens.RESTA)) {
                int linea = actual.getLine();
                avanzar();
                NodoAST b = new Binary("-", izq, termino());
                b.linea = linea;
                izq = b;
            } else {
                break;
            }
        }
        return izq;
    }

    private NodoAST termino() {
        NodoAST izq = factor();
        while (true) {
            if (check(TypesTokens.MULTIPLICACION)) {
                int linea = actual.getLine();
                avanzar();
                NodoAST b = new Binary("*", izq, factor());
                b.linea = linea;
                izq = b;
            } else if (check(TypesTokens.DIVISION)) {
                int linea = actual.getLine();
                avanzar();
                NodoAST b = new Binary("/", izq, factor());
                b.linea = linea;
                izq = b;
            } else {
                break;
            }
        }
        return izq;
    }

    private NodoAST factor() {
        if (actual == null)
            throw error("Se esperaba un factor");

        switch (actual.getType()) {
            case PARENTESIS_IZQUIERDO: {
                avanzar();
                NodoAST e = expresion();
                esperar(TypesTokens.PARENTESIS_DERECHO, "Se esperaba ')'");
                return e;
            }
            case IDENTIFICADOR: {
                Token t = actual;
                avanzar();
                Identificador id = new Identificador(t.getValue());
                id.linea = t.getLine();
                return id;
            }
            case ENTERO: {
                Token t = actual;
                avanzar();
                Literal lit = new Literal(t.getValue(), "int");
                lit.linea = t.getLine();
                return lit;
            }
            case DECIMAL: {
                Token t = actual;
                avanzar();
                Literal lit = new Literal(t.getValue(), "real");
                lit.linea = t.getLine();
                return lit;
            }
            case CADENA: {
                Token t = actual;
                avanzar();
                Literal lit = new Literal(t.getValue(), "string");
                lit.linea = t.getLine();
                return lit;
            }
            default:
                error("Se esperaba un factor");
                return null;
        }
    }

}
