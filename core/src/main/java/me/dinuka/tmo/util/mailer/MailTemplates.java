/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.dinuka.tmo.util.mailer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author E
 */
public class MailTemplates {

    StringBuilder mailerBody = new StringBuilder();

    String mainColor = "#f19108";

    public MailTemplates() {
        //
    }

    public String accountVerifyMail(String VerificationCode) {
        StringBuilder mailerBody = new StringBuilder();
        mailerBody.append("<!DOCTYPE html>");
        mailerBody.append("<html lang=\"en\">");
        mailerBody.append("<head>");
        mailerBody.append("<meta charset=\"UTF-8\">");
        mailerBody.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        mailerBody.append("<title>TechMartOnline Account Verification</title>");
        mailerBody.append("<style>");
        mailerBody.append("body {");
        mailerBody.append("font-family: Arial, sans-serif;");
        mailerBody.append("background-color: #f5f5f5;");
        mailerBody.append("margin: 0;");
        mailerBody.append("padding: 20px;");
        mailerBody.append("}");
        mailerBody.append(".email-container {");
        mailerBody.append("max-width: 500px;");
        mailerBody.append("margin: 0 auto;");
        mailerBody.append("background-color: white;");
        mailerBody.append("border-radius: 8px;");
        mailerBody.append("overflow: hidden;");
        mailerBody.append("box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);");
        mailerBody.append("}");
        mailerBody.append(".header {");
        mailerBody.append("background-color: #4a90e2;");
        mailerBody.append("padding: 30px;");
        mailerBody.append("text-align: center;");
        mailerBody.append("color: white;");
        mailerBody.append("}");
        mailerBody.append(".logo {");
        mailerBody.append("width: 250px;");
        mailerBody.append("height: auto;");
        mailerBody.append("}");
        mailerBody.append(".brand-name {");
        mailerBody.append("font-size: 24px;");
        mailerBody.append("font-weight: bold;");
        mailerBody.append("}");
        mailerBody.append(".content {");
        mailerBody.append("padding: 30px;");
        mailerBody.append("text-align: center;");
        mailerBody.append("}");
        mailerBody.append(".message {");
        mailerBody.append("font-size: 16px;");
        mailerBody.append("color: #333;");
        mailerBody.append("margin-bottom: 30px;");
        mailerBody.append("}");
        mailerBody.append(".verification-code {");
        mailerBody.append("font-size: 32px;");
        mailerBody.append("font-weight: bold;");
        mailerBody.append("color: #4a90e2;");
        mailerBody.append("letter-spacing: 5px;");
        mailerBody.append("background-color: #f8f9fa;");
        mailerBody.append("padding: 20px;");
        mailerBody.append("border-radius: 5px;");
        mailerBody.append("margin: 20px 0;");
        mailerBody.append("border: 2px solid #4a90e2;");
        mailerBody.append("}");
        mailerBody.append(".footer {");
        mailerBody.append("background-color: #333;");
        mailerBody.append("color: white;");
        mailerBody.append("padding: 20px;");
        mailerBody.append("text-align: center;");
        mailerBody.append("font-size: 14px;");
        mailerBody.append("}");
        mailerBody.append("@media (max-width: 600px) {");
        mailerBody.append(".email-container {");
        mailerBody.append("margin: 10px;");
        mailerBody.append("}");
        mailerBody.append(".header, .content, .footer {");
        mailerBody.append("padding: 20px;");
        mailerBody.append("}");
        mailerBody.append(".verification-code {");
        mailerBody.append("font-size: 24px;");
        mailerBody.append("letter-spacing: 3px;");
        mailerBody.append("}");
        mailerBody.append("}");
        mailerBody.append("</style>");
        mailerBody.append("</head>");
        mailerBody.append("<body>");
        mailerBody.append("<div class=\"email-container\">");
        mailerBody.append("<div class=\"header\">");
        mailerBody.append("<img src=\"https://raw.githubusercontent.com/DinukaDilshan415/images/7a5f610434dedce8344f98788f3eaffcaf11b708/tmlogowhite.svg\" alt=\"TechMart\" class=\"logo\">");
        mailerBody.append("</div>");
        mailerBody.append("<div class=\"content\">");
        mailerBody.append("<div class=\"message\">");
        mailerBody.append("Hi,<br><br>");
        mailerBody.append("Please use this code to verify your account:");
        mailerBody.append("</div>");
        mailerBody.append("<div class=\"verification-code\">" + VerificationCode + "</div>");
        mailerBody.append("<div class=\"message\">");
        mailerBody.append("Don't share this code with anyone.");
        mailerBody.append("</div>");
        mailerBody.append("</div>");
        mailerBody.append("<div class=\"footer\">");
        mailerBody.append("© 2025 TechmartOnline. All rights reserved.");
        mailerBody.append("</div>");
        mailerBody.append("</div>");
        mailerBody.append("</body>");
        mailerBody.append("</html>");
        return mailerBody.toString();
    }

