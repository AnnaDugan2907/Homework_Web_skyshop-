package org.skypro.skyshop;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.NoSuchProductException;
import org.skypro.skyshop.service.StorageService;

import java.util.*;
import java.util.stream.Collectors;

public class BasketServiceTest {

    private StorageService storageServiceMock;
    private ProductBasket productBasketMock;
    private BasketService basketService;

    @BeforeEach
    public void setUp() {
        storageServiceMock = mock(StorageService.class);
        productBasketMock = mock(ProductBasket.class);
        basketService = new BasketService(productBasketMock, storageServiceMock);
    }

    //Добавление несуществующего товара в корзину приводит к выбросу исключения.
    @Test
    public void testAddProductToBasket_ProductDoesNotExist_Throws() {
        UUID nonexistentId = UUID.randomUUID();

        when(storageServiceMock.getProductById(nonexistentId))
                .thenThrow(new NoSuchProductException("Продукт не найден"));

        assertThrows(NoSuchProductException.class, () -> {
            basketService.addProductToBasket(nonexistentId);
        });

        verify(productBasketMock, never()).addProduct(nonexistentId);
    }

    //Добавление существующего товара вызывает метод addProduct у мока ProductBasket
    @Test
    public void testAddProductToBasket_ProductExists() {
        UUID productId = UUID.randomUUID();

        Product mockProduct = mock(Product.class);
        when(storageServiceMock.getProductById(productId)).thenReturn(mockProduct);

        basketService.addProductToBasket(productId);

        verify(productBasketMock).addProduct(productId);
    }

    //Метод getUserBacket возвращает пустую корзину, если ProductBasket пуст.
    @Test
    public void testGetUserBasket_Empty() {
        when(productBasketMock.getProducts()).thenReturn(Collections.emptyMap());

        UserBasket basket = basketService.getUserBasket();

        assertNotNull(basket);
        assertTrue(basket.getItems().isEmpty());
    }

    //Метод getUserBasket возвращает подходящую корзину, если в ProductBasket есть товары.
    @Test
    public void testGetUserBasket_ReturnsCorrectBasket() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Map<UUID, Integer> productsMap = new HashMap<>();
        productsMap.put(productId1, 2);
        productsMap.put(productId2, 3);

        when(productBasketMock.getProducts()).thenReturn(productsMap);

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(product1.getId()).thenReturn(productId1);
        when(product2.getId()).thenReturn(productId2);

        when(storageServiceMock.getProductById(productId1)).thenReturn(product1);
        when(storageServiceMock.getProductById(productId2)).thenReturn(product2);

        UserBasket basket = basketService.getUserBasket();

        assertNotNull(basket);
        assertEquals(2, basket.getItems().size());

        List<BasketItem> items = basket.getItems();
        Map<UUID, Integer> itemMap = items.stream()
                .filter(item -> item.getProduct().getId() != null)
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        BasketItem::getQuantity,
                        (existing, replacement) -> existing
                ));

        assertEquals(2, itemMap.get(productId1));
        assertEquals(3, itemMap.get(productId2));
    }

}
