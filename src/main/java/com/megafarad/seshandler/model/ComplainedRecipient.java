package com.megafarad.seshandler.model;

/**
 * Represents a recipient that has complained about the email.
 *
 * @param emailAddress The email address of the recipient who complained about the email.
 */
public record ComplainedRecipient(
        String emailAddress
) { }
