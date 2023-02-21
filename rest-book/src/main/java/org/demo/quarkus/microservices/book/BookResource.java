package org.demo.quarkus.microservices.book;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

@Path("/api/books")
@Tag(name = "Book REST Endpoint")
public class BookResource {

    @Inject
    Logger logger;

    @Inject
    @RestClient
    NumberProxy proxy;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new book")
    @Fallback(fallbackMethod = "fallbackOnCreatingABook")
    @Retry(maxRetries = 3, delay = 2000)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createABook( @FormParam("title") String title, @FormParam("author") String author, @FormParam("year") int yearOfPublication, @FormParam("genre") String genre){
        Book book = new Book();
        book.isbn13 = proxy.generateIsbnNumbers().isbn13;
        book.title = title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.creationDate = Instant.now();
        book.genre = genre;
        logger.info("Book created: "+book);
        return Response.status(201).entity(book).build();
    }

    public Response fallbackOnCreatingABook (@FormParam("title") String title, @FormParam("author") String author, @FormParam("year") int yearOfPublication, @FormParam("genre") String genre) throws FileNotFoundException {
        Book book = new Book();
        book.title = title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.creationDate = Instant.now();
        book.genre = genre;
        book.isbn13 = "Will be set later";
        saveBookOnDisk(book);
        logger.warn("Book saved on disk: "+book);
        return Response.status(206).entity(book).build();
    }

    private void saveBookOnDisk(Book book) throws FileNotFoundException {
        String bookJson = JsonbBuilder.create().toJson(book);
        try(PrintWriter out = new PrintWriter("book-"+Instant.now().toEpochMilli()+".json")) {
            out.println(bookJson);
        }
    }

}