package com.compi.lenyau2.ast;

public abstract class NodoAST {
    public int linea;

    public abstract <Resultado> Resultado accept(Visitor<Resultado> visitor);
}
