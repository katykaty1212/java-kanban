package task;

import java.util.Objects;

public class Task {
    private String title;//название
    private String description;//описание
    private int id;//айди номер
    private Status status; // статус

    public Task(String title, String description) { //тут добавляется задача
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%d,TASK,%s,%s,%s\n", id, title, status, description);
    }
}