package me.dinuka.tmo;

import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CartService;
import me.dinuka.tmo.util.RegexValidator;

import java.util.ArrayList;
import java.util.List;

@Stateful
public class CartSessionBean implements CartService {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    private List<Cart> sessionCart = new ArrayList<>();

    @Override
    public String addProductToCart(String productId, String qty, User user) {
        Product product = em.find(Product.class, Integer.valueOf(productId));

        String stockJpql = "SELECT s FROM Stock s WHERE s.product = :product";
        List<Stock> stockResults = em.createQuery(stockJpql, Stock.class)
                .setParameter("product", product)
                .getResultList();
        Stock pStock = stockResults.isEmpty() ? null : stockResults.get(0);

        if (pStock != null) {
            String cartJpql = "SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product";
            List<Cart> cartList = em.createQuery(cartJpql, Cart.class)
                    .setParameter("user", user)
                    .setParameter("product", product)
                    .getResultList();

            int requestedQty = Integer.parseInt(qty);

            if (cartList.isEmpty()) { // Product not in cart
                if (requestedQty <= pStock.getQty()) { // Product qty available
                    Cart cart = new Cart();
                    cart.setQty(requestedQty);
                    cart.setProduct(product);
                    cart.setUser(user);
                    cart.setStock(pStock);

                    em.persist(cart);

                    return "success";
                } else {
                    return "Insuficient Product Quantity";
                }
            } else { // Product already exists in cart
                Cart cart = cartList.get(0);
                int newQty = cart.getQty() + requestedQty;

                if (newQty <= pStock.getQty()) { // Product qty available
                    cart.setQty(newQty);

                    em.merge(cart);

                    return "success";
                } else {
                    return "Insuficient Product Quantity";
                }
            }
        } else {
            return "Product Not Found";
        }
    }

    @Override
    public String addProductToSessionCart(String productId, String quantity) {
        return addProductToStatefulCart(productId, quantity);
    }

    @Override
    public String addSessionCartToDatabase(List<Cart> sessionList, User user) {
        if (!sessionList.isEmpty()) {
            for (Cart sessionCart : sessionList) {
                Product product = em.find(Product.class, sessionCart.getProduct().getId());
                Stock stock = em.find(Stock.class, sessionCart.getStock().getId());

                String jpql = "SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product";
                List<Cart> cartList = em.createQuery(jpql, Cart.class)
                        .setParameter("user", user)
                        .setParameter("product", sessionCart.getProduct())
                        .getResultList();

                if (cartList.isEmpty()) {
                    sessionCart.setUser(user);

                    em.persist(sessionCart);

                    return "success";

                } else { // Product already exists in user's DB cart
                    Cart dbcart = cartList.get(0);
                    int newQty = dbcart.getQty() + sessionCart.getQty();

                    if (newQty <= stock.getQty()) {
                        dbcart.setQty(newQty);
                        dbcart.setUser(user);

                        em.merge(dbcart);

                        return "success";
                    } else {
                        return "Insuficient Product Quantity";
                    }
                }
            }

        }
        return "";
    }

    public String addProductToStatefulCart(String productId, String qty) {
        Product product = em.find(Product.class, Integer.valueOf(productId));

        if (product == null) {
            return "Product Not Found";
        }

        String stockJpql = "SELECT s FROM Stock s WHERE s.product = :product";
        List<Stock> stockResults = em.createQuery(stockJpql, Stock.class)
                .setParameter("product", product)
                .getResultList();
        Stock pStock = stockResults.isEmpty() ? null : stockResults.get(0);

        if (pStock == null) {
            return "Product Not Found";
        }

        int requestedQty = Integer.parseInt(qty);

        // Check if product already in cart
        Cart foundCart = sessionCart.stream()
                .filter(c -> c.getProduct().getId() == product.getId())
                .findFirst()
                .orElse(null);

        if (foundCart != null) {
            int newQty = foundCart.getQty() + requestedQty;
            if (newQty <= pStock.getQty()) {
                foundCart.setQty(newQty);
                return "success";
            } else {
                return "Insuficient Product Quantity";
            }
        } else {
            if (requestedQty <= pStock.getQty()) {
                Cart cart = new Cart();
                cart.setQty(requestedQty);
                cart.setUser(null);
                cart.setProduct(product);
                cart.setStock(pStock);
                sessionCart.add(cart);
                return "success";
            } else {
                return "Insuficient Product Quantity";
            }
        }
    }

    @Override
    public List<Cart> getSessionCart() {
        return sessionCart;
    }

    @Override
    public String removeFromSessionCart(String id, User user) {
        Product product = em.find(Product.class, Integer.valueOf(id));

        String jpql = "SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product";
        List<Cart> cartList = em.createQuery(jpql, Cart.class)
                .setParameter("user", user)
                .setParameter("product", product)
                .getResultList();

        if (!cartList.isEmpty()) {
            Cart cartItem = cartList.get(0);

            em.remove(cartItem);

            return "success";
        } else {
            System.out.println("Product Not Found");
            return "Product Not Found";
        }

    }

    @Override
    public String updateCartQtyFromCart(String id, String qty, User user) {

        if (!RegexValidator.isValidInteger(id)) {
            return "Invalid Product Id";
        } else if (!RegexValidator.isValidInteger(qty)) {
            return "Invalid Product Qty";
        } else {
            Product product = em.find(Product.class, Integer.valueOf(id));

            if (product == null) {
                return "Product Not Found";
            }

            String stockJpql = "SELECT s FROM Stock s WHERE s.product = :product";
            List<Stock> stockResults = em.createQuery(stockJpql, Stock.class)
                    .setParameter("product", product)
                    .getResultList();
            Stock pStock = stockResults.isEmpty() ? null : stockResults.get(0);

            if (user != null) {
                String cartJpql = "SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product";
                List<Cart> cartList = em.createQuery(cartJpql, Cart.class)
                        .setParameter("user", user)
                        .setParameter("product", product)
                        .getResultList();

                if (cartList.isEmpty()) { // Product not found in user's cart
                    return "Product Not Found";
                } else {
                    Cart cart = cartList.get(0);
                    int targetQty = Integer.parseInt(qty);

                    if (pStock != null && targetQty <= pStock.getQty()) {
                        cart.setQty(targetQty);

                        System.out.println("Cart Updated:"+" Product Id: " + cart.getProduct().getId() + " Qty: " + cart.getQty());

                        em.merge(cart);

                        return "success";
                    } else {
                        return "Insuficient Product Quantity";
                    }
                }
            } else {
                return "User Not Logged In";
            }
        }
    }

    public void clearSessionCart() {
        sessionCart.clear();
    }

    public void removeFromSessionCart(int productId) {
        sessionCart.removeIf(c -> c.getProduct().getId() == productId);
    }

}
