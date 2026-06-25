package me.dinuka.tmo;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import me.dinuka.tmo.service.EmailService;
import me.dinuka.tmo.util.mailer.Mail;
import me.dinuka.tmo.util.mailer.MailTemplates;

import java.util.HashMap;

@Stateless
public class EmailSessionBean implements EmailService {

    @Asynchronous
    @Override
    public void sendOrderEmail(HashMap<String, Object> orderDetails, String email) {

        System.out.println("Email sent to: " + email);
        Mail.sendMail(email, "Your TechMart-Online order confirmation", new MailTemplates().generateOrderConfirmationEmail(orderDetails));
    }
}
