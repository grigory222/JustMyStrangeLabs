package ru.itmo.tpo;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

public final class TestConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = TestConfig.class.getClassLoader()
                .getResourceAsStream("test-config.properties")) {
            if (is != null) PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load test-config.properties", e);
        }
    }

    private TestConfig() {}

    public static String get(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }

    public static String checkIn(String prefix) {
        int offset = Integer.parseInt(get(prefix + ".checkin.offset", "30"));
        return LocalDate.now().plusDays(offset).toString();
    }

    public static String checkOut(String prefix) {
        int offset = Integer.parseInt(get(prefix + ".checkout.offset", "37"));
        return LocalDate.now().plusDays(offset).toString();
    }
}
