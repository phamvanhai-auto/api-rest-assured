package test;

import api_flow.IssueFlow;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.testng.annotations.Test;
import utils.AuthenticationHandler;

import static io.restassured.RestAssured.given;

public class FullCRUDIsuueTestNG extends BaseTest {

    @Test
    public void Full_API_Flow() {
        IssueFlow issueFlow = new IssueFlow(request, baseUri, projectKey, "task");
        issueFlow.createIssue();
        issueFlow.verifyIssueDetails();
        issueFlow.updateIssue("Done");
        issueFlow.verifyIssueDetails();
        issueFlow.deleteIssue();
    }

    @Test
    public void Create_Issue() {
        IssueFlow issueFlow = new IssueFlow(request, baseUri, projectKey, "task");
        issueFlow.createIssue();
        issueFlow.verifyIssueDetails();
    }

    @Test
    public void Update_Issue() {
        IssueFlow issueFlow = new IssueFlow(request, baseUri, projectKey, "task");
        issueFlow.createIssue();
        issueFlow.updateIssue("Done");
        issueFlow.verifyIssueDetails();
    }

}
