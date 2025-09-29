package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public String toString() {
        return String.format("%d,SUBTASK,%s,%s,%s,%d,%s,%s\n",
                getId(), getTitle(), getStatus(),getDescription(), epicId, getDuration(), getStartTime());
    }
}