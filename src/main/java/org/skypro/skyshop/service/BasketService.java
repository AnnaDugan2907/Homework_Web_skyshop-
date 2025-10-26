package org.skypro.skyshop.service;

import jakarta.servlet.http.HttpSession;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final StorageService storageService;
    //    private static final String productBasket = "userBasket";
    private final ProductBasket productBasket;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }
//    private ProductBasket getBasketFromSession(HttpSession session){
//        ProductBasket basket = (ProductBasket) session.getAttribute(productBasket);
//        if (basket == null) {
//            basket = new ProductBasket();
//            session.setAttribute(productBasket, basket);
//        }
//        return basket;
//    }

//    public void addProductToBasket(UUID id, HttpSession session) {
//        ProductBasket basket = getBasketFromSession(session);
//
//        if (!storageService.getProductById(id).isPresent()) {
//            throw new IllegalArgumentException("Товар с id " + id + " не найден");
//        }
//
//        basket.addProduct(id);
//    }

    public void addProductToBasket(UUID id) {
        if (!storageService.getProductById(id).isPresent()) {
            throw new IllegalArgumentException("Товар с id " + id + " не найден");
        }

        productBasket.addProduct(id);
    }

//    public UserBasket getUserBasket(HttpSession session) {
//        ProductBasket basket = getBasketFromSession(session);
//        Map<UUID, Integer> productsMap = basket.getProducts();
//
//        List<BasketItem> items = productsMap.entrySet().stream()
//                .map(entry -> {
//                    UUID productId = entry.getKey();
//                    int quantity = entry.getValue();
//                    Product product = storageService.getProductById(productId)
//                            .orElseThrow(() -> new IllegalStateException("Продукт не найден для id: " + productId));
//                    return new BasketItem(product, quantity);
//                })
//                .collect(Collectors.toList());
//
//        return new UserBasket(items);
//    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> productsMap = productBasket.getProducts();

        List<BasketItem> items = productsMap.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = storageService.getProductById(productId)
                            .orElseThrow(() -> new IllegalStateException("Продукт не найден для id: " + productId));
                    return new BasketItem(product, quantity);
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }

}
