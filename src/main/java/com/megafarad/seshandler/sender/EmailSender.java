package com.megafarad.seshandler.sender;

/**
 * The EmailSender interface defines a contract for sending emails from a specified
 * sender to a recipient with a subject and content in both HTML and plain text formats.
 * <p>
 * This interface provides a single method, {@code send}, which is responsible for
 * initiating the email delivery process. Classes implementing this interface are expected
 * to use the provided parameters to construct and send an email using the desired
 * delivery mechanism (e.g., SMTP, third-party email services such as Amazon SES).
 * <p>
 * Example use cases include:
 * - Sending emails about Amazon SES bounce, complaint, or delivery notifications.
 * - Abstracting email delivery logic from the main application logic.
 */
public interface EmailSender {
    /**
     * Sends an email with the specified details.
     *
     * @param from      The email address of the sender.
     * @param to        The email address of the recipient.
     * @param subject   The subject line of the email.
     * @param htmlBody  The HTML content of the email body.
     * @param textBody  The plain text content of the email body.
     */
    void send(String from, String to, String subject, String htmlBody, String textBody);
}
