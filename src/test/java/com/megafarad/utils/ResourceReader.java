package com.megafarad.utils;

import java.util.Objects;
import java.util.Scanner;

public class ResourceReader {
    public static String readResourceToString(String path) {
        return new Scanner(Objects.requireNonNull(ResourceReader.class.getClassLoader().getResourceAsStream(path)))
                .useDelimiter("\\A").next();
    }
}
