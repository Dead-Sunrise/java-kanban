import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrioritizedTaskHandlerTest extends BaseHttpHandlerTest {
    @Test
    void getPrioritizedTaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание");
        SubTask subTask = new SubTask("Подзадача 1", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task = new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Подзадача 1") &&
                response.body().contains("Задача 1"), "Список задач по приоритету некорректный.");
    }

    @Test
    void getEmptyPrioritizedTaskTest() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Список задач по приоритету пуст."));
    }
}