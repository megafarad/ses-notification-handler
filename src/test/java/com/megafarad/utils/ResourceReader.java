package com.megafarad.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class ResourceReader {
    public static String readResourceToString(String path) {
        return new Scanner(Objects.requireNonNull(ResourceReader.class.getClassLoader().getResourceAsStream(path)))
                .useDelimiter("\\A").next();
    }
}
