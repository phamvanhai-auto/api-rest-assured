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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.restassured.RestAssured.given;

public class FullCRUDIsuue implements RequestCapability {

    public static void main(String[] args) {


    String baseUri = "https://phamvanhai-jira.atlassian.net";
    String apiCreateIssuePath = "/rest/api/3/issue";
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
    Response response = request.body(issueFieldsContent).post(apiCreateIssuePath);
    //response.prettyPrint();

    // return body content
    Map<String, String> responseBody = JsonPath.from(response.asString()).get();
    final String ISSUE_KEY = responseBody.get("key");

    IssueFields issueFields = issueContentBuilder.getIssueFields();
    String expectedSummary = issueFields.getFields().getSummary();
    String expectedStatus = "To Do";

    Function<String, Map<String, String>> getIssueInfo = issueKey -> {
        String apiIssuePath = "/rest/api/3/issue/" + issueKey;
        // READ ISSUE JUST CREATED
        Response response_1 = request.get(apiIssuePath);
        //response_1.prettyPrint();

        Map<String, Object> fields = JsonPath.from(response_1.getBody().asString()).get("fields");
        String actualSummary = fields.get("summary").toString();
        Map<String, Object> status = (Map<String, Object>) fields.get("status");
        Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
        String actualStatus = (String) statusCategory.get("name");

        Map<String, String> issueInfo = new HashMap<>();
        issueInfo.put("summary", actualSummary);
        issueInfo.put("status", actualStatus);
        return issueInfo;
    };

    Map<String, String> issueInfo = getIssueInfo.apply(ISSUE_KEY);

    System.out.println(expectedSummary);
    System.out.println(issueInfo.get("summary"));

    System.out.println(expectedStatus);
    System.out.println(issueInfo.get("status"));

    //UPDATE ISSUE
        //Get transitions id
    String apiTransitionIdPath = "/rest/api/3/issue/" + ISSUE_KEY + "/transitions";
    request.get(apiTransitionIdPath);
    final String DONE_TRANSITION_ID = "31";

    String trasitionBody = "{\n" +
            "  \"transition\": {\n" +
            "    \"id\": \"21\"\n" +
            "  }\n" +
            "}";

        //Transition issue
    String apiTransitionPath = "/rest/api/3/issue/" + ISSUE_KEY + "/transitions";
    request.body(trasitionBody).post(apiTransitionPath).then().statusCode(204);
    issueInfo = getIssueInfo.apply(ISSUE_KEY);
    String lastestIssueStatus = issueInfo.get("status");
    System.out.println(lastestIssueStatus);

    //DELETE ISSUE
    String apiDeletePath = "/rest/api/3/issue/" + ISSUE_KEY;
    request.delete(apiDeletePath);

        //getIssue to verify delete successfully
    String apiIssuePath = "/rest/api/3/issue/" + ISSUE_KEY;
    response = request.get(apiIssuePath);
    Map<String, Object> errorReturn = JsonPath.from(response.body().asString()).get();
    List<String> errorMessages = (List<String>) errorReturn.get("errorMessages");
    System.out.println(errorMessages.get(0));
    }
}
