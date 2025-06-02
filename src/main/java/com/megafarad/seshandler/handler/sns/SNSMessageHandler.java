package com.megafarad.seshandler.handler.sns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megafarad.seshandler.handler.ses.LoggingSESNotificationHandler;
import com.megafarad.seshandler.handler.ses.SESNotificationHandler;
import com.megafarad.seshandler.model.SESNotification;

/**
 * The SNSMessageHandler class is an implementation of the AWS Lambda
 * RequestHandler interface that processes AWS Simple Notification Service (SNS)
 * events. It is designed to handle SNS messages containing Amazon Simple Email Service (SES)
 * notifications and delegates their processing to an implementation of the SESNotificationHandler
 * interface.
 * <p>
 * The handler:
 * - Deserializes SNS messages into SESNotification objects using Jackson ObjectMapper.
 * - Dispatches the SESNotification objects to an SESNotificationHandler for processing.
 * - Handles errors during message processing and logs failure details.
 * <p>
 * This class is configured at runtime to use a specific SESNotificationHandler
 * implementation. The implementation is determined by the environment variable
 * `SES_NOTIFICATION_HANDLER_CLASS`. If the variable is not set or is blank,
 * a default handler (LoggingSESNotificationHandler) is used.
 */
public class SNSMessageHandler implements RequestHandler<SNSEvent, Void> {

    private final SESNotificationHandler sesHandler;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs an instance of SNSMessageHandler and initializes the SESNotificationHandler
     * implementation to process Amazon Simple Email Service (SES) notifications.
     * <p>
     * This constructor determines the specific SESNotificationHandler implementation to be used
     * based on the `SES_NOTIFICATION_HANDLER_CLASS` environment variable. If the variable is not
     * set or is blank, a default handler implementation, LoggingSESNotificationHandler, is used.
     * <p>
     * The SESNotificationHandler is responsible for handling notifications such as bounce,
     * complaint, and delivery events.
     * <p>
     * The constructor encapsulates the loading and instantiation logic of the handler to ensure
     * flexibility and configurability during runtime.
     */
    public SNSMessageHandler() {
        sesHandler = loadHandlerFromEnv();
    }


    /**
     * Handles an AWS Lambda function invocation triggered by an Amazon SNS event.
     * Processes each SNS record by deserializing the SNS message into an {@link SESNotification}
     * and passing it to the specified {@link SESNotificationHandler} implementation for further processing.
     * This method is responsible for handling SES notifications such as bounce, complaint, and delivery events.
     * If deserialization or processing fails for any record, an error message is logged using the Lambda execution context.
     *
     * @param snsEvent The SNS event containing one or more SNS records. Each record represents a single
     *                 notification sent via Amazon Simple Notification Service (SNS) and needs to be processed.
     * @param context  The AWS Lambda execution context, providing runtime information such as logging utilities,
     *                 the remaining execution time for the Lambda function, and the identifier of the invoked function.
     * @return Always returns {@code null} as a Void response type is used for this AWS Lambda handler method.
     */
    @Override
    public Void handleRequest(SNSEvent snsEvent, Context context) {
        for (SNSEvent.SNSRecord record : snsEvent.getRecords()) {
            try {
                SESNotification notification = mapper.readValue(record.getSNS().getMessage(), SESNotification.class);
                sesHandler.handleNotification(notification, context);
            } catch (Exception e) {
                context.getLogger().log("Failed to handle message: " + e.getMessage());
            }
        }
        return null;
    }

    private SESNotificationHandler loadHandlerFromEnv() {
        String handlerClassName = System.getenv("SES_NOTIFICATION_HANDLER_CLASS");
        if (handlerClassName == null || handlerClassName.isBlank()) {
            return new LoggingSESNotificationHandler();
        }

        try {
            Class<?> clazz = Class.forName(handlerClassName);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            if (!(instance instanceof SESNotificationHandler handler)) {
                throw new IllegalArgumentException("Class does not implement SESNotificationHandler: " + handlerClassName);
            }

            return handler;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load handler class: " + handlerClassName, e);
        }
    }

}
