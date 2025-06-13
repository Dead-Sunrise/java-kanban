package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import controller.DurationTypeAdapter;
import controller.LocalDateTimeAdapter;
import controller.TaskManager;
import taskmanagement.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {

    private Gson gson;
    String[] compoundPath;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        compoundPath = httpExchange.getRequestURI().getPath().split("/");
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                requestGet(httpExchange);
                break;
            case "POST":
                requestPost(httpExchange);
                break;
            case "DELETE":
                requestDelete(httpExchange);
                break;
            default:
                sendNotFound(httpExchange, "Неверный метод запроса.");
        }
    }

    public void requestGet(HttpExchange httpExchange) throws IOException {
        if (compoundPath[1].equals("tasks")) {
            if (compoundPath.length == 2) {
                if (taskManager.getAllTasks().isEmpty()) {
                    sendText(httpExchange, "Список задач пуст.");
                } else {
                    sendText(httpExchange, gson.toJson(taskManager.getAllTasks()));
                }
            } else {
                try {
                    Optional<Task> task = taskManager.getTaskById(Integer.parseInt(compoundPath[2]));
                    if (task.isPresent()) {
                        sendText(httpExchange, gson.toJson(task.get()));
                    } else {
                        sendNotFound(httpExchange, "Задача с таким id не найдена.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id задачи должен быть в цифоровом формате.");
                }
            }
        } else {
            sendNotFound(httpExchange, "Неверный путь.");
        }
    }

    public void requestPost(HttpExchange httpExchange) throws IOException {
        String json = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(json, Task.class);
        if (!taskManager.intersection(task)) {
            if (taskManager.getTaskById(task.getId()).isEmpty()) {
                taskManager.createTask(task);
                sendSuccess(httpExchange, 201, "Задача добавлена.");
            } else {
                taskManager.updateTask(task, task.getId(), task.getStatus());
                sendSuccess(httpExchange, 201, "Задача обновлена.");
            }
        } else {
            sendHasInteractions(httpExchange, "Время выполнения задачи пересекается с другими задачами.");
        }
    }

    public void requestDelete(HttpExchange httpExchange) throws IOException {
        if (compoundPath[1].equals("tasks")) {
            if (compoundPath.length == 2) {
                taskManager.deleteAllTasks();
                sendText(httpExchange, "Все задачи удалены");
            } else {
                try {
                    Optional<Task> task = taskManager.getTaskById(Integer.parseInt(compoundPath[2]));
                    if (task.isPresent()) {
                        taskManager.deleteTaskById(Integer.parseInt(compoundPath[2]));
                        sendText(httpExchange, "Задача удалена.");
                    } else {
                        sendNotFound(httpExchange, "Задача с таким id не найдена.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id задачи должен быть в цифоровом формате.");
                }
            }
        }
    }
}