package test;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.RequestCapability;
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
    System.out.println(taskTypeId);

    String summary = "This is new bug";
    IssueFields.IssueType issueType = new IssueFields.IssueType(taskTypeId);
    IssueFields.Project project = new IssueFields.Project("RAA");
    IssueFields.Fields fields = new IssueFields.Fields(summary, issueType, project);
    IssueFields issueFields = new IssueFields(fields);

   // System.out.println(new Gson().toJson(issueFields));

    Response response = request.body(new Gson().toJson(issueFields)).post(apiPath);
    response.prettyPrint();


    }
}