    public String adminVerificationEmail(String verificationCode) {
        StringBuilder mailerBody = new StringBuilder();

        mailerBody.append("<!DOCTYPE html>");
        mailerBody.append("<html lang=\"en\">");
        mailerBody.append("<head>");
        mailerBody.append("    <meta charset=\"UTF-8\">");
        mailerBody.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        mailerBody.append("    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">");
        mailerBody.append("    <title>TechMartOnline Admin Verification</title>");
        mailerBody.append("    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap\" rel=\"stylesheet\">");
        mailerBody.append("    <style>");
        mailerBody.append("        body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }");
        mailerBody.append("        table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }");
        mailerBody.append("        img { -ms-interpolation-mode: bicubic; border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }");
        mailerBody.append("        table { border-collapse: collapse !important; }");
        mailerBody.append("        body { height: 100% !important; margin: 0 !important; padding: 0 !important; width: 100% !important; font-family: 'Inter', 'Arial', sans-serif; }");
        mailerBody.append("        .primary-color { color: #0096ed; }");
        mailerBody.append("        .secondary-color-bg { background-color: #b2d235; }");
        mailerBody.append("        @media screen and (max-width: 600px) { .content-table { width: 100% !important; } }");
        mailerBody.append("    </style>");
        mailerBody.append("</head>");
        mailerBody.append("<body style=\"background-color: #f7f7f7; margin: 0 !important; padding: 0 !important;\">");
        mailerBody.append("    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        mailerBody.append("        <tr>");
        mailerBody.append("            <td align=\"center\" style=\"padding: 40px 0 30px 0;\">");
        mailerBody.append("                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td align=\"center\" valign=\"top\" style=\"padding: 0 10px;\">");
        mailerBody.append("                            <a href=\"https://techmartonline.com\" target=\"_blank\">");
        mailerBody.append("                                <img src=\"https://raw.githubusercontent.com/DinukaDilshan415/images/7a5f610434dedce8344f98788f3eaffcaf11b708/tmsvg.svg\" alt=\"TechMart Logo\" width=\"180\" style=\"display: block; width: 180px; max-width: 100%; min-width: 100px; font-family: 'Inter', sans-serif; color: #333333; font-size: 18px;\" border=\"0\">");
        mailerBody.append("                            </a>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                </table>");
        mailerBody.append("            </td>");
        mailerBody.append("        </tr>");
        mailerBody.append("        <tr>");
        mailerBody.append("            <td align=\"center\" style=\"padding: 0 10px;\">");
        mailerBody.append("                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"content-table\" style=\"max-width: 600px; background-color: #ffffff; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.07);\">");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td align=\"center\" style=\"padding: 40px 30px 20px 30px; font-family: 'Inter', sans-serif; font-size: 16px; line-height: 24px; color: #555555;\">");
        mailerBody.append("                            <h1 style=\"font-size: 26px; font-weight: 700; margin: 0; color: #1e1e1e;\">Admin Verification Code</h1>");
        mailerBody.append("                            <p style=\"margin: 20px 0 0;\">Hello Admin, please use the following verification code to complete your secure action. This code will expire in 10 minutes.</p>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td align=\"center\" style=\"padding: 20px 30px 30px 30px;\">");
        mailerBody.append("                            <div style=\"font-family: 'Courier New', Courier, monospace; font-size: 36px; font-weight: bold; letter-spacing: 5px; color: #0096ed; background-color: #f0f8ff; padding: 20px 30px; border-radius: 8px; border: 1px dashed #0096ed;\">");
        mailerBody.append(verificationCode);
        mailerBody.append("                            </div>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td align=\"center\" style=\"padding: 0px 30px 40px 30px; font-family: 'Inter', sans-serif; font-size: 14px; line-height: 20px; color: #888888;\">");
        mailerBody.append("                            <p style=\"margin: 0;\">If you did not request this code, you can safely ignore this email. Your account remains secure.</p>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                </table>");
        mailerBody.append("            </td>");
        mailerBody.append("        </tr>");
        mailerBody.append("        <tr>");
        mailerBody.append("            <td align=\"center\" style=\"padding: 30px 10px 40px 10px;\">");
        mailerBody.append("                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td align=\"center\" style=\"padding: 0; font-family: 'Inter', sans-serif; font-size: 12px; line-height: 18px; color: #888888;\">");
        mailerBody.append("                            &copy; 2025 TechmartOnline. All Rights Reserved. <br>");
        mailerBody.append("                            123 Learning Lane, Knowledge City, 10101<br>");
        mailerBody.append("                            <a href=\"#\" style=\"color: #0096ed; text-decoration: none;\">Privacy Policy</a> &bull; <a href=\"#\" style=\"color: #0096ed; text-decoration: none;\">Unsubscribe</a>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                </table>");
        mailerBody.append("            </td>");
        mailerBody.append("        </tr>");
        mailerBody.append("    </table>");
        mailerBody.append("</body>");
        mailerBody.append("</html>");

        return mailerBody.toString();
    }
}
