package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import controller.DurationTypeAdapter;
import controller.LocalDateTimeAdapter;
import controller.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class PrioritizedTaskHandler extends BaseHttpHandler {
    private final Gson gson;

    public PrioritizedTaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            if (taskManager.getPrioritizedTasks().isEmpty()) {
                sendText(httpExchange, "Список задач по приоритету пуст.");
            } else {
                sendText(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()));
            }
        } else {
            sendNotFound(httpExchange, "Метод не поддерживается.");
        }
    }
}
