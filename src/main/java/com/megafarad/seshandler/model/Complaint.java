package com.megafarad.seshandler.model;

import java.util.List;

/**
 * Represents a complaint received from SES.
 *
 * @param complainedRecipients List of recipients who complained about the email
 * @param timestamp Timestamp of the complaint
 * @param complaintSubType Subtype of the complaint
 * @param feedbackId Feedback ID associated with the complaint
 * @param userAgent User agent information
 * @param complaintFeedbackType Type of complaint feedback
 * @param arrivalDate Arrival date of the complaint
 */
public record Complaint(
        List<ComplainedRecipient> complainedRecipients,
        String timestamp,
        String complaintSubType,
        String feedbackId,
        String userAgent,
        String complaintFeedbackType,
        String arrivalDate
) { }
