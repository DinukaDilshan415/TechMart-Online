/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.dinuka.tmo.util.mailer;

import me.dinuka.tmo.entity.Cart;

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

    public String generateOrderConfirmationEmail(HashMap<String, Object> detailMap) {
        StringBuilder mailerBody = new StringBuilder();

        // Start of the HTML document
        mailerBody.append("<!DOCTYPE html>");
        mailerBody.append("<html lang=\"en\">");
        mailerBody.append("<head>");
        mailerBody.append("    <meta charset=\"UTF-8\">");
        mailerBody.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        mailerBody.append("    <title>TechMart-Online - Order Confirmation</title>");
        mailerBody.append("    <style>");
        mailerBody.append("        body { margin: 0; padding: 0; -webkit-text-size-adjust: 100%; width: 100% !important; background-color: #f4f4f4; font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; color: #333333; }");
        mailerBody.append("        @media screen and (max-width: 600px) {");
        mailerBody.append("            .container { width: 100% !important; padding: 0 15px !important; }");
        mailerBody.append("            .column-wrapper { display: block !important; width: 100% !important; }");
        mailerBody.append("            .column { width: 100% !important; display: block !important; padding: 0 0 20px 0 !important; }");
        mailerBody.append("            .logo { width: 180px !important; height: auto !important; }");
        mailerBody.append("            .main-content { padding: 0 20px 20px 20px !important; }");
        mailerBody.append("            .order-summary-header { font-size: 18px !important; }");
        mailerBody.append("            .total-amount { font-size: 18px !important; }");
        mailerBody.append("        }");
        mailerBody.append("    </style>");
        mailerBody.append("</head>");
        mailerBody.append("<body style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; width: 100% !important; background-color: #f4f4f4; font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; color: #333333;\">");
        mailerBody.append("    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color: #f4f4f4;\">");
        mailerBody.append("        <tr>");
        mailerBody.append("            <td align=\"center\" valign=\"top\">");
        mailerBody.append("                <table class=\"container\" width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width: 600px; margin: 0 auto; background-color: #ffffff;\">");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td align=\"center\" style=\"padding: 25px 0;\">");
        mailerBody.append("                            <a href=\"#\" target=\"_blank\"><img class=\"logo\" src=\"https://raw.githubusercontent.com/DinukaDilshan415/images/b66d20d39801680883abb6625a8419c654b1f901/tmsvg.svg\" alt=\"TechMart Logo\" width=\"220\" style=\"display: block; border: 0;\"></a>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td class=\"main-content\" style=\"padding: 0 30px 20px 30px;\">");
        mailerBody.append("                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        mailerBody.append("                                <tr>");
        mailerBody.append("                                    <td align=\"center\" style=\"padding: 10px 0;\">");
        mailerBody.append("                                        <h1 style=\"color: #0096ed; margin: 0; font-size: 26px; font-weight: bold;\">Thank You For Your Order!</h1>");
        mailerBody.append("                                        <p style=\"margin: 8px 0 0 0; font-size: 16px; color: #555555;\">Hi ").append(detailMap.get("billName")).append(", we've received your order and are getting it ready for you.</p>");
        mailerBody.append("                                    </td>");
        mailerBody.append("                                </tr>");
        mailerBody.append("                                <tr>");
        mailerBody.append("                                    <td align=\"center\" style=\"padding: 20px 0; margin-top: 20px; border-top: 1px solid #eeeeee; border-bottom: 1px solid #eeeeee;\">");
        mailerBody.append("                                        <p style=\"margin: 0; font-size: 14px; color: #555555;\">");
        mailerBody.append("                                            <strong style=\"color: #0096ed;\">Order #</strong>").append(detailMap.get("OrderId"));
        mailerBody.append("                                            <span style=\"padding: 0 10px; color: #cccccc;\">|</span>");
        mailerBody.append("                                            <strong style=\"color: #0096ed;\">Placed on</strong> ").append(detailMap.get("placedOn"));
        mailerBody.append("                                        </p>");
        mailerBody.append("                                    </td>");
        mailerBody.append("                                </tr>");
        mailerBody.append("                            </table>");
        mailerBody.append("                            <table class=\"column-wrapper\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-top: 30px;\">");
        mailerBody.append("                                <tr>");
        mailerBody.append("                                    <td class=\"column\" width=\"48%\" valign=\"top\" style=\"padding-right: 2%;\">");
        mailerBody.append("                                        <h3 style=\"margin-top: 0; margin-bottom: 10px; font-size: 18px; border-bottom: 2px solid #0096ed; padding-bottom: 5px; color: #333;\">Billing Info</h3>");
        mailerBody.append("                                        <p style=\"margin: 0; line-height: 1.6; font-size: 14px; color: #555555;\">");
        mailerBody.append(detailMap.get("billName")).append("<br>");
        mailerBody.append(detailMap.get("billAddress")).append("<br>");
        mailerBody.append(detailMap.get("billCity")).append(", ").append(detailMap.get("billPcode")).append("<br>");
        mailerBody.append("Sri Lanka<br>");
        mailerBody.append("T: ").append(detailMap.get("billNumber"));
        mailerBody.append("                                        </p>");
        mailerBody.append("                                    </td>");
        mailerBody.append("                                    <td class=\"column\" width=\"48%\" valign=\"top\" style=\"padding-left: 2%;\">");
        mailerBody.append("                                        <h3 style=\"margin-top: 0; margin-bottom: 10px; font-size: 18px; border-bottom: 2px solid #0096ed; padding-bottom: 5px; color: #333;\">Gift Info</h3>");
        mailerBody.append("                                        <p style=\"margin: 0; line-height: 1.6; font-size: 14px; color: #555555;\">");
        mailerBody.append(detailMap.get("giftName")).append("<br>");
        mailerBody.append(detailMap.get("giftAddress")).append("<br>");
        mailerBody.append(detailMap.get("giftCity")).append(", ").append(detailMap.get("giftPcode")).append("<br>");
        mailerBody.append("Sri Lanka<br>");
        // As requested, using the 'giftNote' field here.
        mailerBody.append("Note: ").append(detailMap.get("giftNote"));
        mailerBody.append("                                        </p>");
        mailerBody.append("                                    </td>");
        mailerBody.append("                                </tr>");
        mailerBody.append("                            </table>");
        mailerBody.append("                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-top: 30px; border: 1px solid #dddddd; border-collapse: collapse;\">");
        mailerBody.append("                                <thead>");
        mailerBody.append("                                    <tr><td colspan=\"4\" class=\"order-summary-header\" style=\"background-color: #b2d235; padding: 15px; font-size: 20px; font-weight: bold; color: #ffffff;\">Order Summary</td></tr>");
        mailerBody.append("                                    <tr style=\"background-color: #f9f9f9; text-align: left; font-size: 12px; color: #555555; font-weight: bold;\">");
        mailerBody.append("                                        <th style=\"padding: 10px; width: 5%;\">#</th>");
        mailerBody.append("                                        <th style=\"padding: 10px; width: 50%;\">Item Description</th>");
        mailerBody.append("                                        <th style=\"padding: 10px; width: 20%; text-align: right;\">Discount</th>");
        mailerBody.append("                                        <th style=\"padding: 10px; width: 25%; text-align: right;\">Total</th>");
        mailerBody.append("                                    </tr>");
        mailerBody.append("                                </thead>");
        mailerBody.append("                                <tbody>");

        // Loop through the product list and append a row for each product
        @SuppressWarnings("unchecked")
        List<Cart> productsList =  (List<Cart>) detailMap.get("productsList");
        int itemNumber = 1;

        double SubTotal = 0;
        double fullSubTotal = 0;
        double totalDiscount = 0;
        double deliveryFee = 500;

        for (Cart cart : productsList) {
            String title = cart.getProduct().getTitle();
            double price = cart.getStock().getPrice();
            int qty = cart.getQty();
            int discount = cart.getProduct().getDiscount();
            long displayPrice = Math.round((price * 100 / (100 - discount)));
            double discountPrice = (displayPrice - price) * qty;
            double totalP = price * qty;

            SubTotal += totalP;
            fullSubTotal += discountPrice;
            totalDiscount += discountPrice;


            mailerBody.append("<tr style=\"border-bottom: 1px solid #eeeeee;\">");
            mailerBody.append("    <td style=\"padding: 15px 10px; vertical-align: top; color: #555;\">").append(itemNumber++).append("</td>");
            mailerBody.append("    <td style=\"padding: 15px 10px; line-height: 1.5;\">");
            mailerBody.append("        <strong style=\"font-size: 15px; color: #333333;\">").append(title).append("</strong><br>");
            mailerBody.append("        <span style=\"background-color: #0096ed; color: #ffffff; padding: 2px 6px; font-size: 11px; border-radius: 3px; font-weight: bold; display: inline-block; margin-top: 5px;\">").append(discount).append("%</span>");
            mailerBody.append("        <span style=\"font-size: 13px; color: #777777; margin-left: 5px;\">");
            mailerBody.append("            <s style=\"color: #d9534f;\">LKR ").append(displayPrice).append("</s> LKR ").append(price).append(" x ").append(qty);
            mailerBody.append("        </span>");
            mailerBody.append("    </td>");
            mailerBody.append("    <td style=\"padding: 15px 10px; text-align: right; vertical-align: top; color: #555;\">LKR ").append(discountPrice).append("</td>");
            mailerBody.append("    <td style=\"padding: 15px 10px; text-align: right; vertical-align: top; font-weight: bold; color: #333;\">LKR ").append(totalP).append("</td>");
            mailerBody.append("</tr>");
        }

        double total = SubTotal+deliveryFee;

        mailerBody.append("                                </tbody>");
        mailerBody.append("                                <tfoot style=\"text-align: right; color: #333333; font-size: 14px;\">");
        mailerBody.append("                                    <tr>");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 10px 10px 5px 10px; text-align: right;\">Subtotal and Discount</td>");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 10px 10px 5px 10px; text-align: right;\">");
        mailerBody.append("                                            <strong style=\"font-size: 16px;\">LKR ").append(SubTotal).append("</strong><br>");
        mailerBody.append("                                            <s style=\"font-size: 12px; color: #777777;\">LKR ").append(fullSubTotal).append("</s>");
        mailerBody.append("                                        </td>");
        mailerBody.append("                                    </tr>");
        mailerBody.append("                                    <tr>");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 5px 10px; text-align: right;\">Site Discount</td>");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 5px 10px; text-align: right;\">- LKR ").append(totalDiscount).append("</td>");
        mailerBody.append("                                    </tr>");
        mailerBody.append("                                    <tr>");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 5px 10px; border-bottom: 1px solid #dddddd;\">Delivery Fee</td>");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 5px 10px; border-bottom: 1px solid #dddddd;\">LKR ").append(deliveryFee).append("</td>");
        mailerBody.append("                                    </tr>");
        mailerBody.append("                                    <tr style=\"font-weight: bold;\">");
        mailerBody.append("                                        <td colspan=\"2\" style=\"padding: 15px 10px; text-align: right; font-size: 16px;\">Total</td>");
        mailerBody.append("                                        <td colspan=\"2\" class=\"total-amount\" style=\"padding: 15px 10px; color: #0096ed; font-size: 22px;\">LKR ").append(total).append("</td>");
        mailerBody.append("                                    </tr>");
        mailerBody.append("                                    <tr style=\"background-color: #e8f5e9;\">");
        mailerBody.append("                                        <td colspan=\"4\" style=\"padding: 15px 10px; text-align: center; color: #d32f2f; font-weight: bold; font-size: 16px;\">");
        mailerBody.append("                                            Your Total Saving on This Order: LKR ").append(totalDiscount);
        mailerBody.append("                                        </td>");
        mailerBody.append("                                    </tr>");
        mailerBody.append("                                </tfoot>");
        mailerBody.append("                            </table>");
        mailerBody.append("                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-top: 30px;\">");
        mailerBody.append("                                <tr><td align=\"center\"><a href=\"#\" target=\"_blank\" style=\"background-color: #0096ed; color: #ffffff; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-size: 16px; font-weight: bold; display: inline-block;\">View Order Status</a></td></tr>");
        mailerBody.append("                            </table>");
        mailerBody.append("                        </td>");
        mailerBody.append("                    </tr>");
        mailerBody.append("                    <tr>");
        mailerBody.append("                        <td style=\"background-color: #333333; color: #cccccc; padding: 20px 30px; text-align: center;\">");
        mailerBody.append("                            <p style=\"margin: 0; font-size: 14px;\">Questions? Contact our <a href=\"mailto:support@techmart.com\" style=\"color: #b2d235; text-decoration: none;\">Support Team</a>.</p>");
        mailerBody.append("                            <p style=\"margin: 10px 0 0 0; font-size: 12px; line-height: 1.5;\">&copy; 2025 TechMart. All Rights Reserved.<br>123 Education Lane, Colombo, Sri Lanka</p>");
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
