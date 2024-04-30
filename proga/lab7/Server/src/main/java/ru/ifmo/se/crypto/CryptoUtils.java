package ru.ifmo.se.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CryptoUtils {
    private static final MessageDigest md;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:',.<>?";
    private static final Random random = new Random();
    private static final int SALT_LENGTH = 16;
    private static final String PEPPER = "4J:EB'7g%;?C_9Ga";

    static {
        try {
            md = MessageDigest.getInstance("SHA384");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private CryptoUtils() {

    }

    private static String getRandomString(int length) {
        return IntStream.range(0, length)
                .mapToObj(i -> CHARACTERS.charAt(random.nextInt(CHARACTERS.length())))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static byte[] hashBytes(String string){
         return md.digest(string.getBytes(StandardCharsets.UTF_8));
    }
    public static String hash(String string){
        return Base64.getEncoder().encodeToString(hashBytes(string));
    }

    public static String hash(String string, String salt, String pepper){
        return hash(string + salt + pepper);
    }

    public static String generateSalt() {
        return getRandomString(SALT_LENGTH);
    }

    public static String getPepper() {
        return PEPPER;
    }
}
