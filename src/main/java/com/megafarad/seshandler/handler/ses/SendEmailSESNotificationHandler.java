package com.megafarad.seshandler.handler.ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.megafarad.seshandler.model.*;
import static com.megafarad.seshandler.utils.EnvVar.*;
import com.megafarad.seshandler.rendering.ProductionRenderingService;
import com.megafarad.seshandler.rendering.RenderedEmail;
import com.megafarad.seshandler.rendering.RenderingService;
import com.megafarad.seshandler.sender.EmailSender;
import com.megafarad.seshandler.sender.ProductionEmailSender;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.StringLoader;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A handler for processing Amazon SES notifications such as bounce, complaint,
 * and delivery events. This handler generates email notifications based on
 * predefined templates and sends them to a designated recipient.
 * <p>
 * This class extends {@link AbstractSESNotificationHandler}, overriding methods
 * to handle specific SES notification types: bounce, complaint, and delivery.
 * Emails are rendered using templates managed by the {@link PebbleEngine} and
 * sent via an {@link EmailSender}.
 */
public class SendEmailSESNotificationHandler extends AbstractSESNotificationHandler {

    private final EmailSender emailSender;
    private final RenderingService renderingService;
    private final PebbleTemplate htmlBounceBodyTemplate;
    private final PebbleTemplate textBounceBodyTemplate;
    private final PebbleTemplate htmlComplaintBodyTemplate;
    private final PebbleTemplate textComplaintBodyTemplate;
    private final PebbleTemplate htmlDeliveryBodyTemplate;
    private final PebbleTemplate textDeliveryBodyTemplate;
    private final PebbleTemplate bounceSubjectTemplate;
    private final PebbleTemplate complaintSubjectTemplate;
    private final PebbleTemplate deliverySubjectTemplate;
    private final String senderEmail;
    private final String recipientEmail;


    /**
     * Constructs a SendEmailSESNotificationHandler instance and initializes its internal
     * components, including email sender, rendering service, and templates for handling
     * various email events such as bounces, complaints, and deliveries.
     * <p>
     * This constructor performs the following actions:
     * - Configures an email sender instance using a production-ready implementation.
     * - Sets up a rendering service for processing email templates.
     * - Loads and prepares templates for both HTML and plain text emails for
     *   bounce, complaint, and delivery event notifications, using environment
     *   variables for template paths or default values if not provided.
     * - Initializes subject templates for the different types of email notifications.
     * - Retrieves sender and recipient email addresses from mandatory environment
     *   variables.
     * <p>
     * The templates and email addresses are utilized during the processing of
     * SES (Simple Email Service) notifications to compose and send appropriate
     * email responses.
     *
     * @throws IllegalStateException If any required environment variable (e.g., sender email,
     *                               recipient email) is missing or invalid.
     */
    public SendEmailSESNotificationHandler() {
        this.emailSender = new ProductionEmailSender();
        this.renderingService = new ProductionRenderingService();
        PebbleEngine bodyEngine = new PebbleEngine.Builder().autoEscaping(true).build();
        PebbleEngine subjectEngine = new PebbleEngine.Builder().loader(new StringLoader()).build();
        this.htmlBounceBodyTemplate = bodyEngine.getTemplate(getEnvVar("HTML_BOUNCE_TEMPLATE",
                "templates/html-bounce-notification.peb"));
        this.htmlComplaintBodyTemplate = bodyEngine.getTemplate(getEnvVar("HTML_COMPLAINT_TEMPLATE",
                "templates/html-complaint-notification.peb"));
        this.htmlDeliveryBodyTemplate = bodyEngine.getTemplate(getEnvVar("HTML_DELIVERY_TEMPLATE",
                "templates/html-delivery-notification.peb"));
        this.textBounceBodyTemplate = bodyEngine.getTemplate(getEnvVar("TEXT_BOUNCE_TEMPLATE",
                "templates/text-bounce-notification.peb"));
        this.textComplaintBodyTemplate = bodyEngine.getTemplate(getEnvVar("TEXT_COMPLAINT_TEMPLATE",
                "templates/text-complaint-notification.peb"));
        this.textDeliveryBodyTemplate = bodyEngine.getTemplate(getEnvVar("TEXT_DELIVERY_TEMPLATE",
                "templates/text-delivery-notification.peb"));
        this.bounceSubjectTemplate = subjectEngine.getTemplate(getEnvVar("BOUNCE_SUBJECT",
                "Bounce Email Notification"));
        this.complaintSubjectTemplate = subjectEngine.getTemplate(getEnvVar("COMPLAINT_SUBJECT",
                "Complaint Email Notification"));
        this.deliverySubjectTemplate = subjectEngine.getTemplate(getEnvVar("DELIVERY_SUBJECT",
                "Delivery Email Notification"));
        this.senderEmail = getEnvVar("SENDER_EMAIL");
        this.recipientEmail = getEnvVar("RECIPIENT_EMAIL");
    }

