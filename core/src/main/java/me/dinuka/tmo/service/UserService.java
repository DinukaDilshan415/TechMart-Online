package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.dto.UserDTO;

@Remote
public interface UserService {
    String save(UserDTO userDTO);
    void update(UserDTO userDTO);
    void delete(int id);
    UserDTO getByEmail(String email);
}
