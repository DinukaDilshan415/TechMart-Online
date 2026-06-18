package me.dinuka.tmo;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.UserDTO;
import me.dinuka.tmo.entity.Status;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.UserService;
import me.dinuka.tmo.util.HibernateUtil;
import me.dinuka.tmo.util.Util;
import me.dinuka.tmo.util.mailer.Mail;
import me.dinuka.tmo.util.mailer.MailTemplates;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

@Stateless
public class UserSessionBean implements UserService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager session;

    @Override
    public String save(UserDTO userDTO) {

        List<User> users = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                .setParameter("email", userDTO.getEmail()).getResultList();

        if (!users.isEmpty()) {
            return "email-exists";
        } else {

            // insert
            final String verificationCode = Util.generateCode();

            User u = new User();
            u.setUsername(userDTO.getUsername());
            u.setEmail(userDTO.getEmail());
            u.setMobile(userDTO.getMobile());
            u.setPassword(userDTO.getPassword());
            u.setVerification_code(verificationCode);
            u.setCreated_at(new Date());

            Status status = session.getReference(Status.class, 1);

            u.setStatus(status);

            session.persist(u);

            //send mail
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Mail.sendMail(userDTO.getEmail(), "TechMartOnline Account Verification", new MailTemplates().accountVerifyMail(verificationCode));
                }
            }).start();
            return "success";
        }
    }

    @Override
    public void update(UserDTO userDTO) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public UserDTO getByEmail(String email) {
        return null;
    }
}
