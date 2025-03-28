package controller;

import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, SubTask> subTasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    int currentId = 0;

    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    public Map<Integer, SubTask> getAllSubTasks() {
        return subTasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Integer keys : epics.keySet()) {
            Epic epic = epics.get(keys);
            epic.setStatus(Status.NEW);
            epic.addToSubTask(null);
            epics.put(keys, epic);
        }
    }

    public void createTask(Task task, Status status) {
        task.setId(currentId++);
        task.setStatus(status);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(currentId++);
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask, int epicId, Status status) {
        subTask.setId(currentId++);
        subTask.setStatus(status);
        subTask.setEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        if (epics.containsKey(subTask.getEpicId())) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.addToSubTask(subTask);
            epic.setStatus(setEpicStatus(epicId));
            epics.put(epicId, epic);
        } else {
            System.out.println("Не удалось добавить подзадачу в Эпик, Эпика с таким id нет.");
        }
    }

    public void updateTask(Task task, int taskId, Status status) {
        task.setId(taskId);
        task.setStatus(status);
        tasks.put(taskId, task);
    }

    public void updateEpic(Epic epic, int epicId) {
        epic.setId(epicId);
        epics.put(epicId, epic);
    }

    public void updateSubTask(SubTask subTask, int epicId, int id, Status status) {
        subTask.setId(id);
        subTask.setEpicId(epicId);
        subTask.setStatus(status);
        SubTask oldSubTask = subTasks.get(id);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(epicId);
        epic.setStatus(setEpicStatus(epicId));
        epic.deleteSubTask(oldSubTask);
        epic.addToSubTask(subTask);
        epics.put(epicId, epic);
    }

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача с таким Id отсутствует");
            return null;
        }
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик с таким id отсутствует");
            return null;
        }
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Подзадача с таким id отсутствует");
            return null;
        }
        return subTasks.get(id);
    }

    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача с таким Id отсутствует");
        }
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<SubTask> subTasksForDelete = epic.getSubTaskList();
            for (SubTask subTask : subTasksForDelete) {
                subTasks.values().remove(subTask);
            }
            epics.remove(id);

        } else {
            System.out.println("Эпик с таким Id отсутствует");
        }
    }

    public void deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Подзадача с таким Id отсутствует");
        } else {
            SubTask subTask = subTasks.get(id);
            int epicId = subTask.getEpicId();
            subTasks.remove(id);
            Epic epic = epics.get(epicId);
            epic.setStatus(setEpicStatus(epicId));
            epic.deleteSubTask(subTask);
            epics.put(epicId, epic);
        }
    }

    public Status setEpicStatus(int id) {
        int count = 0;
        int newCount = 0;
        int doneCount = 0;
        for (Integer key : subTasks.keySet()) {
            SubTask subTask = subTasks.get(key);
            if (subTask.getEpicId() == id) {
                count++;
                if (subTask.getStatus() == Status.IN_PROGRESS) {
                    newCount++;
                } else if (subTask.getStatus() == Status.DONE) {
                    doneCount++;
                }
            }
        }
        if (count == 0) {
            return Status.NEW;
        }
        if (count == doneCount) {
            return Status.DONE;
        }
        if (newCount != 0) {
            return Status.IN_PROGRESS;
        }
        return Status.NEW;
    }

    public ArrayList<SubTask> getAllEpicSubTasks(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            System.out.println(epic.getSubTaskList());
            return epic.getSubTaskList();
        } else {
            System.out.println("Эпик с таким Id отсутствует");
            return new ArrayList<>();
        }
    }
}
