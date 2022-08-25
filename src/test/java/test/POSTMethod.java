package test;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class POSTMethod {

    public static void main(String[] args) {
        String baseUri = "https://jsonplaceholder.typicode.com";

        //Request scope
        RequestSpecification request = given();
        request.baseUri(baseUri);

        //Content-type -> Header
        request.header(new Header("Content-type", "application/json; charset=UTF-8"));

        // Form up request body
//        String postBody ="{\n" +
//                "\"userId\": 1,\n" +
//                "\"id\": 1,\n" +
//                "\"title\": \"the request's title\",\n" +
//                "\"body\": \"the req's body\"\n" +
//                "}";

        Gson gson = new Gson();
        PostBody postBody = new PostBody();
        postBody.setUserId(1);
        postBody.setTitle("the request's title");
        postBody.setBody("the req's body");

        //Response scope
        Response response = request.body(gson.toJson(postBody)).post("/posts");
        response.prettyPrint();
        response.then().statusCode(equalTo(201));
        response.then().statusLine(containsStringIgnoringCase("201 Created"));
        response.then().body("userId", equalTo(1));

    }
}
