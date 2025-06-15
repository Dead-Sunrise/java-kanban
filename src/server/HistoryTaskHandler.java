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

public class HistoryTaskHandler extends BaseHttpHandler {
    private final Gson gson;

    public HistoryTaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            if (taskManager.getHistory().isEmpty()) {
                sendText(httpExchange, "Список истории просмотров пуст.");
            } else {

                sendText(httpExchange, gson.toJson(taskManager.getHistory()));
            }
        } else {
            sendNotFound(httpExchange, "Метод не поддерживается.");
        }
    }
}