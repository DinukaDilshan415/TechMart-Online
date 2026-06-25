package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.entity.User;

import java.util.Map;

@Remote
public interface CheckoutProcessingService {
    String processCheckout(User user, Map<String, Object> requestData);
}
