/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author carli
 */
public class Capitalizador {
    public static String capitalizarNombre(String texto) {
    String[] palabras = texto.toLowerCase().split("\\s+");
    StringBuilder resultado = new StringBuilder();
    for (String palabra : palabras) {
        if (!palabra.isBlank()) {
            resultado.append(Character.toUpperCase(palabra.charAt(0)))
                     .append(palabra.substring(1))
                     .append(" ");
        }
    }
    return resultado.toString().trim();
}

}
