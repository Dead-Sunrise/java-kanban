package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import controller.DurationTypeAdapter;
import controller.LocalDateTimeAdapter;
import controller.TaskManager;
import taskmanagement.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler {

    private Gson gson;
    String[] compoundPath;

    public SubTaskHandler(TaskManager taskManager, Gson gson) {
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
        if (compoundPath[1].equals("subtasks")) {
            if (compoundPath.length == 2) {
                if (taskManager.getAllSubTasks().isEmpty()) {
                    sendText(httpExchange, "Список подзадач пуст.");
                } else {
                    sendText(httpExchange, gson.toJson(taskManager.getAllSubTasks()));
                }
            } else {
                try {
                    Optional<SubTask> subTask = taskManager.getSubTaskById(Integer.parseInt(compoundPath[2]));
                    if (subTask.isPresent()) {
                        sendText(httpExchange, gson.toJson(subTask.get()));
                    } else {
                        sendNotFound(httpExchange, "Подадача с таким id не найдена.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id подзадачи должен быть в цифоровом формате.");
                }
            }
        }
    }

    public void requestPost(HttpExchange httpExchange) throws IOException {
        String json = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(json, SubTask.class);
        if (!taskManager.getEpicById(subTask.getEpicId()).isEmpty()) {
            if (!taskManager.intersection(subTask)) {
                if (taskManager.getSubTaskById(subTask.getId()).isEmpty()) {
                    taskManager.createSubTask(subTask);
                    sendSuccess(httpExchange, 201, "Подзадача успешно добавлена.");
                } else {
                    taskManager.updateSubTask(subTask, subTask.getEpicId(), subTask.getId(), subTask.getStatus());
                    sendSuccess(httpExchange, 201, "Подзадача успешно обновлена");
                }
            } else {
                sendHasInteractions(httpExchange, "Время выполнения подзадачи пересекается с другими задачами.");
            }
        } else {
            sendNotFound(httpExchange, "Не найден эпик для подзадачи.");
        }
    }

    public void requestDelete(HttpExchange httpExchange) throws IOException {
        if (compoundPath[1].equals("subtasks")) {
            if (compoundPath.length == 2) {
                taskManager.deleteAllSubTasks();
                sendText(httpExchange, "Все подзадачи удалены");
            } else {
                try {
                    Optional<SubTask> subTask = taskManager.getSubTaskById(Integer.parseInt(compoundPath[2]));
                    if (subTask.isPresent()) {
                        taskManager.deleteSubTaskById(Integer.parseInt(compoundPath[2]));
                        sendText(httpExchange, "Подзадача удалена.");
                    } else {
                        sendNotFound(httpExchange, "Подзадача с таким id не найдена.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id подзадачи должен быть в цифоровом формате.");
                }
            }
        }
    }
}
