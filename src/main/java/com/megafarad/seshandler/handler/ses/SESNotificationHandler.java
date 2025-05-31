package com.megafarad.seshandler.handler.ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.megafarad.seshandler.model.SESNotification;


/**
 * The SESNotificationHandler interface defines a contract for handling
 * notifications from Amazon Simple Email Service (SES). Implementations of
 * this interface should provide the logic to process various SES notifications.
 *
 * SES notifications can include events such as:
 * - Bounce notifications: indicating issues with email delivery.
 * - Complaint notifications: indicating recipients marking emails as spam or unwanted.
 * - Delivery notifications: confirming successful email delivery.
 *
 * The handleNotification method is invoked to process a given notification and
 * provides the corresponding AWS Lambda execution context information.
 */
public interface SESNotificationHandler {

    /**
     * Handles an Amazon Simple Email Service (SES) notification by determining its type
     * and processing it using the context provided. The method dispatches handling of
     * Bounce, Complaint, or Delivery notifications based on the notification type found
     * in the SESNotification object.
     *
     * @param notification An SESNotification object that contains the details of the event
     *                     (e.g., Bounce, Complaint, Delivery) to be processed.
     * @param context      The AWS Lambda execution context, providing runtime information
     *                     such as remaining execution time and logging utilities.
     */
    void handleNotification(SESNotification notification, Context context);
}
