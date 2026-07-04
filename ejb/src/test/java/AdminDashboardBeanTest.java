
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import me.dinuka.tmo.AdminDashboardBean;
import me.dinuka.tmo.entity.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDashboardBeanTest {

    @InjectMocks
    private AdminDashboardBean adminDashboardBean;

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Long> longQuery;

    @Mock
    private TypedQuery<Double> doubleQuery;

    @Mock
    private TypedQuery<Object[]> objectQuery;

    @Mock
    private TypedQuery<Stock> stockQuery;

    @Mock
    private TypedQuery<Invoice> invoiceQuery;

    private Admin admin;
    private Status status;

    @BeforeEach
    void setUp() throws Exception {

        admin = new Admin();
        admin.setId(1);

        status = new Status();
        status.setId(1);

        Field field = AdminDashboardBean.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(adminDashboardBean, em);
    }

    @Test
    void shouldReturnDashboardDataSuccessfully() {

        when(em.find(Status.class, 1)).thenReturn(status);

        when(em.createQuery(contains("SUM(i.amount)"), eq(Double.class)))
                .thenReturn(doubleQuery);

        when(doubleQuery.getSingleResult()).thenReturn(250000.0);

        when(em.createQuery(contains("COUNT(ii)"), eq(Long.class)))
                .thenReturn(longQuery);

        when(longQuery.getSingleResult()).thenReturn(15L);

        TypedQuery<Long> customerQuery = mock(TypedQuery.class);
        when(em.createQuery(contains("COUNT(u)"), eq(Long.class)))
                .thenReturn(customerQuery);
        when(customerQuery.getSingleResult()).thenReturn(8L);

        TypedQuery<Long> productQuery = mock(TypedQuery.class);
        when(em.createQuery(contains("COUNT(p)"), eq(Long.class)))
                .thenReturn(productQuery);

        when(productQuery.setParameter(eq("status"), eq(status)))
                .thenReturn(productQuery);

        when(productQuery.getSingleResult()).thenReturn(40L);

        when(em.createQuery(contains("GROUP BY ii.stock"), eq(Object[].class)))
                .thenReturn(objectQuery);

        when(objectQuery.setMaxResults(anyInt())).thenReturn(objectQuery);
        when(objectQuery.getResultList()).thenReturn(Collections.emptyList());

        when(em.createQuery(contains("FROM Stock"), eq(Stock.class)))
                .thenReturn(stockQuery);

        when(stockQuery.setMaxResults(anyInt())).thenReturn(stockQuery);
        when(stockQuery.getResultList()).thenReturn(Collections.emptyList());

        when(em.createQuery(contains("FROM Invoice"), eq(Invoice.class)))
                .thenReturn(invoiceQuery);

        when(invoiceQuery.setMaxResults(anyInt())).thenReturn(invoiceQuery);
        when(invoiceQuery.getResultList()).thenReturn(Collections.emptyList());

        String json = adminDashboardBean.getDashboardData(admin);

        JsonObject response = JsonParser.parseString(json).getAsJsonObject();

        assertTrue(response.get("status").getAsBoolean());

        verify(em).find(Status.class, 1);
    }

    @Test
    void shouldReturnSessionExpiredWhenAdminIsNull() {

        String json = adminDashboardBean.getDashboardData(null);

        JsonObject response = JsonParser.parseString(json).getAsJsonObject();

        assertFalse(response.get("status").getAsBoolean());

        assertEquals(
                "Admin Session Expired",
                response.get("message").getAsString()
        );

        verify(em, never()).find(any(), any());
    }
}