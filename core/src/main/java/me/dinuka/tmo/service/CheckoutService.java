package me.dinuka.tmo.service;

import com.google.gson.JsonObject;
import jakarta.ejb.Remote;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;

import java.util.List;
import java.util.Map;

@Remote
public interface CheckoutService {
    String getDeatilsForPayment(User user, Map<String, Object> requestData);

    String buyProductNow(List<Cart> cartList, String productId, String qty,User user);
}
