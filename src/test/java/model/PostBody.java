package model;

public class PostBody {

    int userId;
    int Id;
    String title;
    String body;

    public PostBody() {
    }

    public PostBody(int userId, int id, String title, String body) {
        this.userId = userId;
        Id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "PostBody{" +
                "userId=" + userId +
                ", Id=" + Id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
