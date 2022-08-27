package builder;

import model.IssueFields;

public class IssueContentBuilder {

    IssueFields issueFields;
    public String build(String summary, String taskTypeId, String projectKey){

        IssueFields.IssueType issueType = new IssueFields.IssueType(taskTypeId);
        IssueFields.Project project = new IssueFields.Project(projectKey);
        IssueFields.Fields fields = new IssueFields.Fields(summary, issueType, project);
        issueFields = new IssueFields(fields);

        return BodyJSONBuilder.getJSONContent(issueFields);
    }

    public IssueFields getIssueFields() {
        return issueFields;
    }
}
