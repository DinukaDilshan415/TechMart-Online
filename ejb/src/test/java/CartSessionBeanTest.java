
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import me.dinuka.tmo.CartSessionBean;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartSessionBeanTest {

    @InjectMocks
    private CartSessionBean cartSessionBean;

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Stock> stockQuery;

    @Mock
    private TypedQuery<Cart> cartQuery;

    private Product product;
    private Stock stock;
    private User user;

    @BeforeEach
    void setUp() throws Exception {

        product = new Product();
        product.setId(1);

        stock = new Stock();
        stock.setId(1);
        stock.setProduct(product);
        stock.setQty(20);

        user = new User();
        user.setId(1);

        Field field = CartSessionBean.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(cartSessionBean, em);
    }

    @Test
    void shouldAddProductToCartSuccessfully() {

        when(em.find(Product.class, 1)).thenReturn(product);

        when(em.createQuery(anyString(), eq(Stock.class)))
                .thenReturn(stockQuery);

        when(stockQuery.setParameter(eq("product"), eq(product)))
                .thenReturn(stockQuery);

        when(stockQuery.getResultList())
                .thenReturn(List.of(stock));

        when(em.createQuery(contains("SELECT c FROM Cart"), eq(Cart.class)))
                .thenReturn(cartQuery);

        when(cartQuery.setParameter(eq("user"), eq(user)))
                .thenReturn(cartQuery);

        when(cartQuery.setParameter(eq("product"), eq(product)))
                .thenReturn(cartQuery);

        when(cartQuery.getResultList())
                .thenReturn(Collections.emptyList());

        String result = cartSessionBean.addProductToCart("1", "5", user);

        assertEquals("success", result);

        verify(em, times(1)).persist(any(Cart.class));
    }

    @Test
    void shouldUpdateCartQtySuccessfully() {

        Cart cart = new Cart();
        cart.setQty(2);
        cart.setProduct(product);

        when(em.find(Product.class, 1)).thenReturn(product);

        when(em.createQuery(anyString(), eq(Stock.class)))
                .thenReturn(stockQuery);

        when(stockQuery.setParameter(eq("product"), eq(product)))
                .thenReturn(stockQuery);

        when(stockQuery.getResultList())
                .thenReturn(List.of(stock));

        when(em.createQuery(contains("SELECT c FROM Cart"), eq(Cart.class)))
                .thenReturn(cartQuery);

        when(cartQuery.setParameter(eq("user"), eq(user)))
                .thenReturn(cartQuery);

        when(cartQuery.setParameter(eq("product"), eq(product)))
                .thenReturn(cartQuery);

        when(cartQuery.getResultList())
                .thenReturn(List.of(cart));

        String result = cartSessionBean.updateCartQtyFromCart("1", "10", user);

        assertEquals("success", result);
        assertEquals(10, cart.getQty());

        verify(em).merge(cart);
    }
}