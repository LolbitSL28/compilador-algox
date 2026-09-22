package com.compi.lenyau2;

public class Lexer {

    private String archivo;
    private int posicion = 0;
    private int line = 1;

    private TokenList head = null;
    private TokenList tail = null;

    public Lexer(String archivo) {
        this.archivo = archivo;
    }

    private void addToken(TypesTokens type, String value) {
        Token token = new Token(type, value, line);
        TokenList newNodo = new TokenList(token);

        if (head == null) {
            head = newNodo;
            tail = newNodo;
        } else {
            tail.setNext(newNodo);
            tail = newNodo;
        }
    }

    public TokenList analizeToken() {
        while (posicion < archivo.length()) {
            char actual = archivo.charAt(posicion);

            switch (actual) {
                case ' ':
                case '\r':
                case '\t':
                    posicion++;
                    break;
                case '\n':
                    line++;
                    posicion++;
                    break;
                case '+':
                    addToken(TypesTokens.SUMA, "+");
                    posicion++;
                    break;
                case '-':
                    addToken(TypesTokens.RESTA, "-");
                    posicion++;
                    break;
                case '*':
                    addToken(TypesTokens.MULTIPLICACION, "*");
                    posicion++;
                    break;
                case '/':
                    if (posicion + 1 < archivo.length() && archivo.charAt(posicion + 1) == '/') {

                        while (posicion < archivo.length() && archivo.charAt(posicion) != '\n') {
                            posicion++;
                        }
                    } else {
                        addToken(TypesTokens.DIVISION, "/");
                        posicion++;
                    }
                    break;
                case '=':
                    addToken(TypesTokens.IGUAL, "=");
                    posicion++;
                    break;
                case '<':
                    if (posicion + 1 < archivo.length() && archivo.charAt(posicion + 1) == '=') {
                        addToken(TypesTokens.MENOR_IGUAL, "<=");
                        posicion += 2;
                    } else if (posicion + 1 < archivo.length() && archivo.charAt(posicion + 1) == '>') {
                        addToken(TypesTokens.DIFERENTE, "<>");
                        posicion += 2;
                    } else {
                        addToken(TypesTokens.MENOR_QUE, "<");
                        posicion++;
                    }
                    break;
                case '>':
                    if (posicion + 1 < archivo.length() && archivo.charAt(posicion + 1) == '=') {
                        addToken(TypesTokens.MAYOR_IGUAL, ">=");
                        posicion += 2;
                    } else {
                        addToken(TypesTokens.MAYOR_QUE, ">");
                        posicion++;
                    }
                    break;
                case ':':
                    if (posicion + 1 < archivo.length() && archivo.charAt(posicion + 1) == '=') {
                        addToken(TypesTokens.ASIGNACION, ":=");
                        posicion += 2;
                    } else {
                        System.out.println("Error: se espera '=' en la línea " + line);
                        posicion++;
                    }
                    break;
                case ';':
                    addToken(TypesTokens.PUNTO_Y_COMA, ";");
                    posicion++;
                    break;
                case ',':
                    addToken(TypesTokens.COMA, ",");
                    posicion++;
                    break;
                case '{':
                    addToken(TypesTokens.LLAVE_IZQUIERDA, "{");
                    posicion++;
                    break;
                case '}':
                    addToken(TypesTokens.LLAVE_DERECHA, "}");
                    posicion++;
                    break;
                case '(':
                    addToken(TypesTokens.PARENTESIS_IZQUIERDO, "(");
                    posicion++;
                    break;
                case ')':
                    addToken(TypesTokens.PARENTESIS_DERECHO, ")");
                    posicion++;
                    break;
                case '"':
                    posicion++;
                    String cadena = "";
                    boolean closed = false;
                    while (posicion < archivo.length()) {
                        char chr = archivo.charAt(posicion);
                        if (chr == '"') {
                            closed = true;
                            break;
                        }
                        if (chr == '\n' || chr == '\r') {
                            System.out.println("Error: Fin de línea inesperado en la línea " + line);
                            break;
                        }
                        cadena += chr;
                        posicion++;
                    }
                    if (closed) {
                        posicion++;
                        addToken(TypesTokens.CADENA, cadena);
                    }
                    break;
                default:
                    if (Character.isDigit(actual)) {
                        String number = "";
                        while (posicion < archivo.length() && (Character.isDigit(archivo.charAt(posicion)))) {
                            number += archivo.charAt(posicion);
                            posicion++;
                        }
                        if (posicion < archivo.length() && archivo.charAt(posicion) == '.') {
                            number += archivo.charAt(posicion);
                            posicion++;
                            while (posicion < archivo.length() && Character.isDigit(archivo.charAt(posicion))) {
                                number += archivo.charAt(posicion);
                                posicion++;
                            }
                            addToken(TypesTokens.DECIMAL, number);
                        } else {
                            addToken(TypesTokens.ENTERO, number);
                        }
                    } else if (Character.isLetter(actual)) {
                        String id = "";
                        while (posicion < archivo.length() && (Character.isLetterOrDigit(archivo.charAt(posicion))
                                || archivo.charAt(posicion) == '_')) {
                            id += archivo.charAt(posicion);
                            posicion++;
                        }
                        switch (id) {
                            case "and":
                                addToken(TypesTokens.AND, id);
                                break;
                            case "or":
                                addToken(TypesTokens.OR, id);
                                break;
                            case "not":
                                addToken(TypesTokens.NOT, id);
                                break;
                            case "if":
                                addToken(TypesTokens.IF, id);
                                break;
                            case "then":
                                addToken(TypesTokens.THEN, id);
                                break;
                            case "else":
                                addToken(TypesTokens.ELSE, id);
                                break;
                            case "while":
                                addToken(TypesTokens.WHILE, id);
                                break;
                            case "do":
                                addToken(TypesTokens.DO, id);
                                break;
                            case "repeat":
                                addToken(TypesTokens.REPEAT, id);
                                break;
                            case "until":
                                addToken(TypesTokens.UNTIL, id);
                                break;
                            case "to":
                                addToken(TypesTokens.TO, id);
                                break;
                            case "step":
                                addToken(TypesTokens.STEP, id);
                                break;
                            case "exit":
                                addToken(TypesTokens.EXIT, id);
                                break;
                            case "write":
                                addToken(TypesTokens.WRITE, id);
                                break;
                            case "read":
                                addToken(TypesTokens.READ, id);
                                break;
                            case "int":
                                addToken(TypesTokens.INT, id);
                                break;
                            case "real":
                                addToken(TypesTokens.REAL, id);
                                break;
                            case "string":
                                addToken(TypesTokens.STRING, id);
                                break;
                            default:
                                addToken(TypesTokens.IDENTIFICADOR, id);
                                break;
                        }
                    } else {
                        System.out.println("Error: Simbolo no valido '" + actual + "' en la línea " + line);
                        posicion++;
                    }
                    break;
            }
        }
        return head;
    }

}
