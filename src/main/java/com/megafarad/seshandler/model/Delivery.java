package com.megafarad.seshandler.model;

import java.util.List;

/**
 * Represents a delivery event from SES.
 *
 * @param timestamp Timestamp of the delivery event
 * @param processingTimeMillis Processing time in milliseconds
 * @param recipients List of recipients for the delivery
 * @param smtpResponse SMTP response for the delivery
 * @param reportingMTA Reporting MTA for the delivery
 * @param remoteMtaIp Remote MTA IP address
 */
public record Delivery(
        String timestamp,
        Long processingTimeMillis,
        List<String> recipients,
        String smtpResponse,
        String reportingMTA,
        String remoteMtaIp
) { }
