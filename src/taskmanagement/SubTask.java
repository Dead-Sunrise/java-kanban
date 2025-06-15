package taskmanagement;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status,
                   int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int id) {
        epicId = id;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return getId() + "," + getTaskType() + "," + getName() + "," + getStatus() + "," + getDescription() +
                "," + getStartTime() + "," + getDuration() + "," + getEndTime() + "," + getEpicId();
    }
}
