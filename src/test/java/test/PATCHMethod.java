package test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.BuildJsonModel;
import model.PostBody;
import model.RequestCapability;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PATCHMethod implements RequestCapability {

    public static void main(String[] args) {
        final String TARGET_POST_NUM = "1";
        String baseUri = "https://jsonplaceholder.typicode.com";

        //Form up request instance, baseUri
        RequestSpecification request = given();
        request.baseUri(baseUri);

        //Content-type -> Header
        request.header(defaultHeader);

        //Form up body
        PostBody postBody = new PostBody();
        postBody.setTitle("Edit Title");
        String postBodyStr = BuildJsonModel.parseJSONString(postBody);

        Response response = request.body(postBodyStr).patch("/posts/".concat(TARGET_POST_NUM));
        response.prettyPrint();
        response.then().body("title", equalTo(postBody.getTitle()));

    }
}
