package test;

import builder.BodyJSONBuilder;
import builder.IssueContentBuilder;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.RequestCapability;
import org.apache.commons.lang3.RandomStringUtils;
import utils.AuthenticationHandler;
import utils.ProjectInfo;

import static io.restassured.RestAssured.given;

public class JiraNewIssue implements RequestCapability {

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

//    IssueFields.IssueType issueType = new IssueFields.IssueType(taskTypeId);
//    IssueFields.Project project = new IssueFields.Project("RAA");
//    IssueFields.Fields fields = new IssueFields.Fields(summary, issueType, project);
//    IssueFields issueFields = new IssueFields(fields);
    IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
    String issueFieldsContent = issueContentBuilder.build(randomSummary, taskTypeId, projectKey);

    //Response response = request.body(new Gson().toJson(issueFields)).post(apiPath);

    //Response response = request.body(BodyJSONBuilder.getJSONContent(issueFields)).post(apiPath);

    //????ng g??i g???i h??m
    Response response = request.body(issueFieldsContent).post(apiPath);
    response.prettyPrint();


    }
}
