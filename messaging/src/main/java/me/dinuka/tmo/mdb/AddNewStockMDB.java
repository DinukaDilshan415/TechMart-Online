package me.dinuka.tmo.mdb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.entity.Warehouse;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/productTopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic")
})
public class AddNewStockMDB implements MessageListener {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String payload = ((TextMessage) message).getText();

                if (payload.startsWith("PROVISION_STOCK:")) {
                    String[] tokens = payload.split(":");

                    int productId = Integer.parseInt(tokens[1]);
                    int quantity = Integer.parseInt(tokens[2]);
                    int warehouseId = Integer.parseInt(tokens[3]);
                    double price = Double.parseDouble(tokens[4]);

                    Product managedProduct = em.find(Product.class, productId);
                    Warehouse warehouse = em.find(Warehouse.class, warehouseId);

                    if (managedProduct != null) {
                        Stock newStockRow = new Stock();
                        newStockRow.setProduct(managedProduct);
                        newStockRow.setQty(quantity);
                        newStockRow.setWarehouse(warehouse);
                        newStockRow.setPrice(price);

                        em.persist(newStockRow);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("MDB stock initialization parsing failure: " + e.getMessage());
        }
    }
}