    /**
     * Constructs a SendEmailSESNotificationHandler instance, initializing various components
     * required for handling SES notification emails effectively, including the email sender,
     * rendering service, and templates for different SES event types (bounce, complaint, delivery).
     *
     * @param emailSender                 An instance of EmailSender to send the composed emails.
     * @param renderingService            A RenderingService responsible for generating email content
     *                                    from templates and context data.
     * @param htmlBounceBodyTemplate      The Pebble template used for the HTML body of bounce emails.
     * @param textBounceBodyTemplate      The Pebble template used for the plain text body of bounce emails.
     * @param htmlComplaintBodyTemplate   The Pebble template used for the HTML body of complaint emails.
     * @param textComplaintBodyTemplate   The Pebble template used for the plain text body of complaint emails.
     * @param htmlDeliveryBodyTemplate    The Pebble template used for the HTML body of delivery emails.
     * @param textDeliveryBodyTemplate    The Pebble template used for the plain text body of delivery emails.
     * @param bounceSubjectTemplate       The Pebble template for the subject line of bounce emails.
     * @param complaintSubjectTemplate    The Pebble template for the subject line of complaint emails.
     * @param deliverySubjectTemplate     The Pebble template for the subject line of delivery emails.
     * @param senderEmail                 The sender email address used for the outgoing SES notifications.
     * @param recipientEmail              The recipient email address used for the outgoing SES notifications.
     */
    public SendEmailSESNotificationHandler(EmailSender emailSender,
                                           RenderingService renderingService,
                                           PebbleTemplate htmlBounceBodyTemplate,
                                           PebbleTemplate textBounceBodyTemplate,
                                           PebbleTemplate htmlComplaintBodyTemplate,
                                           PebbleTemplate textComplaintBodyTemplate,
                                           PebbleTemplate htmlDeliveryBodyTemplate,
                                           PebbleTemplate textDeliveryBodyTemplate,
                                           PebbleTemplate bounceSubjectTemplate,
                                           PebbleTemplate complaintSubjectTemplate,
                                           PebbleTemplate deliverySubjectTemplate,
                                           String senderEmail,
                                           String recipientEmail) {
        this.emailSender = emailSender;
        this.renderingService = renderingService;
        this.htmlBounceBodyTemplate = htmlBounceBodyTemplate;
        this.textBounceBodyTemplate = textBounceBodyTemplate;
        this.htmlComplaintBodyTemplate = htmlComplaintBodyTemplate;
        this.textComplaintBodyTemplate = textComplaintBodyTemplate;
        this.htmlDeliveryBodyTemplate = htmlDeliveryBodyTemplate;
        this.textDeliveryBodyTemplate = textDeliveryBodyTemplate;
        this.bounceSubjectTemplate = bounceSubjectTemplate;
        this.complaintSubjectTemplate = complaintSubjectTemplate;
        this.deliverySubjectTemplate = deliverySubjectTemplate;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
    }


