package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.CheckoutPayloadDTO;
import me.dinuka.tmo.entity.City;
import me.dinuka.tmo.entity.GiftAddress;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.entity.UserAddress;
import me.dinuka.tmo.service.CheckoutProcessingService;
import me.dinuka.tmo.util.RegexValidator;
import me.dinuka.tmo.util.Util;

import java.util.List;
import java.util.Map;

@Stateless
public class ChechOutProcessingBean implements CheckoutProcessingService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Resource(lookup = "jms/checkoutProccesingConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/checkoutQueue")
    private Queue checkoutQueue;
    
    @Override
    public String processCheckout(User user, Map<String, Object> requestData) {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        JsonObject reqJsonObejct = gson.toJsonTree(requestData).getAsJsonObject();

        Boolean isGift = reqJsonObejct.get("isGift").getAsBoolean();

        JsonObject deliveryAddress = reqJsonObejct.getAsJsonObject("deliveryAddress");
        String daFullName = deliveryAddress.get("fullName").getAsString();
        String daAddress = deliveryAddress.get("addressLine1").getAsString();
        String daAddressType = deliveryAddress.get("addressType").getAsString();
        String daMobile = deliveryAddress.get("phone").getAsString();
        String daPostalCode = deliveryAddress.get("postalCode").getAsString();
        String daCityId = deliveryAddress.get("city").getAsString();

        if (user == null) {
            responseObject.addProperty("message", "Session Expired !");
        } else {
            if (daAddressType.isEmpty()) {
                responseObject.addProperty("message", "Please Enter Some Type Like (Home / Work)");
            } else if (daMobile.isEmpty()) {
                responseObject.addProperty("message", "Enter Mobile Number");
            } else if (!RegexValidator.isValidSlPhone(daMobile)) {
                responseObject.addProperty("message", "Enter Valid Mobile Number");
            } else if (daFullName.isEmpty()) {
                responseObject.addProperty("message", "Please Enter Full Name For Address");
            } else if (daAddress.isEmpty()) {
                responseObject.addProperty("message", "Enter Full Address");
            } else if (daCityId.equals("0")) {
                responseObject.addProperty("message", "Please select city");
            } else if (daPostalCode.isEmpty()) {
                responseObject.addProperty("message", "Postalcode can not be empty");
            } else if (!Util.isCodeValid(daPostalCode)) {
                responseObject.addProperty("message", "Enter Valid Postal Code");
            } else {

                City city = em.find(City.class, Double.valueOf(daCityId).intValue());

                GiftAddress ga = null;
                if (isGift) {
                    JsonObject giftAddress = reqJsonObejct.getAsJsonObject("giftAddress");
                    String gaFullName = giftAddress.get("fullName").getAsString();
                    String gaAddress = giftAddress.get("addressLine1").getAsString();
                    String gaCityId = giftAddress.get("city").getAsString();
                    String gaPostalCode = giftAddress.get("postalCode").getAsString();
                    String gaNote = giftAddress.get("note").getAsString();

                    City city2 = em.find(City.class, Double.valueOf(gaCityId).intValue());

                    if (gaFullName.isEmpty()) {
                        responseObject.addProperty("message", "Please Enter Recipient's Full Name");
                    } else if (gaAddress.isEmpty()) {
                        responseObject.addProperty("message", "Enter Recipient's Full Address");
                    } else if (gaCityId.equals("0")) {
                        responseObject.addProperty("message", "Please select Recipient's city");
                    } else if (gaPostalCode.isEmpty()) {
                        responseObject.addProperty("message", "Recipient's Postalcode can not be empty");
                    } else if (!Util.isCodeValid(gaPostalCode)) {
                        responseObject.addProperty("message", "Enter Valid Postal Code");
                    } else if (gaNote.isEmpty()) {
                        responseObject.addProperty("message", "Enter Something In Note");
                    } else {
                        ga = new GiftAddress(gaFullName, gaAddress, gaNote, gaPostalCode, city2);
                        em.persist(ga);
                    }
                }

                String jpql = "SELECT ua FROM UserAddress ua WHERE ua.type = :type " +
                        "AND ua.full_name = :fullName " +
                        "AND ua.address = :address " +
                        "AND ua.mobile = :mobile " +
                        "AND ua.postal_code = :postalCode";

                List<UserAddress> addressList = em.createQuery(jpql, UserAddress.class)
                        .setParameter("type", daAddressType)
                        .setParameter("fullName", daFullName)
                        .setParameter("address", daAddress)
                        .setParameter("mobile", daMobile)
                        .setParameter("postalCode", daPostalCode)
                        .getResultList();


                if (addressList.isEmpty()) { //add address to db
                    UserAddress userAddress = new UserAddress(daAddressType, daFullName, daAddress, daPostalCode, daMobile, city, user);
                    em.persist(userAddress);
                    updateDatabase(userAddress, ga, user);
                } else { //get exist address
                    UserAddress userAddress = (UserAddress) addressList.get(0);
                    updateDatabase(userAddress, ga, user);
                }
                
                responseObject.addProperty("message", "Checkout completed");
                responseObject.addProperty("status", true);
            }
        }

        return gson.toJson(responseObject);
    }

    @Asynchronous
    private void updateDatabase(UserAddress userAddress, GiftAddress giftAddress, User user) {

        try (JMSContext context = connectionFactory.createContext()) {

            CheckoutPayloadDTO tokenPayload = new CheckoutPayloadDTO(userAddress, giftAddress, user);

            context.createProducer().send(checkoutQueue, tokenPayload);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
