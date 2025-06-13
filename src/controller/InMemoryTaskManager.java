package controller;

import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, SubTask> subTasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    Set<Task> priorityList = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    int currentId = 0;
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    @Override
    public Map<Integer, SubTask> getAllSubTasks() {
        return subTasks;
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().stream().forEach(key -> {
            priorityList.remove(tasks.get(key));
            historyManager.remove(key);
        });
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubTasks();
        epics.keySet().stream().forEach(key ->
                historyManager.remove(key));
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.keySet().stream().forEach(key -> {
            priorityList.remove(subTasks.get(key));
            historyManager.remove(key);
        });
        subTasks.clear();
        epics.values().stream().forEach(epic -> {
            epic.setStatus(Status.NEW);
            epic.setSubTaskList(new ArrayList<>());
            epicSetTime(epic);
            epics.put(epic.getId(), epic);
        });
    }

    @Override
    public void createTask(Task task) {
        if (intersection(task)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else {
            task.setId(currentId++);
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                priorityList.add(task);
            }
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(currentId++);
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (intersection(subTask)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else {
            subTask.setId(currentId++);
            subTasks.put(subTask.getId(), subTask);
            if (subTask.getStartTime() != null) {
                priorityList.add(subTask);
            }
            int epicId = subTask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.addToSubTask(subTask);
            epic.setStatus(setEpicStatus(epicId));
            epicSetTime(epic);
            epics.put(epicId, epic);
        }
    }

    @Override
    public void updateTask(Task task, int taskId, Status status) {
        deleteTaskById(taskId);
        if (intersection(task)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else {
            task.setId(taskId);
            task.setStatus(status);
            tasks.put(taskId, task);
        }
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        epic.setId(epicId);
        epics.put(epicId, epic);
    }

    @Override
    public void updateSubTask(SubTask subTask, int epicId, int id, Status status) {
        deleteSubTaskById(id);
        if (intersection(subTask)) {
            System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else {
            subTask.setId(id);
            subTask.setEpicId(epicId);
            subTask.setStatus(status);
            SubTask oldSubTask = subTasks.get(id);
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(epicId);
            epic.setStatus(setEpicStatus(epicId));
            epic.deleteSubTask(oldSubTask);
            epic.addToSubTask(subTask);
            epicSetTime(epic);
            epics.put(epicId, epic);
        }
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            return Optional.empty();
        } else {
            historyManager.add(tasks.get(id));
            return Optional.of(tasks.get(id));
        }
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        if (!epics.containsKey(id)) {
            return Optional.empty();
        } else {
            historyManager.add(epics.get(id));
            return Optional.of(epics.get(id));
        }
    }

    @Override
    public Optional<SubTask> getSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return Optional.empty();
        } else {
            historyManager.add(subTasks.get(id));
            return Optional.of(subTasks.get(id));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача с таким Id отсутствует");
        }
        priorityList.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<SubTask> subTasksForDelete = epic.getSubTaskList();
            for (SubTask subTask : subTasksForDelete) {
                subTasks.values().remove(subTask);
                priorityList.remove(subTask);
            }
            epics.remove(id);
            historyManager.remove(id);

        } else {
            System.out.println("Эпик с таким Id отсутствует");
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Подзадача с таким Id отсутствует");
        } else {
            SubTask subTask = subTasks.get(id);
            int epicId = subTask.getEpicId();
            historyManager.remove(id);
            priorityList.remove(subTasks.get(id));
            subTasks.remove(id);
            Epic epic = epics.get(epicId);
            epic.setStatus(setEpicStatus(epicId));
            epic.deleteSubTask(subTask);
            epicSetTime(epic);
            epics.put(epicId, epic);
        }
    }

    @Override
    public Status setEpicStatus(int id) {
        boolean isNew = false;
        boolean isDone = false;
        boolean isInProgress = false;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                switch (subTask.getStatus()) {
                    case NEW -> isNew = true;
                    case IN_PROGRESS -> isInProgress = true;
                    case DONE -> isDone = true;
                }
            }
        }
        if (isInProgress) {
            return Status.IN_PROGRESS;
        } else if (isNew && isDone) {
            return Status.IN_PROGRESS;
        } else if (isDone) {
            return Status.DONE;
        } else {
            return Status.NEW;
        }
    }

    @Override
    public ArrayList<SubTask> getAllEpicSubTasks(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic.getSubTaskList();
        } else {
            System.out.println("Эпик с таким Id отсутствует");
            return new ArrayList<>();
        }
    }

    public void epicSetTime(Epic epic) {
        List<SubTask> subTaskList = epic.getSubTaskList();
        if (subTaskList.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        }
        LocalDateTime startTime = subTaskList.stream()
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        epic.setStartTime(startTime);
        LocalDateTime endTime = subTaskList.stream()
                .map(SubTask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        epic.setEndTime(endTime);
        Duration duration = Duration.ZERO;
        for (SubTask subTask : subTaskList) {
            duration = duration.plus(subTask.getDuration());
        }
        epic.setDuration(duration);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return priorityList;
    }

    @Override
    public boolean intersection(Task task) {
        Set<Task> tasks = getPrioritizedTasks();
        boolean intersection = false;
        if (task.getStartTime() == null) {
            intersection = false;
        } else if (tasks.isEmpty()) {
            intersection = false;
        } else {
            for (Task t : tasks) {
                LocalDateTime taskStart = task.getStartTime();
                LocalDateTime taskEnd = task.getEndTime();
                LocalDateTime taskFromListStart = t.getStartTime();
                LocalDateTime taskFromListEnd = t.getEndTime();
                if ((taskStart.isAfter(taskFromListStart) && taskStart.isBefore(taskFromListEnd)) ||
                        (taskEnd.isAfter(taskFromListStart) && taskEnd.isBefore(taskFromListEnd)) ||
                        (taskStart.isBefore(taskFromListStart) && taskEnd.isAfter(taskFromListEnd)) ||
                        (taskStart.isAfter(taskFromListStart) && taskEnd.isBefore(taskFromListEnd)) ||
                        taskStart.isEqual(taskFromListStart) ||
                        taskStart.isEqual(taskFromListEnd) ||
                        taskEnd.isEqual(taskFromListStart) ||
                        taskEnd.isEqual(taskFromListEnd)) {
                    intersection = true;
                } else {
                    intersection = false;
                }
            }
        }
        return intersection;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}