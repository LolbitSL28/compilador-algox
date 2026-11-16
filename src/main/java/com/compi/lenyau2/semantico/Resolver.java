package com.compi.lenyau2.semantico;

import com.compi.lenyau2.ast.*;
import java.util.ArrayList;
import java.util.List;

public class Resolver implements Visitor<TipoDato> {

    private final TablaSimbolos tabla = new TablaSimbolos();
    private final List<String> errores = new ArrayList<>();

    public List<String> getErrores() {
        return errores;
    }

    private void reportarError(int linea, String mensaje) {
        errores.add("Error semantico [linea " + linea + "]: " + mensaje);
    }

    @Override
    public TipoDato visitPrograma(Programa p) {
        for (NodoAST s : p.sentencias) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public TipoDato visitBloque(Bloque b) {
        for (NodoAST s : b.sentencias) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public TipoDato visitDeclaracion(Declaracion d) {
        TipoDato tipo = TipoDato.desdeString(d.tipo);
        for (String id : d.ids) {
            if (tabla.existe(id)) {
                reportarError(d.linea, "Variable duplicada: " + id);
            } else {
                tabla.declarar(id, tipo);
            }
        }
        return null;
    }

    @Override
    public TipoDato visitAsignacion(Asignacion a) {
        if (!tabla.existe(a.id)) {
            reportarError(a.linea, "Variable no declarada: " + a.id);
        }

        TipoDato tipoVar = tabla.obtener(a.id);
        TipoDato tipoExpr = a.expresion.accept(this);

        if (tabla.existe(a.id) && !AnalizadorTipos.asignable(tipoVar, tipoExpr)) {
            reportarError(a.linea, "Incompatibilidad de tipos (504) en asignacion: "
                    + tipoVar + " := " + tipoExpr);
        }
        return null;
    }

    @Override
    public TipoDato visitSi(Si s) {
        TipoDato tipoCond = s.condicion.accept(this);
        if (!AnalizadorTipos.esCondicionValida(tipoCond)) {
            reportarError(s.linea, "Condicion de 'if' debe ser numerica, se obtuvo: " + tipoCond);
        }
        s.entonces.accept(this);
        if (s.sino != null)
            s.sino.accept(this);
        return null;
    }

    @Override
    public TipoDato visitMientras(Mientras m) {
        TipoDato tipoCond = m.condicion.accept(this);
        if (!AnalizadorTipos.esCondicionValida(tipoCond)) {
            reportarError(m.linea, "Condicion de 'while' debe ser numerica, se obtuvo: " + tipoCond);
        }
        m.cuerpo.accept(this);
        return null;
    }

    @Override
    public TipoDato visitRepetir(Repetir r) {
        r.cuerpo.accept(this);
        TipoDato tipoCond = r.condicion.accept(this);
        if (!AnalizadorTipos.esCondicionValida(tipoCond)) {
            reportarError(r.linea, "Condicion de 'repeat' debe ser numerica, se obtuvo: " + tipoCond);
        }
        return null;
    }

    @Override
    public TipoDato visitDesde(Desde d) {
        if (!tabla.existe(d.id)) {
            reportarError(d.linea, "Variable no declarada: " + d.id);
        } else if (!AnalizadorTipos.esNumerico(tabla.obtener(d.id))) {
            reportarError(d.linea, "Variable de 'for' debe ser numerica");
        }

        TipoDato tIni = d.inicio.accept(this);
        if (!AnalizadorTipos.esNumerico(tIni) && tIni != TipoDato.ERROR)
            reportarError(d.linea, "Inicio de 'for' debe ser numerico");

        TipoDato tFin = d.fin.accept(this);
        if (!AnalizadorTipos.esNumerico(tFin) && tFin != TipoDato.ERROR)
            reportarError(d.linea, "Fin de 'for' debe ser numerico");

        if (d.paso != null) {
            TipoDato tPaso = d.paso.accept(this);
            if (!AnalizadorTipos.esNumerico(tPaso) && tPaso != TipoDato.ERROR)
                reportarError(d.linea, "Paso de 'for' debe ser numerico");
        }
        d.cuerpo.accept(this);
        return null;
    }

    @Override
    public TipoDato visitSalir(Salir s) {
        return null;
    }

    @Override
    public TipoDato visitEscribir(Escribir e) {
        e.expresion.accept(this);
        return null;
    }

    @Override
    public TipoDato visitLeer(Leer l) {
        if (!tabla.existe(l.id)) {
            reportarError(l.linea, "Variable no declarada: " + l.id);
        }
        return null;
    }

    @Override
    public TipoDato visitLiteral(Literal l) {
        return TipoDato.desdeString(l.tipo);
    }

    @Override
    public TipoDato visitIdentificador(Identificador i) {
        if (!tabla.existe(i.nombre)) {
            reportarError(i.linea, "Variable no declarada: " + i.nombre);
            return TipoDato.ERROR;
        }
        return tabla.obtener(i.nombre);
    }

    @Override
    public TipoDato visitUnary(Unary u) {
        TipoDato t = u.expr.accept(this);
        if (u.op.equals("not")) {
            TipoDato res = AnalizadorTipos.not(t);
            if (res == TipoDato.ERROR) {
                reportarError(u.linea, "Incompatibilidad de tipos (504) en 'not' con [" + t + "]");
            }
            return res;
        }
        return TipoDato.ERROR;
    }

    @Override
    public TipoDato visitBinary(Binary b) {
        TipoDato izq = b.izq.accept(this);
        TipoDato der = b.der.accept(this);
        TipoDato res;

        if (b.op.equals("+") || b.op.equals("-") || b.op.equals("*") || b.op.equals("/")) {
            res = AnalizadorTipos.aritmetico(b.op, izq, der);
        } else if (b.op.equals("and") || b.op.equals("or")) {
            res = AnalizadorTipos.logico(izq, der);
        } else {
            res = AnalizadorTipos.relacional(izq, der);
        }

        if (res == TipoDato.ERROR) {
            reportarError(b.linea, "Incompatibilidad de tipos (504) en '" + b.op
                    + "' con [" + izq + ", " + der + "]");
        }
        return res;
    }
}