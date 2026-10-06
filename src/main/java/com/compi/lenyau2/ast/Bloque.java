package com.compi.lenyau2.ast;

import java.util.List;

public class Bloque extends NodoAST {
    public final List<NodoAST> sentencias;

    public Bloque(List<NodoAST> s) {
        this.sentencias = s;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitBloque(this);
    }
}
