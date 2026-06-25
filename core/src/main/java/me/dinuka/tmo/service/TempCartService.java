package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.entity.Cart;

import java.util.List;

@Remote
public interface TempCartService {
    String saveTempCart(List<Cart> userCartList);
}
