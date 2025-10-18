package org.skypro.skyshop.model.search;

import java.util.UUID;

public interface Searchable {
    UUID getId();

    public String getSearchTerm();

    public String getContentType();

    public String getName();

    default String getStringRepresentation(){
        return getName() + " - " + getContentType();
    }
}
