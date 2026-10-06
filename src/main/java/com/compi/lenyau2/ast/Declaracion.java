package com.compi.lenyau2.ast;

import java.util.List;

public class Declaracion extends NodoAST {
    public final String tipo;
    public final List<String> ids;

    public Declaracion(String tipo, List<String> ids) {
        this.tipo = tipo;
        this.ids = ids;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitDeclaracion(this);
    }
}
