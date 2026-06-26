package me.dinuka.tmo.servlet.product;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import me.dinuka.tmo.dto.ProductDTO;
import me.dinuka.tmo.service.ProductService;
import me.dinuka.tmo.util.RegexValidator;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@MultipartConfig
@WebServlet("/SaveProduct")
public class SaveProduct extends HttpServlet {
    
    @EJB
    private ProductService productService;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        String title = request.getParameter("name");
        String description = request.getParameter("description");
        String subCategoryId = request.getParameter("subCategory");
        String brandId = request.getParameter("brand_id");
        String warehouseId = request.getParameter("warehouse_id");
        String discount = request.getParameter("discount");
        String price = request.getParameter("price");
        String qty = request.getParameter("stock");
        Part imagePart = request.getPart("image");

        if (request.getSession().getAttribute("admin") == null) {
            ResponseObject.addProperty("message", "Please sign in");
        } else if (title.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Product Name");
        } else if (description.isEmpty()) {
            ResponseObject.addProperty("message", "Product Description Can't Be Empty");
        } else if (subCategoryId.isEmpty()) {
            ResponseObject.addProperty("message", "Please Select Sub Category");
        } else if (brandId.equals("0")) {
            ResponseObject.addProperty("message", "Please Select Brand");
        } else if (qty.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Product Quantity");
        } else if (!RegexValidator.isValidInteger(qty)) {
            ResponseObject.addProperty("message", "Enter Valid Quantity");
        } else if (Integer.parseInt(qty) <= 0) {
            ResponseObject.addProperty("message", "Enter Valid Quantity");
        } else if (warehouseId.equals("0")) {
            ResponseObject.addProperty("message", "Please Select Warehouse");
        }else if (price.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Product Price");
        } else if (!RegexValidator.isValidAmount(price)) {
            ResponseObject.addProperty("message", "Enter Valid Price");
        } else if (Double.parseDouble(price) <= 0) {
            ResponseObject.addProperty("message", "Enter Valid Price");
        } else if (!RegexValidator.isValidInteger(discount)) {
            ResponseObject.addProperty("message", "Enter Valid Discount");
        } else if (imagePart == null) {
            ResponseObject.addProperty("message", "Product Image is required");
        } else {
            byte[] imageBytes;

            try (InputStream is = imagePart.getInputStream()) {
                imageBytes = is.readAllBytes();
            }

            DbConnectionTracker.connectionAcquired();

            String s = productService.addProduct(
                    new ProductDTO(title,
                            description,
                            subCategoryId,
                            brandId,
                            discount,
                            "",
                            "0",
                            price,
                            qty,
                            warehouseId,
                            imageBytes));

            if (s.equals("success")) {
                ResponseObject.addProperty("status", true);
                ResponseObject.addProperty("message", "Product Added Successfully");
            }
        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
