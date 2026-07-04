
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Topic;
import jakarta.persistence.EntityManager;

import me.dinuka.tmo.ProductSessionBean;
import me.dinuka.tmo.dto.ProductDTO;
import me.dinuka.tmo.entity.Brand;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Status;
import me.dinuka.tmo.entity.SubCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSessionBeanTest {

    @InjectMocks
    private ProductSessionBean productBean;

    @Mock
    private EntityManager entityManager;

    @Mock
    private JMSContext jmsContext;

    @Mock
    private JMSProducer producer;

    @Mock
    private Topic topic;

    @BeforeEach
    void setUp() throws Exception {

        Field field = ProductSessionBean.class.getDeclaredField("baseUploadDir");
        field.setAccessible(true);
        field.set(productBean, System.getProperty("java.io.tmpdir"));
    }

    @Test
    void shouldAddProductSuccessfully() {

        ProductDTO dto = new ProductDTO();
        dto.setTitle("iPhone 16");
        dto.setDescription("Apple Smartphone");
        dto.setBrand_id("1");
        dto.setSub_category_id("2");
        dto.setDiscount("10");
        dto.setQty("5");
        dto.setPrice("250000");
        dto.setWare_house_id("1");
        dto.setImage(null);

        Brand brand = new Brand();
        SubCategory sub = new SubCategory();
        Status status = new Status();

        when(entityManager.find(SubCategory.class, 2)).thenReturn(sub);
        when(entityManager.find(Brand.class, 1)).thenReturn(brand);
        when(entityManager.find(Status.class, 1)).thenReturn(status);

        when(jmsContext.createProducer()).thenReturn(producer);
        when(producer.setDeliveryMode(anyInt())).thenReturn(producer);

        String result = productBean.addProduct(dto);

        assertEquals("success", result);

        verify(entityManager).persist(any(Product.class));
        verify(entityManager).flush();
        verify(producer).send(eq(topic), anyString());
    }

    @Test
    void shouldPersistProduct() {

        ProductDTO dto = new ProductDTO();

        dto.setTitle("Laptop");
        dto.setBrand_id("1");
        dto.setSub_category_id("1");
        dto.setDiscount("0");
        dto.setQty("2");
        dto.setPrice("150000");

        when(entityManager.find(SubCategory.class, 1)).thenReturn(new SubCategory());
        when(entityManager.find(Brand.class, 1)).thenReturn(new Brand());
        when(entityManager.find(Status.class, 1)).thenReturn(new Status());

        when(jmsContext.createProducer()).thenReturn(producer);
        when(producer.setDeliveryMode(anyInt())).thenReturn(producer);

        productBean.addProduct(dto);

        verify(entityManager, times(1))
                .persist(any(Product.class));
    }

    @Test
    void shouldSetCorrectProductValues() {

        ProductDTO dto = new ProductDTO();

        dto.setTitle("Keyboard");
        dto.setDescription("Gaming Keyboard");
        dto.setBrand_id("1");
        dto.setSub_category_id("2");
        dto.setDiscount("5");
        dto.setQty("5");
        dto.setPrice("12000");

        when(entityManager.find(SubCategory.class, 2)).thenReturn(new SubCategory());
        when(entityManager.find(Brand.class, 1)).thenReturn(new Brand());
        when(entityManager.find(Status.class, 1)).thenReturn(new Status());


        when(jmsContext.createProducer()).thenReturn(producer);
        when(producer.setDeliveryMode(anyInt())).thenReturn(producer);

        productBean.addProduct(dto);

        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(Product.class);

        verify(entityManager).persist(captor.capture());

        Product product = captor.getValue();

        assertEquals("Keyboard", product.getTitle());
        assertEquals("Gaming Keyboard", product.getDescription());
        assertEquals(5, product.getDiscount());
    }


}