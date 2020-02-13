import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class Tests {
    private final String baseUri = "https://reqres.in/";
    private final String basePathSingleUser = "api/users/2";

    @Test
    public void testSingleUserJsonStructure() {
              RestAssured.given()
                .baseUri(baseUri)
                .basePath(basePathSingleUser)
                .when().get()
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("user_schema.json"));
    }
}
