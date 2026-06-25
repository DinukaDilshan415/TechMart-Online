package me.dinuka.tmo.service;

import jakarta.ejb.Remote;

import java.util.HashMap;

@Remote
public interface EmailService {
    void sendOrderEmail(HashMap<String, Object> orderDetails, String email);
}
