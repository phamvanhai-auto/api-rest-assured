package test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraIssueTypes implements RequestCapability {

    public static void main(String[] args) {

        String baseUri = "https://phamvanhai-jira.atlassian.net";
        String apiPath = "/rest/api/3/project/RAA";

        String email = "qa.haipv6@gmail.com";
        String apiToken = "OqAXeSeIX2toPFWWzSTlA075";
        String cred = email.concat(":").concat(apiToken);
        byte[] encodeCred = Base64.encodeBase64(cred.getBytes(StandardCharsets.UTF_8));
        String encodedCredStr = new String(encodeCred);

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(RequestCapability.getAuthenticatedHeader(encodedCredStr));
        //request.header(getAuthenticatedHeader.apply(encodedCredStr));

        Response response = request.get(apiPath);
        response.prettyPrint();

        Map<String, Object> projectInfo = JsonPath.from(response.asString()).get();
        List<Map<String, String>> issueTypes = (List<Map<String, String>>) projectInfo.get("issueTypes");

        for (Map<String, String> issueType : issueTypes) {
            System.out.println(issueType.get("id"));
            System.out.println(issueType.get("name"));
            System.out.println("====");
        }
    }

}
