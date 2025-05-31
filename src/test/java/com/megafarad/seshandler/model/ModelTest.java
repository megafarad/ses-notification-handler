package com.megafarad.seshandler.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megafarad.seshandler.model.SESNotification;
import com.megafarad.utils.ResourceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void parseBounceWithDSN() {
        assertParsedNotification("bounce_with_dsn.json");
    }

    @Test
    public void parseBounceWithoutDSN() {
        assertParsedNotification("bounce_without_dsn.json");
    }

    @Test
    public void parseComplaintWithFeedback() {
        assertParsedNotification("complaint_with_feedback.json");
    }

    @Test
    public void parseComplaintWithoutFeedback() {
        assertParsedNotification("complaint_without_feedback.json");
    }

    @Test
    public void parseDelivery() {
        assertParsedNotification("delivery.json");
    }

    private void assertParsedNotification(String path) {
        String json = ResourceReader.readResourceToString(path);
        try {
            SESNotification notification = mapper.readValue(json, SESNotification.class);
            Assertions.assertNotNull(notification);
        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
