package com.nilesh.smartexpense.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending emails.
 * Currently handles OTP verification emails.
 * Can be extended in future for expense reports,
 * budget alerts, and other notification emails.
 *
 * Uses Gmail SMTP configured via environment variables.
 * To switch email provider, only application.properties needs updating.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Sends an OTP verification email to the user.
     * Email is sent as HTML for better presentation.
     *
     * @param toEmail   recipient's email address
     * @param name      recipient's name for personalization
     * @param otp       the 6-digit OTP code to include in email
     * @throws RuntimeException if email sending fails
     */
    public void sendOtpEmail(String toEmail, String name, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("SmartExpense AI - Your Verification Code");
            helper.setText(buildOtpEmailTemplate(name, otp), true);

            mailSender.send(message);

        } catch (MessagingException ex) {
            throw new RuntimeException("Failed to send verification email. Please try again.");
        }
    }

    /**
     * Builds the HTML email template for OTP verification.
     * Designed to be clean and professional across email clients.
     *
     * @param name  user's name for personalization
     * @param otp   the OTP code to display prominently
     * @return HTML string for the email body
     */
    private String buildOtpEmailTemplate(String name, String otp) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background: linear-gradient(135deg, #1a1a2e, #16213e);
                            padding: 30px; text-align: center; border-radius: 8px 8px 0 0;">
                    <h1 style="color: #00d4ff; margin: 0;">SmartExpense AI</h1>
                    <p style="color: #ccc; margin: 8px 0 0;">Email Verification</p>
                </div>

                <div style="background: #ffffff; padding: 40px; border-radius: 0 0 8px 8px;
                            box-shadow: 0 4px 15px rgba(0,0,0,0.1);">
                    <p style="font-size: 16px; color: #333;">Hi <strong>%s</strong>,</p>
                    <p style="color: #666;">
                        Thank you for registering with SmartExpense AI.
                        Use the verification code below to complete your registration.
                    </p>

                    <div style="background: #f0f2f5; border-radius: 8px; padding: 25px;
                                text-align: center; margin: 30px 0;">
                        <p style="color: #666; margin: 0 0 10px; font-size: 14px;">
                            Your Verification Code
                        </p>
                        <h2 style="color: #1a1a2e; font-size: 42px; letter-spacing: 12px;
                                   margin: 0; font-family: monospace;">%s</h2>
                        <p style="color: #999; font-size: 12px; margin: 10px 0 0;">
                            Valid for 10 minutes only
                        </p>
                    </div>

                    <p style="color: #666; font-size: 14px;">
                        If you did not request this code, please ignore this email.
                        Your account will not be created.
                    </p>

                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        This is an automated message. Please do not reply.
                    </p>
                </div>
            </div>
            """.formatted(name, otp);
    }
}