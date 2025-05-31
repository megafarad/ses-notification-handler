package com.megafarad.seshandler.model;

/**
 * Represents a header for an email message.
 *
 * @param name The name of the header.
 * @param value The value of the header.
 */
public record MailHeader(
        String name,
        String value
) { }
