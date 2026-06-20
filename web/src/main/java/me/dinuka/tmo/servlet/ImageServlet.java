package me.dinuka.tmo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/product-images/*")
public class ImageServlet extends HttpServlet {
    private static final String BASE_DIR = "C:/techmart-online-images/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get path after "/product-images"
        String requestedImage = request.getPathInfo(); // e.g. /4/image1.png

        if (requestedImage == null || requestedImage.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image not specified");
            return;
        }

        File imageFile = new File(BASE_DIR, requestedImage);

        if (!imageFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Set content type (you can improve with MIME type detection if needed)
        response.setContentType("image/png");

        // Send image to response output stream
        Files.copy(imageFile.toPath(), response.getOutputStream());
    }
}