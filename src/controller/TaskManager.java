package controller;

import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.util.*;

public interface TaskManager {
    Map<Integer, Task> getAllTasks();

    Map<Integer, Epic> getAllEpics();

    Map<Integer, SubTask> getAllSubTasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    void createTask(Task task, Status status);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask, int epicId, Status status);

    void updateTask(Task task, int taskId, Status status);

    void updateEpic(Epic epic, int epicId);

    void updateSubTask(SubTask subTask, int epicId, int id, Status status);

    Optional<Task> getTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Optional<SubTask> getSubTaskById(int id);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    Status setEpicStatus(int id);

    ArrayList<SubTask> getAllEpicSubTasks(int id);

    public List<Task> getHistory();

    public Set<Task> getPrioritizedTasks();

    public boolean intersection(Task task);
}
