package com.compi.lenyau2.ast;

public class Asignacion extends NodoAST {
    public final String id;
    public final NodoAST expresion;

    public Asignacion(String id, NodoAST e) {
        this.id = id;
        this.expresion = e;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitAsignacion(this);
    }
}
