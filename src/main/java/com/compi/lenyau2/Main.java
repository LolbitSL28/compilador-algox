package com.compi.lenyau2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        String file = "ejemplo.algox";
        Path path = Paths.get(file);

        try {
            String contenido = Files.readString(path);

            Lexer lexer = new Lexer(contenido);
            TokenList listaTokens = lexer.analizeToken();

            System.out.println("Tokens generados:");
            TokenList current = listaTokens;
            while (current != null) {
                Token token = current.getToken();
                System.out.println(
                        "Token: " + token.getTypeTokenNumber() + ", Tipo: " + token.getType() + ", Valor: "
                                + token.getValue() + ", Línea: " + token.getLine());
                current = current.getNext();
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

    }
}