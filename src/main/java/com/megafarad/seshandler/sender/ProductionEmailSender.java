package com.megafarad.seshandler.sender;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

/**
 * ProductionEmailSender is a concrete implementation of the EmailSender interface,
 * responsible for sending emails using Amazon Simple Email Service (SES).
 * <p>
 * This class integrates with AWS SDK for Java to send emails by constructing
 * and sending a request to Amazon SES. It supports both HTML and plain text email
 * formats and allows specifying the sender and recipient email addresses, subject,
 * and content.
 * <p>
 * Features:
 * - Sends emails using Amazon SES via the SES client from the AWS SDK.
 * - Supports constructing email messages with a subject, HTML content, and plain text content.
 * - Encapsulates the logic for sending emails to decouple email-sending logic
 *   from other application components.
 * <p>
 * This class uses an `SesClient` instance to communicate with Amazon SES. The `SesClient`
 * is created during the instantiation of the class.
 * <p>
 * Typical use cases include:
 * - Sending application notifications via email.
 * - Integrating email delivery features into applications using AWS SES.
 */
public class ProductionEmailSender implements EmailSender {

    private final SesClient sesClient;

    public ProductionEmailSender() {
        this.sesClient = SesClient.create();
    }

    @Override
    public void send(String from, String to, String subject, String htmlBody, String textBody) {
        Destination destination = Destination.builder()
                .toAddresses(to)
                .build();

        Content subjectContent = Content.builder().data(subject).build();
        Content htmlContent = Content.builder().data(htmlBody).build();
        Content textContent = Content.builder().data(textBody).build();

        Body body = Body.builder()
                .html(htmlContent)
                .text(textContent)
                .build();

        Message message = Message.builder()
                .subject(subjectContent)
                .body(body)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(from)
                .destination(destination)
                .message(message)
                .build();

        sesClient.sendEmail(request);
    }
}
