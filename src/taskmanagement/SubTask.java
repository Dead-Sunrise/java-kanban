package taskmanagement;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);

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
        return getId() + "," + getTaskType() + "," + getName() + "," + getStatus() +
                "," + getDescription() + "," + getEpicId();
    }
}
