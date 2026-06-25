package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.entity.*;
import me.dinuka.tmo.service.CheckoutService;
import me.dinuka.tmo.util.PayHere;
import me.dinuka.tmo.util.RegexValidator;
import me.dinuka.tmo.util.Util;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Stateless
public class GetPaymentDetailsBean implements CheckoutService {

    private static final int ORDER_PAID = 1;
    private static final int RATING_DEFAULT_VALUE = 2;
    private static final double DELIVERY_FEE = 500;

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public String getDeatilsForPayment(User user, Map<String, Object> requestData) {
        Gson gson = new Gson();

        JsonObject reqJsonObejct = gson.toJsonTree(requestData).getAsJsonObject();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Boolean isGift = reqJsonObejct.get("isGift").getAsBoolean();

        JsonObject deliveryAddress = reqJsonObejct.getAsJsonObject("deliveryAddress");
        String daFullName = deliveryAddress.get("fullName").getAsString();
        String daAddress = deliveryAddress.get("addressLine1").getAsString();
        String daAddressType = deliveryAddress.get("addressType").getAsString();
        String daMobile = deliveryAddress.get("phone").getAsString();
        String daPostalCode = deliveryAddress.get("postalCode").getAsString();
        String daCityId = deliveryAddress.get("city").getAsString();

        boolean doPayment = false;
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
            if (isGift) {
                JsonObject giftAddress = reqJsonObejct.getAsJsonObject("giftAddress");
                String gaFullName = giftAddress.get("fullName").getAsString();
                String gaAddress = giftAddress.get("addressLine1").getAsString();
                String gaCityId = giftAddress.get("city").getAsString();
                String gaPostalCode = giftAddress.get("postalCode").getAsString();
                String gaNote = giftAddress.get("note").getAsString();

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
                    doPayment = true;
                }
            } else {
                doPayment = true;
            }

            if (doPayment) {
                int orderId = 1;
                try {
                    if (user != null) {
                        City city = em.find(City.class, Double.valueOf(daCityId).intValue());

                        String invoiceJpql = "SELECT i FROM Invoice i ORDER BY i.id DESC";
                        List<Invoice> invoiceList = em.createQuery(invoiceJpql, Invoice.class)
                                .setMaxResults(1)
                                .getResultList();

                        if (!invoiceList.isEmpty()) {
                            Invoice invoice = invoiceList.get(0);
                            orderId = invoice.getId() + 1;
                        }

                        String cartJpql = "SELECT c FROM Cart c WHERE c.user = :user";
                        List<Cart> cartList = em.createQuery(cartJpql, Cart.class)
                                .setParameter("user", user)
                                .getResultList();

                        double amount = 0;
                        String items = "";

                        for (Cart cart : cartList) {
                            amount += cart.getQty() * cart.getStock().getPrice();
                            items += cart.getProduct().getTitle() + "  x  " + cart.getQty() + ", ";
                        }
                        amount += DELIVERY_FEE;

                        //PayHere process
                        String merahantID = "1223992";
                        String merchantSecret = "MTY0MDEzMTcwMDEwMzU2OTgxOTQ4ODk4OTcyMTIzNTIxOTI1MzIz";
                        String orderID = "#000" + orderId;
                        String currency = "LKR";
                        String formattedAmount = new DecimalFormat("0.00").format(amount);
                        String merchantSecretMD5 = PayHere.generateMD5(merchantSecret);

                        String hash = PayHere.generateMD5(merahantID + orderID + formattedAmount + currency + merchantSecretMD5);

                        JsonObject payHereJson = new JsonObject();
                        payHereJson.addProperty("sandbox", true);
                        payHereJson.addProperty("merchant_id", merahantID);

                        payHereJson.addProperty("return_url", "http://localhost:5173/");
                        payHereJson.addProperty("cancel_url", "http://localhost:5173/");
                        payHereJson.addProperty("notify_url", Util.LOCAL_BASE_URL+"/techmart/VerifyPayments");

                        payHereJson.addProperty("order_id", orderID);
                        payHereJson.addProperty("items", items);
                        payHereJson.addProperty("amount", formattedAmount);
                        payHereJson.addProperty("currency", currency);
                        payHereJson.addProperty("hash", hash);

                        payHereJson.addProperty("first_name", user.getUsername());
                        payHereJson.addProperty("last_name", "");
                        payHereJson.addProperty("email", user.getEmail());

                        payHereJson.addProperty("phone", daMobile);
                        payHereJson.addProperty("address", daAddress);
                        payHereJson.addProperty("city", city.getName());
                        payHereJson.addProperty("country", "Sri Lanka");

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Checkout completed");
                        responseObject.add("payhereJson", new Gson().toJsonTree(payHereJson));
                    } else {
                        responseObject.addProperty("message", "User Not Found");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return gson.toJson(responseObject);
    }

    @Override
    public String buyProductNow(List<Cart> cartList, String productId, String qty, User user) {

        String deleteJpql = "DELETE FROM Cart c WHERE c.user = :user";
        em.createQuery(deleteJpql)
                .setParameter("user", user)
                .executeUpdate();

        Product product = em.find(Product.class, Integer.valueOf(productId));

        String stockJpql = "SELECT s FROM Stock s WHERE s.product = :product";
        List<Stock> stockResults = em.createQuery(stockJpql, Stock.class)
                .setParameter("product", product)
                .getResultList();
        Stock pStock = stockResults.isEmpty() ? null : stockResults.get(0);

        if (pStock != null && Integer.parseInt(qty) <= pStock.getQty()) { // Product qty available
            Cart cart = new Cart();
            cart.setQty(Integer.parseInt(qty));
            cart.setProduct(product);
            cart.setUser(user);
            cart.setStock(pStock);

            em.persist(cart);

            return "success";
        } else {
            return "Insuficient Product Quantity";
        }
    }
}
