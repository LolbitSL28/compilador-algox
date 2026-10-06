package com.compi.lenyau2.ast;

public interface Visitor<Resultado> {
    Resultado visitPrograma(Programa p);

    Resultado visitBloque(Bloque b);

    Resultado visitAsignacion(Asignacion a);

    Resultado visitDeclaracion(Declaracion d);

    Resultado visitSi(Si s);

    Resultado visitMientras(Mientras m);

    Resultado visitRepetir(Repetir r);

    Resultado visitDesde(Desde d);

    Resultado visitSalir(Salir s);

    Resultado visitEscribir(Escribir e);

    Resultado visitLeer(Leer l);

    Resultado visitBinary(Binary b);

    Resultado visitUnary(Unary u);

    Resultado visitLiteral(Literal l);

    Resultado visitIdentificador(Identificador i);

}
