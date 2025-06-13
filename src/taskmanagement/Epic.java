package taskmanagement;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(ArrayList<SubTask> newSubTaskList) {
        subTaskList = newSubTaskList;
    }

    public void addToSubTask(SubTask subTask) {
        if (subTask.getId() == this.getId()) {
            return;
        }
        subTaskList.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        subTaskList.remove(subTask);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
