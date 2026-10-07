package com.compi.lenyau2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.compi.lenyau2.ast.Programa;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        String file = "ejemplo.algox";
        Path path = Paths.get(file);

        try {
            // Lexer
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
            // Parser
            Parser parser = new Parser(listaTokens);
            Programa prog = parser.parse();

            if (parser.getErrores().isEmpty()) {
                System.out.println("Análisis sintáctico exitoso.");
                System.out.println("Sentencias principales: " + prog.sentencias.size());
            } else {
                System.out.println("Se encontraron " + parser.getErrores().size() + " error(es):");
                for (String e : parser.getErrores()) {
                    System.out.println("  " + e);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

    }
}