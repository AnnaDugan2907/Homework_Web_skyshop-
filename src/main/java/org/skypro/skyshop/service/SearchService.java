package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<SearchResult> search(String pattern) {
        String lowerPattern = pattern.toLowerCase();

        return getAllSearchables().stream()
                .filter(item -> item.getName().toLowerCase().contains(lowerPattern))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> allSearchables = new ArrayList<>();
        allSearchables.addAll(storageService.getAllProducts());
        allSearchables.addAll(storageService.getAllArticles());
        return allSearchables;
    }

}
