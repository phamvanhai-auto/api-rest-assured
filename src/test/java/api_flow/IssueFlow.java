package api_flow;

import builder.BodyJSONBuilder;
import builder.IssueContentBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.IssueTransition;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueFlow {
    private static Map<String, String> transitionTypeMap = new HashMap<>();
    private static String issuePathPrefix = "/rest/api/3/issue/";
    private RequestSpecification request;
    private String baseUri;
    private Response response;
    private String createdIssueKey;
    private String projectKey;
    private String issueTypeStr;
    private IssueFields issueFields;
    private String status;

    static {
        transitionTypeMap.put("11", "To Do");
        transitionTypeMap.put("21", "In Progress");
        transitionTypeMap.put("31", "Done");

    }
    public IssueFlow(RequestSpecification request, String baseUri, String projectKey, String issueTypeStr) {
        this.request = request;
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        this.issueTypeStr = issueTypeStr;
        this.status = "To Do";
    }

    public void createIssue(){
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId(issueTypeStr);

        //String summary = "This is new bug #4";
        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = true;
        String randomSummary = RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);

        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String issueFieldsContent = issueContentBuilder.build(randomSummary, taskTypeId, projectKey);
        issueFields = issueContentBuilder.getIssueFields();
        this.response = request.body(issueFieldsContent).post(issuePathPrefix);

        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        createdIssueKey = responseBody.get("key");
    }

    public void verifyIssueDetails(){
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = status;

        Map<String, String> issueInfo = getIssueInfo();
        String actualSummary = issueInfo.get("summary");
        String actualStatus = issueInfo.get("status");

        System.out.println(expectedSummary);
        System.out.println(actualSummary);

        System.out.println(expectedStatus);
        System.out.println(actualStatus);

    }

    private Map<String, String> getIssueInfo(){
        String apiIssuePath = issuePathPrefix + createdIssueKey;
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

    }

    public void updateIssue(String issueStatusStr){
        String targetTransitionId = null;
        for (String transitionId : transitionTypeMap.keySet()) {
            if(transitionTypeMap.get(transitionId).equalsIgnoreCase(issueStatusStr)){
                targetTransitionId = transitionId;
                break;
            }
            if(targetTransitionId == null){
                throw new RuntimeException("[ERR] issue status string not supported");
            }
        }
        //Transition issue
        String apiTransitionPath = issuePathPrefix + createdIssueKey + "/transitions";
        IssueTransition.Transition transition = new IssueTransition.Transition(targetTransitionId);
        IssueTransition issueTransition = new IssueTransition(transition);

        String transitionBody = BodyJSONBuilder.getJSONContent(issueTransition);
        request.body(transitionBody).post(apiTransitionPath).then().statusCode(204);

        Map<String, String> issueInfo = getIssueInfo();
        String actualIssueStatus = issueInfo.get("status");
        String expectedIssueStatus = transitionTypeMap.get(targetTransitionId);
        System.out.println(actualIssueStatus);
        System.out.println(expectedIssueStatus);

    }

    public void deleteIssue(){
        String apiDeletePath = issuePathPrefix + createdIssueKey;
        request.delete(apiDeletePath);

        //getIssue to verify delete successfully
        String apiIssuePath = issuePathPrefix + createdIssueKey;
        response = request.get(apiIssuePath);
        response.prettyPrint();
        Map<String, Object> errorReturn = JsonPath.from(response.body().asString()).get();
        List<String> errorMessages = (List<String>) errorReturn.get("errorMessages");
        System.out.println(errorMessages.get(0));

    }
}
