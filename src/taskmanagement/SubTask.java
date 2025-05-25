package taskmanagement;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
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
