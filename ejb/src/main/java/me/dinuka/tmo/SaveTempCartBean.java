package me.dinuka.tmo;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.service.TempCartService;

import java.util.List;

@Stateless
public class SaveTempCartBean implements TempCartService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public String saveTempCart(List<Cart> userCartList) {
        for (Cart cart : userCartList) {
            em.persist(cart);
        }

        return "success";
    }
}

