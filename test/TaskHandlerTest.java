import org.junit.jupiter.api.Test;
import taskmanagement.Status;
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

public class TaskHandlerTest extends BaseHttpHandlerTest {

    @Test
    void postTaskTest() throws IOException, InterruptedException { //тест добавления задачи в список
        String task = gson.toJson(new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1)));
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный код статуса.");
        assertEquals(1, taskManager.getAllTasks().size(), "Задача не добавлена.");
    }

    @Test
    void getTaskByIdTest() throws IOException, InterruptedException { //получение задачи по id
        Task task = new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Задача 1"), "Ошибка получения задачи");
    }

    @Test
    void getAllTasksTest() throws IOException, InterruptedException { // тест получения нескольких задач
        Task task1 = new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача 2", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Задача 1") && response.body().contains("Задача 2"),
                "Список задач не пуст.");
    }

    @Test
    void getEmptyTaskList() throws IOException, InterruptedException { //тест получения пустого списка задач
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().equals("Список задач пуст."), "Список задач не пуст.");
    }

    @Test
    void deleteTaskByIdTest() throws IOException, InterruptedException { //тест удаления одной задачи по id
        Task task1 = new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача 2", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        HttpRequest httpRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode(), "Неверный код статуса.");
        assertTrue(response2.body().contains("Задача удалена"), "Задача удалена некорректно.");
        assertEquals(1, taskManager.getAllTasks().size(), "Задача удалена некорректно.");
    }

    @Test
    void deleteAllTasksTest() throws IOException, InterruptedException { //тест удаления всех задач
        Task task1 = new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача 2", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Все задачи удалены"), "Задачи удалены некорректно.");
        assertEquals(0, taskManager.getAllTasks().size(), "Задача удалены некорректно.");
    }
}