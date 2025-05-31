package com.megafarad.seshandler.model;

import java.util.List;

/**
 * Represents metadata and information about an email message processed by Amazon SES (Simple Email Service).
 * The Mail record provides details such as sender information, recipients, message headers, and other
 * contextual information about the email.
 *
 * @param timestamp The timestamp of the email message.
 * @param messageId The unique identifier of the email message.
 * @param source The email address of the sender.
 * @param sourceArn The Amazon Resource Name (ARN) of the sender.
 * @param sourceIp The IP address of the sender.
 * @param sendingAccountId The AWS account ID that sent the email.
 * @param callerIdentity The AWS account ID that called the SES API.
 * @param destination A list of email addresses to which the email was sent.
 * @param headersTruncated Indicates whether the headers were truncated due to size limitations.
 * @param headers A list of headers associated with the email message.
 * @param commonHeaders Common headers associated with the email message.
 */
public record Mail(
        String timestamp,
        String messageId,
        String source,
        String sourceArn,
        String sourceIp,
        String sendingAccountId,
        String callerIdentity,
        List<String> destination,
        Boolean headersTruncated,
        List<MailHeader> headers,
        CommonHeaders commonHeaders
)
{ }
