# AWS SES Notification Handler: Automated Email Notifications for SES Events

The AWS SES Notification Handler is a Java-based AWS Lambda function that processes Amazon Simple Email Service (SES) notifications and generates customized email notifications for bounce, complaint, and delivery events. It provides a robust solution for monitoring and responding to email delivery status changes in real-time.

This project helps organizations maintain email deliverability by automatically notifying designated recipients about email delivery events. It processes SES notifications received through SNS, parses the event data, and generates human-readable email notifications using customizable templates. The system supports both HTML and plain text email formats, making it versatile for different email client requirements.

## Repository Structure
```
.
├── pom.xml                 # Maven project configuration and dependencies
├── src
│   ├── main
│   │   ├── java/com/megafarad/seshandler
│   │   │   ├── handler            # Core handlers for processing notifications
│   │   │   │   ├── ses            # SES-specific notification handlers
│   │   │   │   └── sns            # SNS message processing
│   │   │   ├── model              # Data models for SES notifications
│   │   │   ├── rendering          # Email template rendering services
│   │   │   ├── sender             # Email sending implementations
│   │   │   └── utils              # Utility classes
│   │   └── resources
│   │       └── templates          # Pebble email templates for notifications
│   └── test                       # Test cases and resources
```

## Usage Instructions
### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- AWS Account with configured SES and SNS services
- AWS credentials configured locally

### Installation

1. Clone the repository:
```bash
git clone https://github.com/megafarad/ses-notification-handler.git
cd ses-notification-handler
```

2. Build the project:
```bash
mvn clean package
```

3. Configure environment variables:
```bash
export SENDER_EMAIL=your-sender@example.com
export RECIPIENT_EMAIL=your-recipient@example.com
export SES_NOTIFICATION_HANDLER_CLASS=com.megafarad.seshandler.handler.ses.SendEmailSESNotificationHandler
```

### Quick Start

1. Deploy the Lambda function:
```bash
aws lambda create-function \
  --function-name ses-notification-handler \
  --runtime java21 \
  --handler com.megafarad.seshandler.handler.sns.SNSMessageHandler \
  --memory-size 512 \
  --timeout 30 \
  --role arn:aws:iam::<account-id>:role/<role-name> \
  --zip-file fileb://target/ses-notification-handler-1.0.0.jar
```

2. Configure SNS subscription:
```bash
aws sns subscribe \
  --topic-arn arn:aws:sns:<region>:<account-id>:<topic-name> \
  --protocol lambda \
  --notification-endpoint arn:aws:lambda:<region>:<account-id>:function:ses-notification-handler
```

### Troubleshooting

Common issues and solutions:

1. Lambda Execution Issues
- Problem: Lambda function fails to execute
- Solution: Check CloudWatch logs and ensure IAM roles have proper permissions
```bash
aws logs get-log-events --log-group-name /aws/lambda/ses-notification-handler
```

2. Email Delivery Issues
- Problem: Notifications not being sent
- Solution: Verify SES configuration and sender email verification
```bash
aws ses get-send-quota
aws ses verify-email-identity --email-address your-sender@example.com
```

3. Template Rendering Issues
- Problem: Malformed email content
- Solution: Check template syntax in resources/templates directory
- Enable debug logging by setting LOG_LEVEL=DEBUG

## Data Flow
The system processes SES notifications through a series of transformations from raw SNS events to formatted email notifications.

```ascii
SNS Event → SNSMessageHandler → SESNotificationHandler → EmailRenderer → SES
     ↓            ↓                     ↓                    ↓           ↓
JSON Data → Event Parsing → Event Classification → Template → Formatted → Email
                                                 Rendering    Output    Delivery
```

Key component interactions:
1. SNSMessageHandler receives and deserializes SNS events
2. SESNotificationHandler processes specific notification types (bounce/complaint/delivery)
3. RenderingService generates email content using Pebble templates
4. EmailSender delivers notifications via Amazon SES
5. Error handling and logging occur at each step