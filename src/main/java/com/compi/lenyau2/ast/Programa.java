package com.compi.lenyau2.ast;

import java.util.List;

public class Programa extends NodoAST {
    public final List<NodoAST> sentencias;

    public Programa(List<NodoAST> s) {
        this.sentencias = s;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitPrograma(this);
    }
}
