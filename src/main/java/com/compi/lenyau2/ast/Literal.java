package com.compi.lenyau2.ast;

public class Literal extends NodoAST {
    public final String valor;
    public final String tipo;

    public Literal(String valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitLiteral(this);
    }
}
