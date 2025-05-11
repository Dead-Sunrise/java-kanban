package controller;

import taskmanagement.*;

import java.io.*;
import java.nio.file.Path;

import static java.lang.Integer.parseInt;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public Path path;
    public String firstString = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public static Task fromString(String value) {
        String[] parameters = value.split(",");
        int id = parseInt(parameters[0]);
        Status status = Status.valueOf(parameters[3]);
        TaskType type = TaskType.valueOf(parameters[1]);
        Task taskFromString = null;
        if (type == TaskType.TASK) {
            taskFromString = new Task(parameters[2], parameters[4]);
            taskFromString.setId(id);
            taskFromString.setStatus(status);
        } else if (type == TaskType.EPIC) {
            taskFromString = new Epic(parameters[2], parameters[4]);
            taskFromString.setId(id);
            taskFromString.setStatus(status);
        } else if (type == TaskType.SUBTASK) {
            taskFromString = new SubTask(parameters[2], parameters[4]);
            taskFromString.setId(id);
            taskFromString.setStatus(status);
            ((SubTask) taskFromString).setEpicId(parseInt(parameters[5]));
        }
        return taskFromString;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            fileWriter.write(firstString);
            for (Integer key : tasks.keySet()) {
                fileWriter.write(tasks.get(key).toString() + "\n");
            }
            for (Integer key : epics.keySet()) {
                fileWriter.write(epics.get(key).toString() + "\n");
            }
            for (Integer key : subTasks.keySet()) {
                fileWriter.write(subTasks.get(key).toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                Task task = fromString(br.readLine());
                if (task.getTaskType() == TaskType.TASK) {
                    fileBackedTaskManager.createTask(task, task.getStatus());
                } else if (task.getTaskType() == TaskType.EPIC) {
                    fileBackedTaskManager.createEpic((Epic) task);
                } else if (task.getTaskType() == TaskType.SUBTASK) {
                    fileBackedTaskManager.createSubTask((SubTask) task, ((SubTask) task).getEpicId(), task.getStatus());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла.");
        }
        return fileBackedTaskManager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void createTask(Task task, Status status) {
        super.createTask(task, status);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask, int epicId, Status status) {
        super.createSubTask(subTask, epicId, status);
        save();
    }

    @Override
    public void updateTask(Task task, int taskId, Status status) {
        super.updateTask(task, taskId, status);
        save();
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        super.updateEpic(epic, epicId);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask, int epicId, int id, Status status) {
        super.updateSubTask(subTask, epicId, id, status);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }
}
