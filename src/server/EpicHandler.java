package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import controller.DurationTypeAdapter;
import controller.LocalDateTimeAdapter;
import controller.TaskManager;
import taskmanagement.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {

    private Gson gson;
    String[] compoundPath;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
        if (compoundPath[1].equals("epics")) {
            if (compoundPath.length == 2) {
                if (taskManager.getAllEpics().isEmpty()) {
                    sendText(httpExchange, "Список эпиков пуст.");
                } else {
                    sendText(httpExchange, gson.toJson(taskManager.getAllEpics()));
                }
            } else if (compoundPath.length == 3) {
                try {
                    Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(compoundPath[2]));
                    if (epic.isPresent()) {
                        sendText(httpExchange, gson.toJson(epic.get()));
                    } else {
                        sendNotFound(httpExchange, "Эпик с таким id не найден.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id эпика должен быть в цифоровом формате.");
                }
            } else if (compoundPath[3].equals("subtasks")) {
                try {
                    Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(compoundPath[2]));
                    if (epic.isPresent()) {
                        if (epic.get().getSubTaskList().isEmpty()) {
                            sendText(httpExchange, "Список подзадач эпика пуст.");
                        } else {
                            sendText(httpExchange, gson.toJson(epic.get().getSubTaskList()));
                        }
                    } else {
                        sendNotFound(httpExchange, "Эпик с таким id не найден.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id эпика должен быть в цифоровом формате.");
                }
            } else {
                sendNotFound(httpExchange, "Неверный путь запроса.");
            }
        }
    }

    public void requestPost(HttpExchange httpExchange) throws IOException {
        String json = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(json, Epic.class);
        if (taskManager.getEpicById(epic.getId()).isEmpty()) {
            taskManager.createEpic(epic);
            sendSuccess(httpExchange, 201, "Эпик успешно добавлен.");
        } else {
            taskManager.updateEpic(epic, epic.getId());
            sendSuccess(httpExchange, 201, "Эпик успешно обновлен");
        }
    }

    public void requestDelete(HttpExchange httpExchange) throws IOException {
        if (compoundPath[1].equals("epics")) {
            if (compoundPath.length == 2) {
                taskManager.deleteAllEpics();
                sendText(httpExchange, "Все эпики удалены");
            } else {
                try {
                    Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(compoundPath[2]));
                    if (epic.isPresent()) {
                        taskManager.deleteEpicById(Integer.parseInt(compoundPath[2]));
                        sendText(httpExchange, "Эпик удален.");
                    } else {
                        sendNotFound(httpExchange, "Эпик с таким id не найден.");
                    }
                } catch (NumberFormatException e) {
                    sendNotFound(httpExchange, "id эпика должен быть в цифоровом формате.");
                }
            }
        }
    }
}