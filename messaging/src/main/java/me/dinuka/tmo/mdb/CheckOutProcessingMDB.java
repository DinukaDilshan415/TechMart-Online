package me.dinuka.tmo.mdb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.CheckoutPayloadDTO;
import me.dinuka.tmo.entity.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/checkoutQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class CheckOutProcessingMDB implements MessageListener {

    private static final int ORDER_PAID = 1;
    private static final int RATING_DEFAULT_VALUE = 2;
    private static final double DELIVERY_FEE = 500;

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objMessage = (ObjectMessage) message;

                CheckoutPayloadDTO dto = (CheckoutPayloadDTO) objMessage.getObject();

                UserAddress userAddress = dto.getUserAddress();
                GiftAddress giftAddress = dto.getGiftAddress();
                User user = dto.getUser();

                int orderId = 1;

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
                    items += cart.getProduct().getTitle() + " x " + cart.getQty() + ",";
                }
                amount += DELIVERY_FEE;

                OrderStatus orderStatus = em.find(OrderStatus.class, ORDER_PAID);

                Invoice invoice = new Invoice();
                invoice.setOrder_id("#000" + orderId);
                invoice.setCreated_at(new Date());
                invoice.setAmount(amount);
                invoice.setOrderStatus(orderStatus);
                invoice.setUser(user);
                invoice.setUserAddress(userAddress);
                invoice.setGiftAddress(giftAddress);

                em.persist(invoice);

                Ratings ratings = em.find(Ratings.class, RATING_DEFAULT_VALUE);

                for (Cart cart : cartList) {
                    InvoiceItems invoiceItems = new InvoiceItems();

                    Stock stock = cart.getStock();

                    invoiceItems.setInvoice(invoice);
                    invoiceItems.setQty(cart.getQty());
                    invoiceItems.setStock(stock);
                    invoiceItems.setRatings(ratings);

                    em.persist(invoiceItems);

                    //update product qty
                    stock.setQty(stock.getQty() - cart.getQty());
                    em.merge(stock);

                    // delete cart item
                    em.remove(cart);
                }

                try {
                    String giftName = "...";
                    String giftAdd = "...";
                    String giftCity = "...";
                    String giftPcode = "...";
                    String giftNote = "...";

                    if (giftAddress != null) {
                        giftName = giftAddress.getFull_name();
                        giftAdd = giftAddress.getAddress();
                        giftCity = giftAddress.getCity().getName();
                        giftPcode = giftAddress.getPostal_code();
                        giftNote = giftAddress.getNote();
                    }

                    HashMap detailMap = new HashMap();
                    detailMap.put("OrderId", orderId);
                    detailMap.put("placedOn", new SimpleDateFormat("MMM dd, yyyy, h:mm:ss a").format(new Date()));
                    detailMap.put("billName", userAddress.getFull_name());
                    detailMap.put("billAddress", userAddress.getAddress());
                    detailMap.put("billCity", userAddress.getCity().getName());
                    detailMap.put("billPcode", userAddress.getPostal_code());
                    detailMap.put("billNumber", userAddress.getMobile());

                    detailMap.put("giftName", giftName);
                    detailMap.put("giftAddress", giftAdd);
                    detailMap.put("giftCity", giftCity);
                    detailMap.put("giftPcode", giftPcode);
                    detailMap.put("giftNote", giftNote);

                    detailMap.put("productsList", cartList);

                    //send mail
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Mail.sendMail(user.getEmail(), "Your TechMart-Online order confirmation", new MailTemplates().generateOrderConfirmationEmail(detailMap));
//                        }
//                    }).start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
