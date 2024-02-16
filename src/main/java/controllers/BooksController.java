package controllers;

import client.Client;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Book;
import org.json.JSONObject;
import utils.PropertyUtils;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class BooksController extends Client {

    public List<Book> getBooks() {
        return Arrays.asList(given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath())
                .get()
                .then()
                .spec(responseSpecification)
                .extract().as(Book[].class));
    }

    public Response getBooksUsingInvalidCredentials() {
        return given()
                .spec(requestSpecificationWithInvalidCredentials)
                .when()
                .basePath(PropertyUtils.getPath())
                .get();
    }

    public Response getBooksAndValidateJsonSchema() {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath())
                .get();
    }

    public Book getBookById(Integer id) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath() + "/" + id)
                .get()
                .then()
                .spec(responseSpecification)
                .extract().as(Book.class);
    }

    public Response getBookByInvalidId(Object id) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath() + "/" + id)
                .get();
    }

    public Response getBookByIdResponse(Integer id) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath() + "/" + id)
                .get();
    }

    public Response createBook(Book book) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath())
                .body(book)
                .post();
    }

    public Response createBookWithInvalidBody(String invalidRequest) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath())
                .body(invalidRequest)
                .post();
    }

    public Response updateBook(Book book) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath())
                .body(book)
                .put(String.valueOf(book.getId()));
    }

    public Response deleteBook(Book book) {
        return given()
                .spec(requestSpecification)
                .when()
                .basePath(PropertyUtils.getPath())
                .body(book)
                .delete(String.valueOf(book.getId()));
    }
}
