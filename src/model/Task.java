package model;

import java.util.Objects;

public class Task {
    static int uniqId = 1;

    private int id;
    private String status = Status.NEW.toString();
    protected  String description;
    protected String name;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        id = uniqId;
        uniqId++;
    }

    public static void setUniqId(int id) {
        uniqId = id;
    }

    public static int getUniqId() {
        return uniqId;
    }

    public Epic getEpic() {
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(Status.DONE.toString())
                || status.equals(Status.NEW.toString())
                || status.equals(Status.IN_PROGRESS.toString())) {
            this.status = status;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id == task.id && Objects.equals(status, task.status)
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
