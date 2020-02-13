import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class Tests {
    @Test
    public void testId() {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        RestAssured.
                get(url)
                .then()
                .body("userId", equalTo(1));
    }

    @Test
    public void testPrintResponse() {
        String baseUri = "https://jsonplaceholder.typicode.com";
        String basePath = "todos/1";

        RestAssured.given()
                .baseUri(baseUri)
                .basePath(basePath)
                .when().get()
                .then().extract().response()
                .prettyPrint();
    }

    @Test
    public void testJsonApi() {
        String baseUri = "https://reqres.in/";
        String basePath = "api/users/2";

        RestAssured.given()
                .baseUri(baseUri)
                .basePath(basePath)
                .when().get()
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("user_schema.json"));
    }
}
