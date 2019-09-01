package com.basaki.k8s.controller;

import com.basaki.k8s.data.entity.Book;
import com.basaki.k8s.model.BookRequest;
import com.basaki.k8s.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code BookController} exposes book service.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@RestController
@Slf4j
@Api(value = "Book Service", produces = "application/json", tags = {"1"})
public class BookController {

    private final BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    @ApiOperation(value = "Creates a book.", response = Book.class)
    @PostMapping(value = "/public/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody BookRequest request,
                       @RequestHeader("X-Request-Foo") String foo,
                       @RequestHeader("X-TXN-DATE") String timestamp) {
        System.out.println("X-Request-Foo - " + foo);
        System.out.println("X-TXN-DATE - " + timestamp);
        return service.create(request);
    }

    @ApiOperation(value = "Retrieves a book.", notes = "Requires book identifier",
            response = Book.class)
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE}, value = "/public/books/{id}")
    public Book read(@PathVariable("id") UUID id) {
        return service.read(id);
    }
}
