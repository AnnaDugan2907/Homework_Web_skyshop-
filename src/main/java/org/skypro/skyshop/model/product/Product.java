package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Searchable {
    private final String productName;
    private final UUID id;

    public Product(UUID id,String productName ) {
        this.productName = productName;
        this.id = id;

        if (productName == null | productName.isBlank()) {
            throw new IllegalArgumentException(new StringBuilder("Название продукта не может быть пустым").toString());
        }
    }

    public abstract double getProductPrice();

    public abstract boolean isSpecial();

    public String getProductName() {
        return productName;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return getProductName();
    }

    @JsonIgnore
    public String getSearchTerm() {
        return getProductName();
    }

    @JsonIgnore
    public String getContentType() {
        return "PRODUCT";
    }

    @Override
    public String toString() {
        return new StringBuilder(productName).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productName, product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName);
    }

}
