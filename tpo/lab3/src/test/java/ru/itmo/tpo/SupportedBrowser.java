package ru.itmo.tpo;

import java.util.List;

public enum SupportedBrowser {
    CHROMIUM,
    FIREFOX;

    public static List<SupportedBrowser> fromSystemProperty() {
        String prop = System.getProperty("browser", "all").toLowerCase();
        return switch (prop) {
            case "chrome", "chromium" -> List.of(CHROMIUM);
            case "firefox" -> List.of(FIREFOX);
            default -> List.of(CHROMIUM, FIREFOX);
        };
    }
}
