package com.megafarad.seshandler.rendering;

import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Service for rendering emails from templates and context data.
 * This implementation generates emails by applying the provided templates and context data.
 * It uses the PebbleTemplate engine for rendering the templates.
 *
 * @see RenderingService
 * @see PebbleTemplate
 */
public class ProductionRenderingService implements RenderingService {

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
    @Override
    public RenderedEmail generateEmail(PebbleTemplate subjectTemplate, PebbleTemplate htmlBodyTemplate,
                                       PebbleTemplate textBodyTemplate, Map<String, Object> context) {

        String subject = renderTemplate(subjectTemplate, context);
        String html = renderTemplate(htmlBodyTemplate, context);
        String text = renderTemplate(textBodyTemplate, context);

        return new RenderedEmail(subject, html, text);
    }

    private String renderTemplate(PebbleTemplate template, Map<String, Object> context) {
        try (StringWriter writer = new StringWriter()){
            template.evaluate(writer, context);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Rendering template failed", e);
        }
    }
}
