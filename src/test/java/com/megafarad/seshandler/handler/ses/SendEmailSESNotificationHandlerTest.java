package com.megafarad.seshandler.handler.ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.megafarad.seshandler.model.SESNotification;
import com.megafarad.seshandler.rendering.ProductionRenderingService;
import com.megafarad.seshandler.sender.EmailSender;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.StringLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.megafarad.utils.SESNotificationParser.parseNotification;
import static org.mockito.Mockito.*;


class SendEmailSESNotificationHandlerTest {

    private final String senderEmail = "sender@example.com";

    private final String recipientEmail = "recipient@example.com";

    private final String bounceSubject = "Bounce Email Notification";

    private final String complaintSubject = "Complaint Email Notification";

    private final String deliverySubject = "Delivery Email Notification";

    private AutoCloseable closeable;

    @Mock
    private EmailSender emailSender;

    @Mock
    private Context context;

    @Mock
    private LambdaLogger logger;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(context.getLogger()).thenReturn(logger);
    }

    @AfterEach
    void destroy() throws Exception {
        closeable.close();
    }

    @Test
    public void testBounceEmailWithDSN() {

        SendEmailSESNotificationHandler handler = createHandler(emailSender);
        SESNotification notification = parseNotification("bounce_with_dsn.json");
        handler.handleNotification(notification, context);
        verify(emailSender).send(eq(senderEmail),
                eq(recipientEmail),
                eq(bounceSubject),
                argThat(s -> s.contains("jane@example.com") &&
                        s.contains("5.1.1") &&
                        s.contains("(failed)") &&
                        s.contains("<strong>Bounce Type:</strong> Permanent</div>") &&
                        s.contains("<strong>Bounce Sub Type:</strong> General</div>")),
                argThat(s -> s.contains("jane@example.com") &&
                        s.contains("5.1.1") &&
                        s.contains("(failed)") &&
                        s.contains("Bounce Type: Permanent") &&
                        s.contains("Bounce Sub Type: General")));
    }

    @Test
    public void testBounceEmailWithoutDSN() {
        SendEmailSESNotificationHandler handler = createHandler(emailSender);
        SESNotification notification = parseNotification("bounce_without_dsn.json");
        handler.handleNotification(notification, context);
        verify(emailSender).send(eq(senderEmail),
                eq(recipientEmail),
                eq(bounceSubject),
                argThat(s -> s.contains("jane@example.com") &&
                        s.contains("richard@example.com")),
                argThat(s -> s.contains("jane@example.com") &&
                        s.contains("richard@example.com")));
    }

    @Test
    public void testComplaintWithFeedback() {
        SendEmailSESNotificationHandler handler = createHandler(emailSender);
        SESNotification notification = parseNotification("complaint_with_feedback.json");
        handler.handleNotification(notification, context);
        verify(emailSender).send(eq(senderEmail),
                eq(recipientEmail),
                eq(complaintSubject),
                argThat(s -> s.contains("richard@example.com") &&
                        s.contains("abuse")),
                argThat(s -> s.contains("richard@example.com") &&
                        s.contains("abuse")));
    }

    @Test
    public void testComplaintWithoutFeedback() {
        SendEmailSESNotificationHandler handler = createHandler(emailSender);
        SESNotification notification = parseNotification("complaint_without_feedback.json");
        handler.handleNotification(notification, context);
        verify(emailSender).send(eq(senderEmail),
                eq(recipientEmail),
                eq(complaintSubject),
                argThat(s -> s.contains("richard@example.com") &&
                                    !s.contains("Complaint Feedback Type")),
                argThat(s -> s.contains("richard@example.com") &&
                                    !s.contains("Complaint Feedback Type")));
    }

    @Test
    public void testDelivery() {
        SendEmailSESNotificationHandler handler = createHandler(emailSender);
        SESNotification notification = parseNotification("delivery.json");
        handler.handleNotification(notification, context);
        verify(emailSender).send(eq(senderEmail),
                eq(recipientEmail),
                eq(deliverySubject),
                argThat(s -> s.contains("jane@example.com")),
                argThat(s -> s.contains("jane@example.com")));
    }

    private SendEmailSESNotificationHandler createHandler(EmailSender emailSender) {
        PebbleEngine bodyEngine = new PebbleEngine.Builder().autoEscaping(true).build();
        PebbleEngine subjectEngine = new PebbleEngine.Builder().loader(new StringLoader()).build();

        return new SendEmailSESNotificationHandler(
                emailSender,
                new ProductionRenderingService(),
                bodyEngine.getTemplate("templates/html-bounce-notification.peb"),
                bodyEngine.getTemplate("templates/text-bounce-notification.peb"),
                bodyEngine.getTemplate("templates/html-complaint-notification.peb"),
                bodyEngine.getTemplate("templates/text-complaint-notification.peb"),
                bodyEngine.getTemplate("templates/html-delivery-notification.peb"),
                bodyEngine.getTemplate("templates/text-delivery-notification.peb"),
                subjectEngine.getTemplate(bounceSubject),
                subjectEngine.getTemplate(complaintSubject),
                subjectEngine.getTemplate(deliverySubject),
                senderEmail,
                recipientEmail

        );
    }

}
