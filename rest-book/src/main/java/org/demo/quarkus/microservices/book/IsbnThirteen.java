package org.demo.quarkus.microservices.book;

import javax.json.bind.annotation.JsonbProperty;

public class IsbnThirteen {
    @JsonbProperty("isbn_13")
    public String isbn13;
}
