package com.megafarad.seshandler.model;

import java.util.List;

/**
 * Represents the common headers of an email message.
 * This record encapsulates the following key information about an email message:
 *
 * @param from The sender's email address(es)
 * @param to The recipient's email address(es)
 * @param date The date and time when the email was sent
 * @param messageId The unique identifier for the email message
 * @param subject The subject line of the email
 */
public record CommonHeaders(
        List<String> from,
        List<String> to,
        String date,
        String messageId,
        String subject
) { }
