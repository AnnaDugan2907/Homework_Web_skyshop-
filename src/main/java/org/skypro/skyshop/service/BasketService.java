package org.skypro.skyshop.service;

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
    private final ProductBasket productBasket;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }
    
    public void addProductToBasket(UUID id) {
        Product product = storageService.getProductById(id);
        productBasket.addProduct(id);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> productsMap = productBasket.getProducts();

        List<BasketItem> items = productsMap.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = storageService.getProductById(productId);
                    return new BasketItem(product, quantity);
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }

}
