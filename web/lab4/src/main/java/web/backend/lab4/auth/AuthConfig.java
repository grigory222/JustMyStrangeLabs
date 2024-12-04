package web.backend.lab4.auth;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;

public class AuthConfig {
    private static final String CONFIG_FILE_PATH = "auth.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream file = AuthConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config properties.");
        }
    }

    public static String getSecretKey() {
        return properties.getProperty("SECRET_KEY");
    }

}