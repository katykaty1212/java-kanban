package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String title;//название
    private String description;//описание
    private int id;//айди номер
    private Status status; // статус
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description) { //тут добавляется задача
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String title, String description, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.status = Status.NEW;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
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
        return String.format("%d,TASK,%s,%s,%s,%s,%s\n", id, title, status, description, duration, startTime);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}