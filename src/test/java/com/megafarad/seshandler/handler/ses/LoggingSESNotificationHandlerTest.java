package com.megafarad.seshandler.handler.ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.megafarad.seshandler.model.SESNotification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.megafarad.utils.SESNotificationParser.parseNotification;
import static org.mockito.Mockito.*;


public class LoggingSESNotificationHandlerTest {

    private final LoggingSESNotificationHandler handler = new LoggingSESNotificationHandler();

    private AutoCloseable closeable;

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
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testBounce() {
        SESNotification notification = parseNotification("bounce_with_dsn.json");
        handler.handleNotification(notification, context);
        verify(logger).log(contains("Bounce"));
    }

    @Test
    public void testComplaint() {
        SESNotification notification = parseNotification("complaint_with_feedback.json");
        handler.handleNotification(notification, context);
        verify(logger).log(contains("Complaint"));
    }

    @Test
    public void testDelivery() {
        SESNotification notification = parseNotification("delivery.json");
        handler.handleNotification(notification, context);
        verify(logger).log(contains("Delivery"));
    }
}
