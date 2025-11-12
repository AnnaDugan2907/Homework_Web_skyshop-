package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;


public class SearchServiceTest {

    private StorageService storageService;
    private SearchService searchService;

    @BeforeEach
    public void setUp() {
        storageService = mock(StorageService.class);
        searchService = new SearchService(storageService);
    }

    //Поиск в случае отсутствия объектов в StorageService
    @Test
    public void testSearch_NoObjectsInStorage() {
        when(storageService.getAllProducts()).thenReturn(Collections.emptyList());
        when(storageService.getAllArticles()).thenReturn(Collections.emptyList());

        var results = searchService.search("test");
        assertTrue(results.isEmpty(), "Результаты должны быть пустыми, если ничего нет в хранилище");
    }


    //Поиск в случае, если объекты в StorageService есть, но нет подходящего
    @Test
    public void testSearch_NoMatchingObjects() {
        when(storageService.getAllProducts()).thenReturn(Arrays.asList(
                new Product(UUID.randomUUID(), "Масло") {
                    @Override
                    public double getProductPrice() { return 10; }
                    @Override
                    public boolean isSpecial() { return false; }
                }
        ));
        when(storageService.getAllArticles()).thenReturn(Collections.emptyList());

        var results = searchService.search("Мясо");
        assertTrue(results.isEmpty(), "Не должно быть совпадений");
    }

    //Поиск, когда есть подходящий объект в StorageService
    @Test
    public void testSearch_FoundMatchingProduct() {
        Product matchingProduct = new Product(UUID.randomUUID(), "Мясо") {
            @Override
            public double getProductPrice() { return 15; }
            @Override
            public boolean isSpecial() { return false; }
        };

        when(storageService.getAllProducts()).thenReturn(Arrays.asList(matchingProduct));
        when(storageService.getAllArticles()).thenReturn(Collections.emptyList());

        var results = searchService.search("Мясо");
        assertFalse(results.isEmpty(), "Должен быть найден продукт");
        assertEquals(1, results.size());
        assertEquals("Мясо", results.iterator().next().getName());
    }

    //Поиск, когда есть подходящий объект в соответствие в статья
    @Test
    public void testSearch_MatchingInArticles() {
        Article article = new Article(UUID.randomUUID(), "Тенденции моды 2024","");
        when(storageService.getAllProducts()).thenReturn(Collections.emptyList());
        when(storageService.getAllArticles()).thenReturn(Arrays.asList(article));

        var results = searchService.search("моды");
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Тенденции моды 2024: ", results.iterator().next().getName());
    }

}
