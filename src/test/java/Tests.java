import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class Tests {
    private final String baseUri = "https://reqres.in/";
    private final String basePathUsers = "api/users/";
    private final String basePathLogin = "api/login/";

    @Test
    public void testSingleUserJsonStructure() {
        String basePath = basePathUsers + String.valueOf(2);

        RestAssured.given()
            .baseUri(baseUri)
            .basePath(basePath)
            .when().get()
            .then()
            .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user_schema.json"));
    }

    @Test
    public void testSingleUserZeroId() {
        String basePathSingleUserZeroId = basePathUsers + String.valueOf(0);

        RestAssured.given()
            .baseUri(baseUri)
            .basePath(basePathSingleUserZeroId)
            .when().get()
            .then()
            .assertThat()
                .statusCode(404)
                .body(matchesJsonSchemaInClasspath("schemas/empty_schema.json"));
    }

    @Test
    public void testSingleUserNegativeId() {
        String basePathSingleUserNegativeId = basePathUsers + String.valueOf(-1);

        RestAssured.given()
            .baseUri(baseUri)
            .basePath(basePathSingleUserNegativeId)
            .when().get()
            .then()
            .assertThat()
                .statusCode(404)
                .body(matchesJsonSchemaInClasspath("schemas/empty_schema.json"));
    }

    @Test
    public void testSingleUserNotFound() {
        String basePathSingleUserNotFound = basePathUsers + String.valueOf(23);

        RestAssured.given()
            .baseUri(baseUri)
            .basePath(basePathSingleUserNotFound)
            .when().get()
            .then()
            .assertThat()
                .statusCode(404)
                .body(matchesJsonSchemaInClasspath("schemas/empty_schema.json"));
    }

    @Test
    public void testCreateUser() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "morpheus");
        jsonObject.put("job", "leader");

        RestAssured.given()
            .baseUri(baseUri)
            .contentType(ContentType.JSON)
            .body(jsonObject.toString())
            .when().post(basePathUsers)
            .then()
            .assertThat()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create_user_schema.json"));
    }

    @Test
    public void testLoginSuccessful() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "eve.holt@reqres.in");
        jsonObject.put("password", "cityslicka");

        RestAssured.given()
            .baseUri(baseUri)
            .contentType(ContentType.JSON)
            .body(jsonObject.toString())
            .when().post(basePathLogin)
            .then()
            .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/login_successful_schema.json"));
    }

    @Test
    public void testLoginUnsuccessfulMissingPassword() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "eve.holt@reqres.in");

        RestAssured.given()
            .baseUri(baseUri)
            .contentType(ContentType.JSON)
            .body(jsonObject.toString())
            .when().post(basePathLogin)
            .then()
            .assertThat()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testLoginUnsuccessfulMissingEmail() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", "cityslicka");

        RestAssured.given()
            .baseUri(baseUri)
            .contentType(ContentType.JSON)
            .body(jsonObject.toString())
            .when().post(basePathLogin)
            .then()
            .assertThat()
                .statusCode(400)
                .body("error", equalTo("Missing email or username"));
    }

    @Test
    public void testLoginUnsuccessfulEmptyBody() {
        JSONObject jsonObject = new JSONObject();

        RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(jsonObject.toString())
                .when().post(basePathLogin)
                .then()
                .assertThat()
                .statusCode(400)
                .body("error", equalTo("Missing email or username"));
    }

    @Test
    public void testLoginUnsuccessfulNotExistingUser() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "notexisitnguser@nomail.com");
        jsonObject.put("password", "password");

        RestAssured.given()
            .baseUri(baseUri)
            .contentType(ContentType.JSON)
            .body(jsonObject.toString())
            .when().post(basePathLogin)
            .then()
            .assertThat()
                .statusCode(400)
                .body("error", equalTo("user not found"));
    }
}