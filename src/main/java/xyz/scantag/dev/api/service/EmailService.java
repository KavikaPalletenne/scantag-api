package xyz.scantag.dev.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendPasswordResetEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("noreply@scantag.co", "ScanTag Auth");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password - ScanTag";

        String content = "<p>Hello,</p>"
                + "<style>p {font-family: sans-serif; padding-left: 20px;} img {width: 75px;padding-left: 15px; padding-top: 30px;} .button2 {font: 20px 'Rubik'; text-decoration: none; color: white; background-color: #FF9369; padding: 6px 18px 6px 18px; border-radius: 15px;} .button2:hover { background-color: #FFB79B; color: white; border-top: 0px solid #000; border-right: 0px solid #000; border-bottom: 0px solid #000; border-left: 0px solid #000;}</style>"
                + "<p>You have requested to reset your password.</p>"
                + "<p style=\"padding-bottom: 20px;\">Click the link below to change your password:</p>"
                + "<p><a class=\"button2\" href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, or you have not made the request.</p>"
                + "<a href=\"https://scantag.co\"><img style=\"float: left; width: 50px;\" src=\"https://raw.githubusercontent.com/KavikaPalletenne/scantag-assets/main/scantag-logo.svg\" alt=\"ScanTag Logo\"/></a>"
                + "<p style=\"float: left; font-weight: bold; padding-top: 40px;\">ScanTag - The Modern Name Tag</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendNotificationEmail(String recipientEmail, String tagName, String scannerLocation, String scannerIpAddress)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("notifications@scantag.co", "ScanTag Notifications");
        helper.setTo(recipientEmail);

        String subject = "You tag " + tagName + " has been scanned - ScanTag";

        String content = "<p>Hello,</p>"
                + "<style>p {font-family: sans-serif; padding-left: 20px;} img {width: 75px;padding-left: 15px; padding-top: 30px;} .button2 {font: 20px 'Rubik'; text-decoration: none; color: white; background-color: #FF9369; padding: 6px 18px 6px 18px; border-radius: 15px;} .button2:hover { background-color: #FFB79B; color: white; border-top: 0px solid #000; border-right: 0px solid #000; border-bottom: 0px solid #000; border-left: 0px solid #000;}</style>"
                + "<p>Your tag " + tagName + " has been scanned</p>"
                + "<p style=\"padding-bottom: 5px;\">Scanner Location: " + scannerLocation + "(" + scannerIpAddress + ")</p>"
                + "<p style=\"padding-botom: 5px;\">To stop receiving notification emails, change your notification settings in your account:</p>"
                + "<a href=\"https://scantag.co/account\" style=\"text-decoration: none;\">scantag.co/account</a>"
                + "<a href=\"https://scantag.co\"><img style=\"float: left; width: 50px\" src=\"https://raw.githubusercontent.com/KavikaPalletenne/scantag-assets/main/scantag-logo.svg\" alt=\"ScanTag Logo\"/></a>"
                + "<p style=\"float: left; font-weight: bold; padding-top: 40px;\">ScanTag - The Modern Name Tag</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendEmailVerificationEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("noreply@scantag.co", "ScanTag Auth");
        helper.setTo(recipientEmail);

        String subject = "Welcome to ScanTag! Verify your email using this link";

        String content = "<p>Hello,</p>"
                + "<style>p {font-family: sans-serif; padding-left: 20px;} img {width: 75px;padding-left: 15px; padding-top: 30px;} .button2 {font: 20px 'Rubik'; text-decoration: none; color: white; background-color: #FF9369; padding: 6px 18px 6px 18px; border-radius: 15px;} .button2:hover { background-color: #FFB79B; color: white; border-top: 0px solid #000; border-right: 0px solid #000; border-bottom: 0px solid #000; border-left: 0px solid #000;}</style>"
                + "<p>Welcome to ScanTag!</p>"
                + "<p style=\"padding-bottom: 20px;\">Click the link below to verify your email:</p>"
                + "<p><a class=\"button2\" href=\"" + link + "\">Verify my email/a></p>"
                + "<br>"
                + "<p>Ignore this email if you did not sign up for a ScanTag account.</p>"
                + "<a href=\"https://scantag.co\"><img style=\"float: left; width: 50px;\" src=\"https://raw.githubusercontent.com/KavikaPalletenne/scantag-assets/main/scantag-logo.svg\" alt=\"ScanTag Logo\"/></a>"
                + "<p style=\"float: left; font-weight: bold; padding-top: 40px;\">ScanTag - The Modern Name Tag</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
