package com.megafarad.seshandler.model;


/**
 * Represents a notification received from Amazon SES (Simple Email Service).
 * This record serves as a wrapper for various types of notifications, including bounce,
 * complaint, and delivery notifications, as well as metadata about the original
 * email message.
 *
 * The record encapsulates the following components:
 *
 * @param notificationType  The type of notification (e.g., bounce, complaint, delivery).
 * @param mail Metadata and information about the original email message.
 * @param bounce Details about the bounce notification, if applicable.
 * @param complaint Details about the complaint notification, if applicable.
 * @param delivery Details about the delivery notification, if applicable.
 */
public record SESNotification(
    String notificationType,
    Mail mail,
    Bounce bounce,
    Complaint complaint,
    Delivery delivery
) { }