    /**
     * Processes and handles bounce notifications from Amazon Simple Email Service (SES). A bounce
     * occurs when an email cannot be delivered to its intended recipient(s). This method generates
     * a notification email to the appropriate recipient, logging the bounce and sending details about
     * the event.
     *
     * @param mail   The mail object containing details of the original email, such as sender,
     *               recipients, and headers.
     * @param bounce The bounce object containing details about the bounce event, including
     *               type, sub-type, impacted recipients, and metadata.
     * @param context The AWS Lambda execution context used to log and manage metadata
     *                related to the SES event processing.
     */
    @Override
    protected void handleBounce(Mail mail, Bounce bounce, Context context) {
        super.handleBounce(mail, bounce, context);
        context.getLogger().log("Bounce received: " + bounce);

        Map<String, Object> templateContext = buildBounceTemplateContext(mail, bounce);

        RenderedEmail email = renderingService.generateEmail(bounceSubjectTemplate, htmlBounceBodyTemplate,
                textBounceBodyTemplate, templateContext);

        emailSender.send(senderEmail, recipientEmail, email.subject(), email.htmlBody(), email.textBody());
    }

    /**
     * Processes and handles complaint notifications received from Amazon Simple Email Service (SES).
     * Complaint notifications occur when a recipient flags an email as spam or unwanted. This method
     * logs the complaint, generates an email notification to the appropriate recipient using the
     * configured templates, and sends the notification.
     *
     * @param mail      An object containing details about the original email that triggered the
     *                  complaint, such as sender, recipients, and headers.
     * @param complaint An object containing information about the complaint event, including
     *                  recipients who filed the complaint, the type of complaint, and associated metadata.
     * @param context   The AWS Lambda execution context, which provides runtime metadata and logging
     *                  utilities to track and handle the complaint processing.
     */
    @Override
    protected void handleComplaint(Mail mail, Complaint complaint, Context context) {
        super.handleComplaint(mail, complaint, context);
        context.getLogger().log("Complaint received: " + complaint);

        Map<String, Object> templateContext = buildComplaintTemplateContext(mail, complaint);

        RenderedEmail email = renderingService.generateEmail(complaintSubjectTemplate, htmlComplaintBodyTemplate,
                textComplaintBodyTemplate, templateContext);

        emailSender.send(senderEmail, recipientEmail, email.subject(), email.htmlBody(), email.textBody());
    }

    /**
     * Processes and handles delivery notifications received from Amazon Simple Email Service (SES).
     * Delivery notifications indicate that an email has been successfully delivered to the recipient's mail server.
     * This method logs the delivery, generates an email notification using preconfigured templates, and sends the notification.
     *
     * @param mail      An object containing details about the original email, such as sender, recipients, and headers.
     * @param delivery  An object containing information about the delivery event, including timestamp,
     *                  processing time, SMTP response, and recipient details.
     * @param context   The AWS Lambda execution context, which provides runtime metadata and logging facilities
     *                  to handle the delivery processing.
     */
    @Override
    protected void handleDelivery(Mail mail, Delivery delivery, Context context) {
        super.handleDelivery(mail, delivery, context);
        context.getLogger().log("Delivery received: " + delivery);

        Map<String, Object> templateContext = buildDeliveryTemplateContext(mail, delivery);

        RenderedEmail email = renderingService.generateEmail(deliverySubjectTemplate, htmlDeliveryBodyTemplate,
                textDeliveryBodyTemplate, templateContext);

        emailSender.send(senderEmail, recipientEmail, email.subject(), email.htmlBody(), email.textBody());
    }


