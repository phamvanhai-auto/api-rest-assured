package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProjectInfo implements RequestCapability{

    private String baseUri;
    private String projectKey;
    private List<Map<String, String>> issueTypes;
    private Map<String, List<Map<String, String>>> projectInfo;


    public ProjectInfo(String baseUri, String projectKey) {
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        getProjectInfo();
    }

    public String getIssueTypeId(String issueTypeStr){
       getIssueTypes();
        String issueTypeId = null;

        for (Map<String, String> issueType : issueTypes) {
            if(issueType.get("name").equalsIgnoreCase(issueTypeStr)){
                issueTypeId = issueType.get("id");
                break;
            }
        }
        if (issueTypeId == null){
            throw new RuntimeException("[Err] Can't not find the id " + issueTypeStr);
        }
        return issueTypeId;
    }

    private void getIssueTypes(){
        issueTypes = projectInfo.get("issueTypes");
    }

    private void getProjectInfo(){

        String apiPath = "/rest/api/3/project/".concat(projectKey);

        String email = "haipv6@gmail.com";
        String apiToken = "OqAXeSeIX2toPFWWzSTlA075";
        String cred = email.concat(":").concat(apiToken);
        byte[] encodeCred = Base64.encodeBase64(cred.getBytes(StandardCharsets.UTF_8));
        String encodedCredStr = new String(encodeCred);

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(RequestCapability.getAuthenticatedHeader(encodedCredStr));

        Response response = request.get(apiPath);
        //response.prettyPrint();

        projectInfo = JsonPath.from(response.asString()).get();
    }

}
