package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.dto.UserDTO;
import me.dinuka.tmo.entity.User;

@Remote
public interface UserService {
    String save(UserDTO userDTO);
    String update(User user);
    void delete(int id);
    UserDTO getByEmail(String email);
    String reSendVerification(User user);
}
