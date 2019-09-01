package com.basaki.k8s.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * {@code BookRequest} represents a request during book creation.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@Getter
public class BookRequest {

    private String title;

    private String author;

    @JsonCreator
    public BookRequest(@JsonProperty("title") String title,
            @JsonProperty("author") String author) {
        this.title = title;
        this.author = author;
    }
}
