package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.InvoiceItemsSet;
import me.dinuka.tmo.entity.*;
import me.dinuka.tmo.service.AdminDashboardService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class AdminDashboardBean implements AdminDashboardService {

    private static final int ACTIVE = 1;

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public String getDashboardData(Admin admin) {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (admin != null) {
            ArrayList<Object> dashboardData = new ArrayList();

            Status activeStatus = em.find(Status.class, ACTIVE);

            String revenueJpql = "SELECT SUM(i.amount) FROM Invoice i";
            Double totalRevenue = em.createQuery(revenueJpql, Double.class).getSingleResult();
            if (totalRevenue == null) totalRevenue = 0.0;

            String orderCountJpql = "SELECT COUNT(ii) FROM InvoiceItems ii";
            int Orders = em.createQuery(orderCountJpql, Long.class).getSingleResult().intValue();

            String customerCountJpql = "SELECT COUNT(u) FROM User u";
            int Customers = em.createQuery(customerCountJpql, Long.class).getSingleResult().intValue();

            String activeProductCountJpql = "SELECT COUNT(p) FROM Product p WHERE p.status = :status";
            int Products = em.createQuery(activeProductCountJpql, Long.class)
                    .setParameter("status", activeStatus)
                    .getSingleResult().intValue();

            String topSellingJpql = "SELECT ii.stock, SUM(ii.qty) as totalQty FROM InvoiceItems ii " +
                    "GROUP BY ii.stock " +
                    "ORDER BY SUM(ii.qty) DESC";
            List<Object[]> results = em.createQuery(topSellingJpql, Object[].class)
                    .setMaxResults(5)
                    .getResultList();

            String lowStockJpql = "SELECT s FROM Stock s WHERE s.qty <= 10 ORDER BY s.qty ASC";
            List<Stock> lowStockList = em.createQuery(lowStockJpql, Stock.class)
                    .setMaxResults(5)
                    .getResultList();

            String latestOrdersJpql = "SELECT i FROM Invoice i ORDER BY i.id DESC";
            List<Invoice> invoiceList = em.createQuery(latestOrdersJpql, Invoice.class)
                    .setMaxResults(4)
                    .getResultList();


            Map<String, Object> sumData = new HashMap<>();
            sumData.put("Total Revenue", totalRevenue);
            sumData.put("Orders", Orders);
            sumData.put("Customers", Customers);
            sumData.put("Active Products", Products);

            dashboardData.add(sumData);

            ArrayList<Object> topSellings = new ArrayList();

            for (Object[] row : results) {
                Stock stock = (Stock) row[0];   // full stock object
                double totalQty = (double) row[1]; // sum result
                double itemRevenue = stock.getPrice() * totalQty; // sum result

                Map<String, Object> topProductMap = new HashMap<>();
                topProductMap.put("totalQty", totalQty);
                topProductMap.put("itemRevenue", itemRevenue);
                topProductMap.put("stock", stock);

                topSellings.add(topProductMap);
            }

            dashboardData.add(topSellings);

            dashboardData.add(lowStockList);

            dashboardData.add(invoiceList);

            responseObject.add("dashboardData", gson.toJsonTree(dashboardData));
            responseObject.addProperty("status", true);
        } else {
            responseObject.addProperty("message", "Admin Session Expired");
        }

        return gson.toJson(responseObject);
    }

    @Override
    public String getOrdersData(Admin admin) {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();

        if (admin != null) {
            String invoiceJpql = "SELECT i FROM Invoice i ORDER BY i.id DESC";
            List<Invoice> invoiceList = em.createQuery(invoiceJpql, Invoice.class)
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
            } else {
                ResponseObject.addProperty("message", "No Orders");
            }
        } else {
            ResponseObject.addProperty("message", "Session Expired");
        }

        return gson.toJson(ResponseObject);
    }

    @Override
    public String getCustomersData(Admin admin) {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();

        if (admin != null) {
            String jpql = "SELECT u, COUNT(i), COALESCE(SUM(i.amount), 0.0) " +
                    "FROM User u " +
                    "LEFT JOIN Invoice i ON i.user = u " +
                    "GROUP BY u";

            List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

            ArrayList<HashMap<String, Object>> userArrayList = new ArrayList<>();

            for (Object[] row : results) {
                User user = (User) row[0];
                Long orderCount = (Long) row[1];
                Double spent = (Double) row[2];

                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("user", user);
                userMap.put("orderCount", orderCount.intValue());
                userMap.put("spent", spent);

                userArrayList.add(userMap);
            }

            if (!userArrayList.isEmpty()) {
                ResponseObject.add("customerList", gson.toJsonTree(userArrayList));
            }

        } else {
            ResponseObject.addProperty("message", "Session Expired");
        }

        return gson.toJson(ResponseObject);
    }

    @Override
    public String getProductsData(Admin admin) {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (admin != null) {
            String productJpql = "SELECT p FROM Product p";
            List<Product> productList = em.createQuery(productJpql, Product.class)
                    .getResultList();

            if (!productList.isEmpty()) {
                String stockJpql = "SELECT s FROM Stock s WHERE s.product IN :products";
                List<Stock> stockList = em.createQuery(stockJpql, Stock.class)
                        .setParameter("products", productList)
                        .getResultList();

                responseObject.add("productList", gson.toJsonTree(productList));
                responseObject.add("stockList", gson.toJsonTree(stockList));
                responseObject.addProperty("status", true);
            }

        } else {
            responseObject.addProperty("message", "Admin Session Expired");
        }

        DbConnectionTracker.connectionReleased();
        return gson.toJson(responseObject);
    }

    @Override
    public String getUsersData(Admin admin) {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (admin != null) {
            String jpql = "SELECT a FROM Admin a";
            List<Admin> adminList = em.createQuery(jpql, Admin.class)
                    .getResultList();

            if (!adminList.isEmpty()) {
                ResponseObject.addProperty("status", true);
                ResponseObject.add("adminList", gson.toJsonTree(adminList));
            }

        } else {
            ResponseObject.addProperty("message", "Session Expired");
        }

        return gson.toJson(ResponseObject);
    }
}
