package me.dinuka.tmo;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    private static final Dotenv dotenv;

    static {
        String localEnvPath = "E:/IntelliJ-Projects/techmart-online";

        dotenv = Dotenv.configure()
                .directory(localEnvPath)
                .filename(".env")
                .ignoreIfMissing()
                .load();
    }

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static String get(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }
}
