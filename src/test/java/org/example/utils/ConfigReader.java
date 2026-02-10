package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            String path = "src/test/resources/config.properties";
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
        } catch (IOException e) {
            // Si no encuentra el archivo (ej: en CI), no falla aqui, sino que properties queda vacio o parcial
            System.out.println("WARN: No se encontro config.properties (Normal en CI si usas Secrets)");
            if (properties == null) properties = new Properties();
        }
    }

    public static String get(String key) {
        // 1. Intentar leer del archivo properties
        String value = properties.getProperty(key);

        // 2. Si es nulo, intentar leer de Variables de Entorno (CI/CD)
        if (value == null) {
            // Convertimos 'app.username' a 'APP_USERNAME'
            String envKey = key.replace(".", "_").toUpperCase();
            value = System.getenv(envKey);
        }

        return value;
    }
}