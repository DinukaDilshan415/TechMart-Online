package me.dinuka.tmo;

import jakarta.ejb.Stateless;
import me.dinuka.tmo.dto.TestDTO;
import me.dinuka.tmo.service.TestService;

@Stateless
public class TestSessionBean implements TestService {
    @Override
    public TestDTO getById(int id) {
        System.out.println("DTO Retrieved: "+" "+id);
        return null;
    }

    @Override
    public void save(TestDTO testDTO) {
        System.out.println("DTO Saved: "+" "+testDTO.getName()+" "+testDTO.getEmail());
    }
}
