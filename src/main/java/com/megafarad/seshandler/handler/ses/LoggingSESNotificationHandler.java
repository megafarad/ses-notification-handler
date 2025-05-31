package com.megafarad.seshandler.handler.ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.megafarad.seshandler.model.Bounce;
import com.megafarad.seshandler.model.Complaint;
import com.megafarad.seshandler.model.Delivery;
import com.megafarad.seshandler.model.Mail;

/**
 * LoggingSESNotificationHandler extends AbstractSESNotificationHandler and provides
 * a concrete implementation for handling Amazon Simple Email Service (SES) notifications.
 * This class logs information about the bounce, complaint, and delivery events using
 * the logging facility provided by the AWS Lambda execution context.
 *
 * The class overrides specific methods from AbstractSESNotificationHandler to include
 * logging functionality for each type of notification:
 * - Bounce notifications are logged with details about the bounce event.
 * - Complaint notifications are logged with details about the complaint event.
 * - Delivery notifications are logged with details about the delivery event.
 *
 * The logging can be useful for debugging, monitoring, or storing metrics regarding
 * the SES notifications received.
 */
public class LoggingSESNotificationHandler extends AbstractSESNotificationHandler {

    /**
     * Processes a bounce notification received from Amazon Simple Email Service (SES).
     * This method is invoked to handle events when an email cannot be successfully delivered
     * to its intended recipients. It logs details about the bounce event using the AWS Lambda
     * execution context.
     *
     * @param mail An object containing the details of the email that triggered the bounce event,
     *             including the sender, recipients, message headers, and other metadata.
     * @param bounce An object containing details about the bounce event, such as the type of
     *               bounce, the affected recipients, and additional event metadata.
     * @param context The AWS Lambda execution context, which provides runtime information and
     *                logging capabilities for the current function invocation.
     */
    @Override
    protected void handleBounce(Mail mail, Bounce bounce, Context context) {
        super.handleBounce(mail, bounce, context);
        context.getLogger().log("Bounce received: " + bounce);
    }

    /**
     * Handles a complaint notification received from Amazon Simple Email Service (SES).
     * Complaint notifications are generated when a recipient marks an email as spam or unwanted.
     * This method logs the complaint event details using the AWS Lambda execution context.
     *
     * @param mail An object containing details of the email associated with the complaint event,
     *             such as the sender, recipients, and message headers.
     * @param complaint An object containing details about the complaint event, including
     *                  the recipients who reported the complaint, complaint type, and metadata
     *                  about the event.
     * @param context The AWS Lambda execution context, providing runtime metadata and utilities
     *                for logging the details of the complaint event.
     */
    @Override
    protected void handleComplaint(Mail mail, Complaint complaint, Context context) {
        super.handleComplaint(mail, complaint, context);
        context.getLogger().log("Complaint received: " + complaint);
    }

    /**
     * Handles the delivery notification for an email. This method is invoked
     * when an email is successfully delivered to the recipient's mail server.
     * It logs the delivery details using the provided AWS Lambda execution context.
     *
     * @param mail      An object containing details about the email that was delivered,
     *                  such as the sender, recipient(s), and headers.
     * @param delivery  An object containing details about the delivery event, including
     *                  the timestamp, processing time, recipient list, and SMTP response.
     * @param context   The AWS Lambda execution context, which provides runtime information
     *                  and logging capabilities for this function invocation.
     */
    protected void handleDelivery(Mail mail, Delivery delivery, Context context) {
        super.handleDelivery(mail, delivery, context);
        context.getLogger().log("Delivery made: " + delivery);
    }
}
