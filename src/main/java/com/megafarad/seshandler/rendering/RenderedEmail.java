package com.megafarad.seshandler.rendering;


/**
 * Represents a fully rendered email, containing the generated subject,
 * HTML body, and plain text body.
 * <p>
 * Instances of this class are typically produced as a result of rendering
 * email templates with specified context data in an implementation of
 * RenderingService.
 *
 * @param subject the subject of the email
 * @param htmlBody the HTML body of the email
 * @param textBody the plain text body of the email
 *
 * @see RenderingService
 */
public record RenderedEmail(String subject, String htmlBody, String textBody) { }
