package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.InvoiceItemsSet;
import me.dinuka.tmo.entity.Invoice;
import me.dinuka.tmo.entity.InvoiceItems;
import me.dinuka.tmo.entity.OrderStatus;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.OrdersService;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrdersSessionBean implements OrdersService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    private static final int ORDER_PAID = 1;
    private static final int ORDER_PENDING = 2;
    private static final int ORDER_PROCESSING = 3;
    private static final int ORDER_SHIPPED = 4;
    private static final int ORDER_DELIVERED = 5;

    @Override
    public String loadUserOrders(User user) {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (user != null) {
            String invoiceJpql = "SELECT i FROM Invoice i WHERE i.user = :user";
            List<Invoice> invoiceList = em.createQuery(invoiceJpql, Invoice.class)
                    .setParameter("user", user)
                    .getResultList();

            ArrayList<InvoiceItemsSet> itemsSets = new ArrayList<>();

            if (!invoiceList.isEmpty()) {
                ResponseObject.add("invoiceList", gson.toJsonTree(invoiceList));

                String itemsJpql = "SELECT ii FROM InvoiceItems ii WHERE ii.invoice IN :invoices";
                List<InvoiceItems> allInvoiceItems = em.createQuery(itemsJpql, InvoiceItems.class)
                        .setParameter("invoices", invoiceList)
                        .getResultList();

                for (Invoice invoice : invoiceList) {
                    List<InvoiceItems> invoiceItemList = allInvoiceItems.stream()
                            .filter(ii -> ii.getInvoice().getId() == invoice.getId())
                            .toList();

                    int itemsCount = invoiceItemList.size();

                    InvoiceItemsSet iIS = new InvoiceItemsSet();
                    iIS.setOrderId(invoice.getOrder_id());
                    iIS.setItemsCount(itemsCount);
                    iIS.setInvoiceItemses(invoiceItemList);

                    itemsSets.add(iIS);
                }

                ResponseObject.add("invoiceItemsList", gson.toJsonTree(itemsSets));
                ResponseObject.addProperty("status", true);
            } else {
                ResponseObject.addProperty("message", "You Have No Orders");
            }
        } else {
            ResponseObject.addProperty("message", "Session Expired");
        }

        return gson.toJson(ResponseObject);
    }

    @Override
    public String updateOrderStatus(String orderId, String newStatus) {

        int statusId = 0;

        if (newStatus.equalsIgnoreCase("Paid")) {
            statusId = ORDER_PAID;
        } else if(newStatus.equalsIgnoreCase("Pending")){
            statusId = ORDER_PENDING;
        }else if(newStatus.equalsIgnoreCase("Processing")){
            statusId = ORDER_PROCESSING;
        }else if(newStatus.equalsIgnoreCase("Shipped")){
            statusId = ORDER_SHIPPED;
        }else if(newStatus.equalsIgnoreCase("Delivered")){
            statusId = ORDER_DELIVERED;
        }

        if (statusId != 0) {
            OrderStatus orderStatus = em.find(OrderStatus.class, statusId);

            if (orderStatus == null) {
                return "Invalid Status";
            }

            String jpql = "SELECT i FROM Invoice i WHERE i.order_id = :orderId";
            List<Invoice> invoiceList = em.createQuery(jpql, Invoice.class)
                    .setParameter("orderId", orderId)
                    .getResultList();

            if (!invoiceList.isEmpty()) {
                Invoice invoice = invoiceList.get(0);
                invoice.setOrderStatus(orderStatus);

                em.merge(invoice);

                return "success";
            } else {
                return "Something Wrong With Order ID";
            }
        } else {
            return "Invalid Status";
        }

    }
}