    private Map<String, Object> buildMailTemplateContext(Mail mail) {
        Map<String, Object> context = new HashMap<>();
        context.put("timestamp", mail.timestamp());
        context.put("messageId", mail.messageId());
        context.put("source", mail.source());
        context.put("sourceArn", mail.sourceArn());
        context.put("sourceIp", mail.sourceIp());
        context.put("sendingAccountId", mail.sendingAccountId());
        context.put("callerIdentity", mail.callerIdentity());
        context.put("destination", mail.destination());
        context.put("headersTruncated", mail.headersTruncated());

        Map<String, Object> headersContext = new HashMap<>();
        mail.headers().forEach(header -> headersContext.put(header.name(), header.value()));
        context.put("headers", headersContext);

        Map<String, Object> commonHeadersContext = new HashMap<>();
        commonHeadersContext.put("from", mail.commonHeaders().from());
        commonHeadersContext.put("to", mail.commonHeaders().to());
        commonHeadersContext.put("date", mail.commonHeaders().date());
        commonHeadersContext.put("messageId", mail.commonHeaders().messageId());
        commonHeadersContext.put("subject", mail.commonHeaders().subject());
        context.put("commonHeaders", commonHeadersContext);

        return context;
    }

    private Map<String, Object> buildBounceTemplateContext(Mail mail, Bounce bounce) {
        Map<String, Object> bounceContext = new HashMap<>();
        bounceContext.put("bounceType", bounce.bounceType());
        bounceContext.put("bounceSubType", bounce.bounceSubType());

        bounceContext.put("bouncedRecipients",
                bounce.bouncedRecipients()
                        .stream()
                        .map(this::buildBouncedRecipientContext)
                        .collect(Collectors.toList()));

        bounceContext.put("timestamp", bounce.timestamp());
        bounceContext.put("feedbackId", bounce.feedbackId());
        bounceContext.put("remoteMtaIp", bounce.remoteMtaIp());
        bounceContext.put("reportingMTA", bounce.reportingMTA());

        Map<String, Object> context = new HashMap<>();

        context.put("bounce", bounceContext);
        context.put("mail", buildMailTemplateContext(mail));

        return context;
    }

    private Map<String, Object> buildBouncedRecipientContext(BouncedRecipient bouncedRecipient) {
        Map<String, Object> context = new HashMap<>();

        context.put("emailAddress", bouncedRecipient.emailAddress());
        context.put("action", bouncedRecipient.action());
        context.put("status", bouncedRecipient.status());
        context.put("diagnosticCode", bouncedRecipient.diagnosticCode());

        return context;
    }

    private Map<String, Object> buildComplaintTemplateContext(Mail mail, Complaint complaint) {
        Map<String, Object> complaintContext = new HashMap<>();

        complaintContext.put("complainedRecipients", complaint.complainedRecipients()
                .stream()
                .map(this::buildComplainedRecipientContext)
                .collect(Collectors.toList()));

        complaintContext.put("timestamp", complaint.timestamp());
        complaintContext.put("complaintSubType", complaint.complaintSubType());
        complaintContext.put("feedbackId", complaint.feedbackId());
        complaintContext.put("userAgent", complaint.userAgent());
        complaintContext.put("complaintFeedbackType", complaint.complaintFeedbackType());
        complaintContext.put("arrivalDate", complaint.arrivalDate());

        Map<String, Object> context = new HashMap<>();
        context.put("complaint", complaintContext);
        context.put("mail", buildMailTemplateContext(mail));

        return context;
    }

    private Map<String, Object> buildComplainedRecipientContext(ComplainedRecipient complainedRecipient) {
        Map<String, Object> context = new HashMap<>();

        context.put("emailAddress", complainedRecipient.emailAddress());

        return context;
    }

    private Map<String, Object> buildDeliveryTemplateContext(Mail mail, Delivery delivery) {
        Map<String, Object> deliveryContext = new HashMap<>();

        deliveryContext.put("timestamp", delivery.timestamp());
        deliveryContext.put("processingTimeMillis", delivery.processingTimeMillis());
        deliveryContext.put("recipients", delivery.recipients());
        deliveryContext.put("smtpResponse", delivery.smtpResponse());
        deliveryContext.put("reportingMTA", delivery.reportingMTA());
        deliveryContext.put("remoteMtaIp", delivery.remoteMtaIp());

        Map<String, Object> context = new HashMap<>();
        context.put("delivery", deliveryContext);
        context.put("mail", buildMailTemplateContext(mail));

        return context;
    }

}
