package me.dinuka.tmo.servlet.checkout;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.util.PayHere;

import java.io.IOException;

@WebServlet("/VerifyPayments")
public class VerifyPayments extends HttpServlet {
    private static final int PAYHERE_SUCCESS = 2;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String merchant_id = request.getParameter("merchant_id");
        String order_id = request.getParameter("order_id");
        String payhere_amount = request.getParameter("payhere_amount");
        String payhere_currency = request.getParameter("payhere_currency");
        String status_code = request.getParameter("status_code");
        String md5sig = request.getParameter("md5sig");

        String merchantSecret = "MTY0MDEzMTcwMDEwMzU2OTgxOTQ4ODk4OTcyMTIzNTIxOTI1MzIz";
        String merchantSecretMD5 = PayHere.generateMD5(merchantSecret);
        String hash = PayHere.generateMD5(merchant_id + order_id + payhere_amount + payhere_currency + merchantSecretMD5);

        if (Integer.parseInt(status_code) == VerifyPayments.PAYHERE_SUCCESS) {
            System.out.println("Payment Completed. Order Id:" + order_id);
            String orderId = order_id.substring(3);
            System.out.println(orderId); // 1
        }
    }
}
