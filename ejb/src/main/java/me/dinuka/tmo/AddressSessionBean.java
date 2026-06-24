package me.dinuka.tmo;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.entity.City;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.entity.UserAddress;
import me.dinuka.tmo.service.AddressService;

import java.util.List;

@Stateless
public class AddressSessionBean implements AddressService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public String saveNewAddress(String type, String fullName, String address, String pCode, String mobile, String cityId, User user) {

        System.out.println("City ID: " + cityId);

        String jpql = "SELECT u FROM User u WHERE u.email = :email";
        List<User> userList = em.createQuery(jpql, User.class)
                .setParameter("email", user.getEmail())
                .getResultList();

        if (!userList.isEmpty()) {

            User u1 = userList.get(0);

            City city = em.find(City.class, Integer.valueOf(cityId));

            UserAddress userAddress = new UserAddress(type, fullName, address, pCode, mobile, city, u1);

            em.persist(userAddress);

            return "success";
        } else {
            return "User Not Found";
        }
    }
}
