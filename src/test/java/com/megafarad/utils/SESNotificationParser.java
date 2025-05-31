package com.megafarad.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megafarad.seshandler.model.SESNotification;

public class SESNotificationParser {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static SESNotification parseNotification(String path) {
        String json = ResourceReader.readResourceToString(path);
        try {
            return mapper.readValue(json, SESNotification.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
