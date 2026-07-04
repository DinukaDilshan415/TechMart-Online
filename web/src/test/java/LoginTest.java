import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.servlet.user.SignIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private EntityManager session;

    @Mock
    private TypedQuery<User> query;

    @InjectMocks
    private SignIn signIn;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession httpSession;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    private void mockRequestBody(String json) throws Exception {
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json))
        );
    }

    private JsonObject getResponse() {
        return new Gson().fromJson(responseWriter.toString(), JsonObject.class);
    }

    // Empty Email
    @Test
    void testEmptyEmail() throws Exception {
        mockRequestBody("{\"loginEmail\":\"\",\"loginPassword\":\"pass123\"}");

        signIn.doPost(request, response);

        JsonObject result = getResponse();
        assertFalse(result.get("status").getAsBoolean());
        assertEquals("Please Enter Email", result.get("message").getAsString());
    }

    //Empty Password
    @Test
    void testEmptyPassword() throws Exception {
        mockRequestBody("{\"loginEmail\":\"test@mail.com\",\"loginPassword\":\"\"}");

        signIn.doPost(request, response);

        JsonObject result = getResponse();
        assertFalse(result.get("status").getAsBoolean());
        assertEquals("Please Enter Your Password", result.get("message").getAsString());
    }

    // Invalid Credentials
    @Test
    void testInvalidCredentials() throws Exception {
        mockRequestBody("{\"loginEmail\":\"wrong@mail.com\",\"loginPassword\":\"wrongpass\"}");

        when(session.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("email", "wrong@mail.com")).thenReturn(query);
        when(query.setParameter("password", "wrongpass")).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        signIn.doPost(request, response);

        JsonObject result = getResponse();
        assertFalse(result.get("status").getAsBoolean());
        assertEquals("Invalid Credentials !", result.get("message").getAsString());
    }

    // Valid Login — Not Verified
    @Test
    void testLoginNotVerified() throws Exception {
        mockRequestBody("{\"loginEmail\":\"tendergoldwasser6@justzeus.com\",\"loginPassword\":\"Qwer@123\"}");

        User mockUser = new User();
        mockUser.setEmail("tendergoldwasser6@justzeus.com");
        mockUser.setPassword("Qwer@123");
        mockUser.setVerification_code("123456");

        when(session.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("email", "tendergoldwasser6@justzeus.com")).thenReturn(query);
        when(query.setParameter("password", "Qwer@123")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(mockUser));
        when(request.getSession()).thenReturn(httpSession);

        signIn.doPost(request, response);

        JsonObject result = getResponse();
        assertTrue(result.get("status").getAsBoolean());
        assertEquals("not verified", result.get("message").getAsString());
    }

    // Valid Login
    @Test
    void testLoginVerified() throws Exception {
        mockRequestBody("{\"loginEmail\":\"tendergoldwasser6@justzeus.com\",\"loginPassword\":\"Qwer@123\"}");

        User mockUser = new User();
        mockUser.setEmail("tendergoldwasser6@justzeus.com");
        mockUser.setPassword("Qwer@123");
        mockUser.setVerification_code("Verified");

        when(session.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("email", "tendergoldwasser6@justzeus.com")).thenReturn(query);
        when(query.setParameter("password", "Qwer@123")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(mockUser));
        when(request.getSession()).thenReturn(httpSession);

        signIn.doPost(request, response);

        JsonObject result = getResponse();
        assertTrue(result.get("status").getAsBoolean());
        assertEquals("verified", result.get("message").getAsString());
    }
}