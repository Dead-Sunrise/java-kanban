import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubTaskHandlerTest extends BaseHttpHandlerTest {

    @Test
    void postSubTaskTest() throws IOException, InterruptedException { //тест добавления подзадачи в список
        Epic epic = new Epic("Эпик 1", "Описание");
        taskManager.createEpic(epic);
        String subTask = gson.toJson(new SubTask("Подзадача 1", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1)));
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subTask))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный код статуса.");
        assertEquals(1, taskManager.getAllSubTasks().size(), "Подзадача не добавлена.");
    }

    @Test
    void getSubTaskByIdTest() throws IOException, InterruptedException { //получение подзадачи по id
        Epic epic = new Epic("Эпик 1", "Описание");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        taskManager.createSubTask(subTask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Подзадача 1"), "Ошибка получения подзадачи");
    }

    @Test
    void getAllSubTasksTest() throws IOException, InterruptedException { // тест получения нескольких подзадач
        Epic epic = new Epic("Эпик 1", "Описание");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Подзадача 1") && response.body().contains("Подзадача 2"),
                "Список подзадач не пуст.");
    }

    @Test
    void getEmptySubTaskList() throws IOException, InterruptedException { //тест получения пустого списка подзадач
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().equals("Список подзадач пуст."), "Список подзадач не пуст.");
    }

    @Test
    void deleteSubTaskByIdTest() throws IOException, InterruptedException { //тест удаления одной подзадачи по id
        Epic epic = new Epic("Эпик 1", "Описание");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Подзадача удалена"), "Подзадача удалена некорректно.");
        assertEquals(1, taskManager.getAllSubTasks().size(), "Подзадача удалена некорректно.");
    }

    @Test
    void deleteAllSubTasksTest() throws IOException, InterruptedException { //тест удаления всех подзадач
        Epic epic = new Epic("Эпик 1", "Описание");
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Status.NEW, 0,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Все подзадачи удалены"), "Подзадачи удалены некорректно.");
        assertEquals(0, taskManager.getAllSubTasks().size(), "Подзадача удалены некорректно.");
    }
}