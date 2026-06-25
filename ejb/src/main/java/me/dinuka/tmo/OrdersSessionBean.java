package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.InvoiceItemsSet;
import me.dinuka.tmo.entity.Invoice;
import me.dinuka.tmo.entity.InvoiceItems;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.OrdersService;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrdersSessionBean implements OrdersService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

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
}
