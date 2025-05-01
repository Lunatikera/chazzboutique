/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author carli
 */
public class ColorNameDetector {
    
    private static final Map<String, Predicate<int[]>> COLOR_RANGES = new LinkedHashMap<>();

    static {
        // Orden de detección importante (de más específico a más general)
        
        // Blancos y negros primero
        COLOR_RANGES.put("Blanco", rgb -> 
            rgb[0] >= 240 && rgb[1] >= 240 && rgb[2] >= 240);
        
        COLOR_RANGES.put("Negro", rgb -> 
            rgb[0] <= 15 && rgb[1] <= 15 && rgb[2] <= 15);
        
        COLOR_RANGES.put("Gris", rgb -> {
            int max = Math.max(Math.max(rgb[0], rgb[1]), rgb[2]);
            int min = Math.min(Math.min(rgb[0], rgb[1]), rgb[2]);
            return (max - min) < 30 && max < 200;
        });
        
        // Colores primarios
        COLOR_RANGES.put("Rojo", rgb -> 
            rgb[0] - rgb[1] > 60 && rgb[0] - rgb[2] > 60);
        
        COLOR_RANGES.put("Verde", rgb -> 
            rgb[1] - rgb[0] > 60 && rgb[1] - rgb[2] > 60);
        
        COLOR_RANGES.put("Azul", rgb -> 
            rgb[2] - rgb[0] > 60 && rgb[2] - rgb[1] > 60);
        
        // Colores secundarios
        COLOR_RANGES.put("Amarillo", rgb -> 
            rgb[0] >= 200 && rgb[1] >= 200 && rgb[2] <= 50);
        
        COLOR_RANGES.put("Magenta", rgb -> 
            rgb[0] >= 200 && rgb[2] >= 200 && rgb[1] <= 50);
        
        COLOR_RANGES.put("Cian", rgb -> 
            rgb[1] >= 200 && rgb[2] >= 200 && rgb[0] <= 50);
        
        // Tonos tierra
        COLOR_RANGES.put("Marrón", rgb -> 
            rgb[0] >= 100 && rgb[0] <= 150 &&
            rgb[1] >= 50 && rgb[1] <= 100 &&
            rgb[2] <= 50);
        
        COLOR_RANGES.put("Beige", rgb -> 
            rgb[0] >= 200 && rgb[1] >= 180 && rgb[2] >= 140 &&
            Math.abs(rgb[0] - rgb[1]) < 30 &&
            Math.abs(rgb[1] - rgb[2]) < 40);
    }

    public static String getColorName(String hexColor) {
        try {
            int[] rgb = hexToRgb(hexColor);
            return COLOR_RANGES.entrySet().stream()
                .filter(entry -> entry.getValue().test(rgb))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(hexColor); // Si no coincide, devuelve el código original
        } catch (IllegalArgumentException e) {
            return "N/A";
        }
    }

    private static int[] hexToRgb(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        
        if (hexColor.length() == 3) {
            hexColor = "" + hexColor.charAt(0) + hexColor.charAt(0)
                         + hexColor.charAt(1) + hexColor.charAt(1)
                         + hexColor.charAt(2) + hexColor.charAt(2);
        }
        
        if (hexColor.length() != 6) {
            throw new IllegalArgumentException("Formato hexadecimal inválido");
        }
        
        return new int[]{
            Integer.parseInt(hexColor.substring(0, 2), 16),
            Integer.parseInt(hexColor.substring(2, 4), 16),
            Integer.parseInt(hexColor.substring(4, 6), 16)
        };
    }
}
