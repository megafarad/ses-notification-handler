package com.megafarad.seshandler.model;

/**
 * Represents a bounced recipient.
 *
 * @param emailAddress The email address of the bounced recipient.
 * @param action The action taken by the mail transfer agent (MTA) for the bounce.
 * @param status The status of the bounce, indicating whether it's permanent or transient.
 * @param diagnosticCode A diagnostic code provided by the MTA for the bounce reason.
 */
public record BouncedRecipient(
        String emailAddress,
        String action,
        String status,
        String diagnosticCode
) { }
