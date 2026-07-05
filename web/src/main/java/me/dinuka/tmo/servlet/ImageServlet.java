package me.dinuka.tmo.servlet;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.EnvConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/product-images/*")
public class ImageServlet extends HttpServlet {

    private final String BASE_DIR = EnvConfig.get("IMG_BASE_PATH");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestedImage = request.getPathInfo();

        if (requestedImage == null || requestedImage.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image not specified");
            return;
        }

        File imageFile = new File(BASE_DIR, requestedImage);

        if (!imageFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("image/png");

        Files.copy(imageFile.toPath(), response.getOutputStream());
    }
}