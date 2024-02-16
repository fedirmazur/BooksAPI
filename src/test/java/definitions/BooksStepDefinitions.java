package definitions;

import controllers.BooksController;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Book;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.*;


public class BooksStepDefinitions {
    BooksController controller = new BooksController();
    List<Book> allBooks;
    Book book;
    Response response;
    Book bookToCreate;
    Book bookToUpdate;

    @When("^user makes GET request for Books API$")
    public void getRequestForAllBooks() {
        allBooks = controller.getBooks();
    }

    @Then("list of books is returned")
    public void listOfBooksIsReturned() {
        SoftAssertions softly = new SoftAssertions();
        assertThat(allBooks).isNotEmpty();
        allBooks.forEach(book -> {
            softly.assertThat(book.getId()).as("%s has id = null", book.toString()).isNotNull();
            softly.assertThat(book.getName()).as("%s has name = null", book.toString()).isNotNull();
            softly.assertThat(book.getAuthor()).as("%s has author = null", book.toString()).isNotNull();
            softly.assertThat(book.getPublication()).as("%s has publication = null", book.toString()).isNotNull();
            softly.assertThat(book.getCategory()).as("%s has category = null", book.toString()).isNotNull();
            softly.assertThat(book.getPages()).as("%s has pages = null or less or equal to 0", book.toString()).isGreaterThan(0).isNotNull();
            softly.assertThat(book.getPrice()).as("%s has price = null or less or equal to 0", book.toString()).isGreaterThan(0).isNotNull();
        });
        softly.assertAll();
    }


    @When("user makes GET request with id {int} to Books API")
    public void userMakesGETRequestWithIdToBooksAPI(int id) {
        allBooks = controller.getBooks();
        AtomicBoolean isIdPresent = new AtomicBoolean(false);
        allBooks.forEach(book1 -> {
            if(book1.getId() == id) {
                book = controller.getBookById(id);
                isIdPresent.set(true);
            }
        });
        if(!isIdPresent.get()) {
            throw new AssertionError(String.format("There is no book with id %s in the Book API", id));
        }
    }


    @Then("user gets a book with certain details")
    public void userGetsABookWithCertainDetails(DataTable dataTable) {
        Map<String,String> data = dataTable.asMaps(String.class,String.class).get(0);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(Integer.parseInt(data.get("id"))).isEqualTo(book.getId());
        softly.assertThat(data.get("name")).isEqualTo(book.getName());
        softly.assertThat(data.get("author")).isEqualTo(book.getAuthor());
        softly.assertThat(data.get("publication")).isEqualTo(book.getPublication());
        softly.assertThat(data.get("category")).isEqualTo(book.getCategory());
        softly.assertThat(Integer.parseInt(data.get("pages"))).isEqualTo(book.getPages());
        softly.assertThat(Double.parseDouble(data.get("price"))).isEqualTo(book.getPrice());
        softly.assertAll();

        controller.deleteBook(book);
    }

    @When("user makes POST request to create new book with certain details")
    public void userMakesPOSTRequestToCreateNewBookWithCertainDetails(DataTable dataTable) {
        Map<String,String> data = dataTable.asMaps(String.class,String.class).get(0);
            bookToCreate = Book.builder()
                    .name(data.get("name"))
                    .author(data.get("author"))
                    .publication("publication")
                    .category(data.get("category"))
                    .pages(Integer.parseInt(data.get("pages")))
                    .price(Double.parseDouble(data.get("price")))
                    .build();

        response = controller.createBook(bookToCreate);
        response.then().log().all();
    }


    @Then("book is created and has all details according to request")
    public void bookIsCreatedAndHasAllDetailsAccordingToRequest() {
        book = response.getBody().as(Book.class);
        Book expectedBook = bookToCreate;
        if (expectedBook != null) {
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(expectedBook.getName()).isEqualTo(book.getName());
            softly.assertThat(expectedBook.getAuthor()).isEqualTo(book.getAuthor());
            softly.assertThat(expectedBook.getPublication()).isEqualTo(book.getPublication());
            softly.assertThat(expectedBook.getCategory()).isEqualTo(book.getCategory());
            softly.assertThat(expectedBook.getPages()).isEqualTo(book.getPages());
            softly.assertThat(expectedBook.getPrice()).isEqualTo(book.getPrice());
            softly.assertAll();
        }
        controller.deleteBook(book).then().log().all();
    }

    @When("user deletes previously created book")
    public void userDeletesPreviouslyCreatedBook() {
        book = response.getBody().as(Book.class);
        response = controller.deleteBook(book);
    }

    @Then("book is deleted")
    public void bookIsDeleted() {
        response = controller.getBookByIdResponse(book.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @When("user makes GET request for Books API to get response")
    public void userMakesGETRequestForBooksAPIToGetResponse() {
        response = controller.getBooksAndValidateJsonSchema();
        response.then().log().all();
    }

    @Then("response matches to JSON schema for all books")
    public void responseMatchesToJSONSchemaForAllBooks() {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("all_books_JSON_schema.json"));
    }

    @When("user makes PUT request to update book")
    public void userMakesPUTRequestToUpdateBook(DataTable dataTable) {
        Map<String,String> data = dataTable.asMaps(String.class,String.class).get(0);
        bookToUpdate = Book.builder()
                .id(Integer.parseInt(data.get("id")))
                .name(data.get("name"))
                .author(data.get("author"))
                .publication("publication")
                .category(data.get("category"))
                .pages(Integer.parseInt(data.get("pages")))
                .price(Double.parseDouble(data.get("price")))
                .build();

        response = controller.updateBook(bookToUpdate);
        response.then().log().all();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @When("user checks that book is updated")
    public void userChecksThatBookIsUpdated() {
        Book updatedBook = response.as(Book.class);
        assertThat(updatedBook).isEqualTo(bookToUpdate);
    }

    @Given("setup")
    public void setup() {
        allBooks = new ArrayList<>();
        book = new Book();
        response = null;
        bookToCreate = new Book();
        bookToUpdate = new Book();
    }

    @When("user makes GET request with invalid id {int} to Books API")
    public void userMakesGETRequestWithInvalidIdToBooksAPI(Object wrongId) {
        response = controller.getBookByInvalidId(wrongId);
        response.then().log().all();
    }


    @Then("user checks that response has {int} status code")
    public void userChecksThatResponseHasTheCorrectStatusCode(int statusCode) {
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @When("user makes POST request to create new book with invalid request body")
    public void userMakesPOSTRequestToCreateNewBookWithInvalidRequestBody() {
        String jsonString = "{\n" +
                "    \"name\": 123,\n" +
                "    \"author\": 456,\n" +
                "    \"publication\": 789,\n" +
                "    \"category\": 456,\n" +
                "    \"pages\": \"test\",\n" +
                "    \"price\": true\n" +
                "}";
        response = controller.createBookWithInvalidBody(jsonString);
        response.then().log().all();
    }

    @When("user makes GET request for Books API with invalid credentials")
    public void userMakesGETRequestForBooksAPIWithInvalidCredentials() {
        response = controller.getBooksUsingInvalidCredentials();
        response.then().log().all();
    }
}
