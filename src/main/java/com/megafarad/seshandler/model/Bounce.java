package com.megafarad.seshandler.model;

import java.util.List;

/**
 * Represents the details of a bounce notification from Amazon Simple Email Service (SES).
 * A bounce occurs when an email cannot be delivered to one or more recipients.
 *
 * This record encapsulates the following key information about a bounce event:
 *
 * @param bounceType The type of bounce (e.g., Permanent, Transient).
 * @param bounceSubType Further categorization of the bounce reason (e.g., General, Suppressed).
 * @param bouncedRecipients A list of recipients affected by the bounce, including details such as email address and diagnostic code.
 * @param timestamp The time at which the bounce event occurred.
 * @param feedbackId A unique identifier for the feedback notification related to this bounce.
 * @param remoteMtaIp The IP address of the mail transfer agent (MTA) that reported the bounce.
 * @param reportingMTA The mail transfer agent (MTA) responsible for sending the bounce report.
 */
public record Bounce(
        String bounceType,
        String bounceSubType,
        List<BouncedRecipient> bouncedRecipients,
        String timestamp,
        String feedbackId,
        String remoteMtaIp,
        String reportingMTA
) { }
