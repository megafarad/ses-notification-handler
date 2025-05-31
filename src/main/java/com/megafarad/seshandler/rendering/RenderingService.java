package com.megafarad.seshandler.rendering;

import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.Map;

/**
 * Service for rendering emails from templates and context data.
 * This interface defines a contract for generating emails by applying templates and context data.
 * Implementations of this interface should handle the rendering logic and return a RenderedEmail object.
 * @see RenderedEmail
 * @see PebbleTemplate
 *
 */
public interface RenderingService {
    /**
     * Generates an email by rendering the provided templates (subject, HTML body,
     * and text body) using the specified context.
     *
     * @param subjectTemplate the template used to generate the email's subject
     * @param htmlBodyTemplate the template used to generate the HTML version of the email body
     * @param textBodyTemplate the template used to generate the plain text version of the email body
     * @param context a map containing the variables to be used during the rendering of the templates
     * @return a RenderedEmail object containing the generated subject, HTML body, and text body of the email
     */
    RenderedEmail generateEmail(PebbleTemplate subjectTemplate, PebbleTemplate htmlBodyTemplate,
                                       PebbleTemplate textBodyTemplate, Map<String, Object> context);
}
