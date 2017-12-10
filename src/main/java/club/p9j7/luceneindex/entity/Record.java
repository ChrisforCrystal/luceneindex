package club.p9j7.luceneindex.entity;

/**
 * 查询到的某条记录
 */
public class Record {
    private int id;
    private String path;
    private String content;

    public Record(int id, String path, String content) {
        this.id = id;
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
