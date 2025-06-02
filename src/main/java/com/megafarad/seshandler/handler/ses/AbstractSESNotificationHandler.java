package com.megafarad.seshandler.handler.ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.megafarad.seshandler.model.*;

/**
 * AbstractSESNotificationHandler provides a base implementation of the SESNotificationHandler
 * interface for handling different types of Amazon Simple Email Service (SES) notifications.
 * <p>
 * This abstract class defines a generic method for handling SES notifications by dispatching
 * the processing to specialized methods based on the notification type. Subclasses can override
 * these specialized methods to provide custom handling logic for each type of notification.
 * <p>
 * Notification types supported include Bounce, Complaint, and Delivery notifications.
 */
public abstract class AbstractSESNotificationHandler implements SESNotificationHandler {
    /**
     * Handles an Amazon Simple Email Service (SES) notification by determining its type
     * and delegating the processing to specific methods for each notification type.
     * This method processes Bounce, Complaint, and Delivery notifications and invokes
     * appropriate handlers for each case.
     *
     * @param notification An SESNotification object containing details about the type of
     *                     notification (Bounce, Complaint, Delivery) and related data.
     * @param context      The AWS Lambda execution context, providing runtime metadata
     *                     and logging utilities for the current invocation.
     */
    @Override
    public void handleNotification(SESNotification notification, Context context) {
        switch (notification.notificationType()) {
            case "Bounce" -> handleBounce(notification.mail(), notification.bounce(), context);
            case "Complaint" -> handleComplaint(notification.mail(), notification.complaint(), context);
            case "Delivery" -> handleDelivery(notification.mail(), notification.delivery(), context);
        }
    }

    /**
     * Handles a bounce notification received from Amazon Simple Email Service (SES).
     * A bounce occurs when an email cannot be successfully delivered to its intended recipient(s).
     * This method is invoked to process and potentially log details about the bounce event,
     * including information about the mail and bounced recipients.
     *
     * @param mail   An object containing details of the email that triggered the bounce,
     *               such as the sender, recipients, and headers.
     * @param bounce An object containing details about the bounce event, such as the
     *               type of bounce, the recipients impacted, and metadata about the event.
     * @param context The AWS Lambda execution context, providing runtime metadata and
     *                logging utilities for the current invocation.
     */
    protected void handleBounce(Mail mail, Bounce bounce, Context context) {

    }

    /**
     * Handles a complaint notification received from Amazon Simple Email Service (SES).
     * Complaint notifications are triggered when a recipient marks an email as spam or unwanted.
     * This method processes the complaint event, providing an opportunity to take corrective
     * measures or log details about the complaint.
     *
     * @param mail      An object containing details of the email that triggered the complaint,
     *                  such as the sender, recipients, and headers.
     * @param complaint An object containing details about the complaint event, such as
     *                  the recipients who generated the complaint, the type of complaint,
     *                  and metadata about the event.
     * @param context   The AWS Lambda execution context, providing runtime metadata and
     *                  logging utilities for the current invocation.
     */
    protected void handleComplaint(Mail mail, Complaint complaint, Context context) {

    }

    /**
     * Handles a delivery notification received from Amazon Simple Email Service (SES).
     * Delivery notifications indicate that an email has been successfully delivered
     * to the recipient's mail server. This method processes the delivery event, providing
     * an opportunity to log or take subsequent actions based on the information provided.
     *
     * @param mail      An object containing details of the email that was delivered, such as
     *                  the sender, recipients, and headers.
     * @param delivery  An object containing details about the delivery event, such as the
     *                  timestamp, processing time, recipient list, and SMTP response.
     * @param context   The AWS Lambda execution context, providing runtime metadata and
     *                  logging utilities for the current invocation.
     */
    protected void handleDelivery(Mail mail, Delivery delivery, Context context) {

    }
}
