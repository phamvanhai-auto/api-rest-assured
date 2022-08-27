package test;

import builder.IssueContentBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.RequestCapability;
import org.apache.commons.lang3.RandomStringUtils;
import utils.AuthenticationHandler;
import utils.ProjectInfo;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class VerifyIssues implements RequestCapability {

    public static void main(String[] args) {


    String baseUri = "https://phamvanhai-jira.atlassian.net";
    String apiPath = "/rest/api/3/issue";
    String projectKey = "RAA";

    String email = "qa.haipv6@gmail.com";
    String apiToken = "OqAXeSeIX2toPFWWzSTlA075";
    String encodedCredStr = AuthenticationHandler.encodedCredStr(email, apiToken);

    //Request header
    RequestSpecification request = given();
    request.baseUri(baseUri);
    request.header(defaultHeader);
    request.header(acceptJSONHeader);
    request.header(RequestCapability.getAuthenticatedHeader(encodedCredStr));

    ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
    String taskTypeId = projectInfo.getIssueTypeId("task");

    //String summary = "This is new bug #4";
    int desiredLength = 20;
    boolean hasLetters = true;
    boolean hasNumbers = true;
    String randomSummary = RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);

    IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
    String issueFieldsContent = issueContentBuilder.build(randomSummary, taskTypeId, projectKey);

    // CREATE ISSUE
    Response response = request.body(issueFieldsContent).post(apiPath);
    //response.prettyPrint();

    // get method to show issue details
    Map<String, String> responseBody = JsonPath.from(response.asString()).get();
    String apiIssuePath = "/rest/api/3/issue/" + responseBody.get("key");

    // READ ISSUE JUST CREATED
    response = request.get(apiIssuePath);
    response.prettyPrint();

    IssueFields issueFields = issueContentBuilder.getIssueFields();
    String expectedSummary = issueFields.getFields().getSummary();
    String expectedStatus = "To Do";

    Map<String, Object> fields = JsonPath.from(response.getBody().asString()).get("fields");
    String actualSummary = fields.get("summary").toString();
    Map<String, Object> status = (Map<String, Object>) fields.get("status");
    Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
    String actualStatus = (String) statusCategory.get("name");

    System.out.println(expectedSummary);
    System.out.println(actualSummary);

    System.out.println(expectedStatus);
    System.out.println(actualStatus);


    }
}
