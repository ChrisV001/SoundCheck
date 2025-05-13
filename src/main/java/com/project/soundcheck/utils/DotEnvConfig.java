package com.project.soundcheck.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvConfig {
    private DotEnvConfig() {

    }

    public static void loadDotEnv() {
        Dotenv.configure()
                .ignoreIfMissing()
                .load()
                .entries()
                .forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }
}
