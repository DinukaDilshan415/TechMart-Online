package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.dto.TestDTO;

@Remote
public interface TestService {
    TestDTO getById(int id);
    void save(TestDTO testDTO);
}
