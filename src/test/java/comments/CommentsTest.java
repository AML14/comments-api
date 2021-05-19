package comments;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import net.minidev.json.JSONValue;
import org.junit.Assert;
import org.junit.Test;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class CommentsTest {

    private static final String OAI_JSON_URL = "src/main/webapp/docs/swagger.yaml";
    private final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(OAI_JSON_URL);

    private String BASE_URI_COMMENTS = "http://localhost:8080/api/comments";


    @Test
    public void getAllComments() {
        try {
            RestAssured
                    .given()
                    .log().all()
//                    .filter(validationFilter)
            .when()
                    .get(BASE_URI_COMMENTS)
            .then()
                    .log().all()
                    .assertThat()
                    .statusCode(lessThan(300))
                    .statusCode(greaterThanOrEqualTo(200))
                    .body("size()", org.hamcrest.Matchers.greaterThan(1));

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void getOneComment() {
        try {
            RestAssured
                    .given()
                    .log().all()
                    .filter(validationFilter)
            .when()
                    .get(BASE_URI_COMMENTS+"/c1")
            .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("id", is("c1"));

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void getOneNonExistingComment() {
        try {
            RestAssured
                    .given()
                    .log().all()
                    .filter(validationFilter)
            .when()
                    .get(BASE_URI_COMMENTS+"/fsfasdf")
            .then()
                    .log().all()
                    .assertThat()
                    .statusCode(404);

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void putComment() {

        JSONObject requestParam = new JSONObject();
        requestParam.put("id", "c1");
        requestParam.put("userName", "Prueba");
        requestParam.put("text", "Hello my name is John Doe");
        requestParam.put("date", "2015-01-16T20:44:53.950");
        requestParam.put("type", "Complain");

        Header acceptJson = new Header("content-type", "application/json");
        try {
            RestAssured
                    .given()
                    .log().all()
//                    .filter(validationFilter)
                    .header(acceptJson)
                    .body(requestParam.toJSONString())
            .when()
                    .put(BASE_URI_COMMENTS)
            .then()
                    .log().all()
                    .assertThat()
                    .statusCode(lessThan(500));

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void postComment() {

        JSONObject requestParam = new JSONObject();
        requestParam.put("id", "c1");
        requestParam.put("userName", "Prueba");
        requestParam.put("text", "Hello my name is John Doe");
        requestParam.put("date", "2015-01-16T20:44:53.950");

        Header acceptJson = new Header("content-type", "application/json");
        try {
            RestAssured
                    .given()
                    .log().all()
//                    .filter(validationFilter)
                    .header(acceptJson)
                    .body(requestParam.toJSONString())
            .when()
                    .post(BASE_URI_COMMENTS)
            .then()
                    .log().all();

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void deleteOneComment() {
        try {
            RestAssured
                    .given()
                    .log().all()
                    .filter(validationFilter)
            .when()
                    .delete(BASE_URI_COMMENTS+"/c2")
            .then()
                    .log().all()
                    .assertThat()
                    .statusCode(204);

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            fail(ex.getMessage());
        }
    }


    @Test
    public void postAndGetComment() {

        JSONObject requestParam = new JSONObject();
        requestParam.put("id", "c1");
        requestParam.put("userName", "Prueba");
        requestParam.put("text", "Hello my name is John Doe");
        requestParam.put("date", "2015-01-16T20:44:53.950");

        Header acceptJson = new Header("content-type", "application/json");
        try {
            Response response = RestAssured.
                    given()
//                    .filter(validationFilter)
                    .header(acceptJson)
                    .body(requestParam.toJSONString())
                    .log().all()
                    .when()
                    .post(BASE_URI_COMMENTS);
            response.then().log().all();
            assertTrue(response.getStatusCode()<500);

            Response response2 = RestAssured.
                    given()
//                    .filter(validationFilter)
                    .log().all()
                    .when()
                    .get(BASE_URI_COMMENTS + "/" + response.getBody().path("id"));
            response2.then().log().all();
            response2.then().body("id", equalTo(response.getBody().path("id")));
            assertTrue(response2.getStatusCode()<400);

            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            fail(ex.getMessage());
        }
    }

}
