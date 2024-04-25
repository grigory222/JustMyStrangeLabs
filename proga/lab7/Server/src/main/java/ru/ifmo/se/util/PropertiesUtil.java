package ru.ifmo.se.util;

import ru.ifmo.se.Main;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil(){
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var is = Main.class.getClassLoader().getResourceAsStream("application.properties")){
            PROPERTIES.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
