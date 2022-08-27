package model;

public class IssueFields {

    private Fields fields;

    public IssueFields(Fields fields) {
        this.fields = fields;
    }
    public Fields getFields() {
        return fields;
    }

    public static class Fields {
        private String summary;
        private IssueType issuetype;
        private Project project;

        public Fields(String summary, IssueType issuetype, Project project) {
            this.summary = summary;
            this.issuetype = issuetype;
            this.project = project;
        }

        public String getSummary() {
            return summary;
        }

        public IssueType getIssuetype() {
            return issuetype;
        }

        public Project getProject() {
            return project;
        }
    }
    public static class IssueType {
        private String id;

        public IssueType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
    public static class Project {
        private String key;

        public Project(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
