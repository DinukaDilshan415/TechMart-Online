package me.dinuka.tmo.servlet.product;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import me.dinuka.tmo.entity.Brand;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.entity.SubCategory;
import me.dinuka.tmo.util.RegexValidator;

import java.io.IOException;
import java.util.List;

@MultipartConfig
@WebServlet("/StockCheck")
public class StockCheck extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        String title = request.getParameter("name");
        String description = request.getParameter("description");
        String subCategoryId = request.getParameter("subCategory");
        String brandId = request.getParameter("brand_id");
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
        } else if (price.isEmpty()) {
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
            SubCategory subCategory = em.find(SubCategory.class, Integer.valueOf(subCategoryId));
            Brand brand = em.find(Brand.class, Integer.valueOf(brandId));

            String productJpql = "SELECT p FROM Product p WHERE p.title = :title AND p.subCategory = :subCategory AND p.brand = :brand";
            List<Product> productList = em.createQuery(productJpql, Product.class)
                    .setParameter("title", title)
                    .setParameter("subCategory", subCategory)
                    .setParameter("brand", brand)
                    .getResultList();

            if (productList.isEmpty()) {
                ResponseObject.addProperty("message", "InsertProduct");
            } else {
                String stockJpql = "SELECT s FROM Stock s WHERE s.product IN :products AND s.price = :price";
                List<Stock> stockList = em.createQuery(stockJpql, Stock.class)
                        .setParameter("products", productList)
                        .setParameter("price", Double.valueOf(price))
                        .getResultList();

                if (stockList.isEmpty()) {
                    ResponseObject.addProperty("message", "InsertProduct");
                } else {
                    ResponseObject.addProperty("message", "UpdateProduct");
                }
            }

            ResponseObject.addProperty("status", true);
        }
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
