package test;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class PUTMethod {

    public static void main(String[] args) {
        final int TARGET_POST_NUM = 1;
        String baseUri = "https://jsonplaceholder.typicode.com";

        //Request scope
        RequestSpecification request = given();
        request.baseUri(baseUri);

        //Content-type -> Header
        request.header(new Header("Content-type", "application/json; charset=UTF-8"));

        //construct body
//        PostBody postBody = new PostBody();
//        postBody.setUserId(1);
//        postBody.setTitle("Edit title");
//        postBody.setBody("Edit body");

        PostBody postBody1 = new PostBody(1, 1, "Edit Title 1", "Edit Body 1");
        PostBody postBody2 = new PostBody(1, 1, "Edit Title 2", "Edit Body 2");
        PostBody postBody3 = new PostBody(1, 1, "Edit Title 3", "Edit Body 3");

        List<PostBody> postBodies = Arrays.asList(postBody1, postBody2, postBody3);

        for (PostBody postBody : postBodies) {
            System.out.println(postBody);
            Gson gson = new Gson();
            String postBodyStr = gson.toJson(postBody);

            //Send request
            Response response = request.body(postBodyStr).put("/posts/".concat(String.valueOf(TARGET_POST_NUM)));
            //response.prettyPrint();

            //Response scope
            response.then().body("userId", equalTo(postBody.getUserId()));
            response.then().body("title", equalTo(postBody.getTitle()));
        }

    }
}
