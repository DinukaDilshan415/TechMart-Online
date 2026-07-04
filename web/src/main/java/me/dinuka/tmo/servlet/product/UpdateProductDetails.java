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

import java.io.IOException;
import java.io.InputStream;

@MultipartConfig
@WebServlet("/UpdateProductDetails")
public class UpdateProductDetails extends HttpServlet {

    @EJB
    private ProductService productService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        String pId = request.getParameter("id");
        String description = request.getParameter("description");
        String qty = request.getParameter("stock");
        Part imagePart = request.getPart("image");

        if (request.getSession().getAttribute("admin") == null) {
            ResponseObject.addProperty("message", "Please sign in");
        } else if (description.isEmpty()) {
            ResponseObject.addProperty("message", "Product Description Can't Be Empty");
        } else if (qty.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Product Quantity");
        } else if (!RegexValidator.isValidInteger(qty)) {
            ResponseObject.addProperty("message", "Enter Valid Quantity");
        } else if (Integer.parseInt(qty) <= 0) {
            ResponseObject.addProperty("message", "Enter Valid Quantity");
        } else if (imagePart == null) {
            ResponseObject.addProperty("message", "Product Image is required");
        } else {
            if (pId != null) {

                byte[] imageBytes = null;

                if(imagePart.getSubmittedFileName() != null){
                    try (InputStream is = imagePart.getInputStream()) {
                        imageBytes = is.readAllBytes();
                    }
                }

                ProductDTO dto = new ProductDTO();
                dto.setId(Integer.parseInt(pId));
                dto.setDescription(description);
                dto.setQty(qty);
                dto.setImage(imageBytes);

                String s = productService.updateProduct(dto);

                if (s.equals("success")) {
                    ResponseObject.addProperty("status", true);
                    ResponseObject.addProperty("message", "Product Updated Successfully");
                } else {
                    ResponseObject.addProperty("message", s);
                }

            }
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
