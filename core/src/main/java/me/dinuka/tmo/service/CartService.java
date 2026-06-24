package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;

import java.util.List;

@Remote
public interface CartService {
    String addProductToCart(String productId, String quantity, User user);
    String addProductToSessionCart(String productId, String quantity);
    String addSessionCartToDatabase(List<Cart> sessionList, User user);
    List<Cart> getSessionCart();
    String removeFromSessionCart(String id, User user);
    String updateCartQtyFromCart(String id, String quantity, User user);
}
