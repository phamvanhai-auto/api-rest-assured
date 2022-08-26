package test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;
import utils.ProjectInfo;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraIssueTypesOK implements RequestCapability {

    public static void main(String[] args) {

        String baseUri = "https://phamvanhai-jira.atlassian.net";
        String projectKey = "RAA";

        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        System.out.println("Task ID: " +projectInfo.getIssueTypeId("task"));
    }

}
