package me.dinuka.tmo;

import jakarta.ejb.Stateless;
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

@Stateless
public class UserSessionBean implements UserService {
    @Override
    public String save(UserDTO userDTO) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                .setParameter("email", userDTO.getEmail())
                .getSingleResultOrNull();

        if (user != null) {
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

            Status status = session.get(Status.class,1);

            u.setStatus(status);

            session.persist(u);
            session.beginTransaction().commit();

            //send mail
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Mail.sendMail(userDTO.getEmail(), "TechMartOnline Account Verification", new MailTemplates().accountVerifyMail(verificationCode));
                }
            }).start();
            session.close();
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
