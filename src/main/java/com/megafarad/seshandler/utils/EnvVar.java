package com.megafarad.seshandler.utils;

/**
 * The EnvVar class provides utility methods to retrieve environment variables
 * from the system and handle cases where the variables are not set or have
 * empty values. This class simplifies accessing configuration options that are
 * set as environment variables.
 */
public class EnvVar {

    /**
     * Retrieves the value of a specified environment variable. If the environment
     * variable is not set or is empty, a default value is returned instead.
     *
     * @param key The name of the environment variable to retrieve.
     * @param defaultValue The default value to return if the environment variable
     *                     is not set or its value is empty.
     * @return The value of the environment variable if it exists and is not empty;
     *         otherwise, the default value.
     */
    public static String getEnvVar(String key, String defaultValue) {
        String value = System.getenv(key);

        if (value == null || value.isEmpty()) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * Retrieves the value of the specified environment variable. If the environment
     * variable is not set or its value is empty, an {@link IllegalStateException} is thrown.
     * This method is primarily used to enforce the presence of critical configuration
     * parameters within the application.
     *
     * @param key The name of the environment variable to retrieve.
     * @return The value of the specified environment variable.
     * @throws IllegalStateException If the environment variable is not set or has an empty value.
     */
    public static String getEnvVar(String key) {
        String value = System.getenv(key);
        if ((value == null || value.isEmpty())) {
            throw new IllegalStateException("Required environment variable " + key);
        } else {
            return value;
        }
    }


}
