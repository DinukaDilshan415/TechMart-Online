package me.dinuka.tmo;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.ProductDTO;
import me.dinuka.tmo.entity.Brand;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Status;
import me.dinuka.tmo.entity.SubCategory;
import me.dinuka.tmo.service.ProductService;
import me.dinuka.tmo.util.monitor.JmsMetrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Singleton
public class ProductSessionBean implements ProductService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "jms/productTopic")
    private Topic productTopic;

    @Resource(lookup = "storage/uploadDir")
    private String baseUploadDir;

    @Override
    public String addProduct(ProductDTO productDTO) {
        final int PENDING_STATUS_ID = 3;
        final int ACTIVE_STATUS_ID = 1;

        SubCategory subCategory = em.find(SubCategory.class, Integer.valueOf(productDTO.getSub_category_id()));
        Brand brand = em.find(Brand.class, Integer.valueOf(productDTO.getBrand_id()));
        Status status = em.find(Status.class, ACTIVE_STATUS_ID);

        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setDiscount(Integer.valueOf(productDTO.getDiscount()));
        product.setCreated_at(new Date());
        product.setStatus(status);

        em.persist(product);
        em.flush();

        String messagePayload = "PROVISION_STOCK:" + product.getId()
                + ":" + productDTO.getQty()
                + ":" + productDTO.getWare_house_id()
                + ":" + productDTO.getPrice();

        try {
            jmsContext.createProducer()
                    .setDeliveryMode(jakarta.jms.DeliveryMode.NON_PERSISTENT)
                    .send(productTopic, messagePayload);

            JmsMetrics.messageSent();
        } catch (Exception e) {
            JmsMetrics.messageFailed();
            throw new RuntimeException(e);
        }
        byte[] image = productDTO.getImage();

        if (image != null && image.length > 0) {
            try {
                Path targetDir = Paths.get(baseUploadDir, String.valueOf(product.getId()));

                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }

                Path targetFile = targetDir.resolve("image1.png");

                Files.write(targetFile, image);

            } catch (IOException e) {
                throw new RuntimeException("EJB file storage operation failed", e);
            }
        }

        return "success";
    }

    @Override
    public String updateProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    public String deleteProduct(int id) {
        return null;
    }
}
