package test;

import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.AuthenticationHandler;

import static io.restassured.RestAssured.given;

public class BaseTest implements RequestCapability{

    String baseUri;
    String projectKey;
    String email;
    String apiToken;
    String encodedCredStr;
    RequestSpecification request;

    @BeforeSuite
    public void beforeSuite(){
        email = "qa.haipv6@gmail.com";
        apiToken = "OqAXeSeIX2toPFWWzSTlA075";
        encodedCredStr = AuthenticationHandler.encodedCredStr(email, apiToken);
    }

    @BeforeTest
    public void beforeTest(){
        request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(RequestCapability.getAuthenticatedHeader(encodedCredStr));
    }
}
