package test;

import api_flow.IssueFlow;
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

public class FullCRUDIsuueOK implements RequestCapability {

    public static void main(String[] args) {

        String baseUri = "https://phamvanhai-jira.atlassian.net";
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

        IssueFlow issueFlow = new IssueFlow(request, baseUri, projectKey, "task");
        issueFlow.createIssue();
        issueFlow.verifyIssueDetails();
        issueFlow.updateIssue("Done");
        issueFlow.verifyIssueDetails();
        issueFlow.deleteIssue();

    }
}
