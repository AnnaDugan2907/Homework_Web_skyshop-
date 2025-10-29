package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> productStorage;
    private final Map<UUID, Article> articleStorage;

    public StorageService() {
        productStorage = new HashMap<>();
        articleStorage = new HashMap<>();
        ProductBasket();
    }

    private void ProductBasket() {
        Article article1 = new Article(UUID.randomUUID(), "Тенденции моды 2024", "Обзор последних трендов в мире моды этого года.");
        Article article2 = new Article(UUID.randomUUID(), "Книги, которые стоит прочитать", "Обзор лучших новинок и классики литературы.");
        Article article3 = new Article(UUID.randomUUID(), "Туризм и приключения", "Лучшие направления для активного отдыха.");

        Product product1 = new Product(UUID.randomUUID(), "Хлеб") {
            @Override
            public double getProductPrice() {
                return 45;
            }

            @Override
            public boolean isSpecial() {
                return false;
            }
        };
        Product product2 = new Product(UUID.randomUUID(), "Мясо") {
            @Override
            public double getProductPrice() {
                return 450;
            }

            @Override
            public boolean isSpecial() {
                return true;
            }
        };
        Product product3 = new Product(UUID.randomUUID(), "Масло") {
            @Override
            public double getProductPrice() {
                return 155;
            }

            @Override
            public boolean isSpecial() {
                return false;
            }
        };

        articleStorage.put(article1.getId(), article1);
        articleStorage.put(article2.getId(), article2);
        articleStorage.put(article3.getId(), article3);

        productStorage.put(product1.getId(), product1);
        productStorage.put(product2.getId(), product2);
        productStorage.put(product3.getId(), product3);
    }

    public Collection<Article> getAllArticles() {
        return articleStorage.values();
    }

    public Collection<Product> getAllProducts() {
        return productStorage.values();
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productStorage.get(id));
    }

}
