package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.entity.City;
import me.dinuka.tmo.entity.User;

@Remote
public interface AddressService {
    String saveNewAddress(String type, String fullName, String address, String pCode, String mobile, String cityId,User u1);
}
