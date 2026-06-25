package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.entity.User;

@Remote
public interface OrdersService {
    String loadUserOrders(User user);
}
