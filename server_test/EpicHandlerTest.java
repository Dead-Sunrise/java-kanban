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

public class EpicHandlerTest extends BaseHttpHandlerTest {

    @Test
    void postEpicTest() throws IOException, InterruptedException { //тест добавления эпика в список
        String epic = gson.toJson(new Epic("Эпик 1", "Описание"));
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epic))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный код статуса.");
        assertEquals(1, taskManager.getAllEpics().size(), "Эпик не добавлен.");
    }

    @Test
    void getEpicByIdTest() throws IOException, InterruptedException { //получение эпика по id
        Epic epic = new Epic("Эпик 1", "Описание");
        taskManager.createEpic(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/0"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Эпик 1"), "Ошибка получения задачи");
    }

    @Test
    void getAllEpicsTest() throws IOException, InterruptedException { // тест получения нескольких эпиков
        Epic epic1 = new Epic("Эпик 1", "Описание");
        Epic epic2 = new Epic("Эпик 2", "Описание");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Эпик 1") && response.body().contains("Эпик 2"),
                "Список эпиков не пуст.");
    }

    @Test
    void getEmptyEpicListTest() throws IOException, InterruptedException { //тест получения пустого списка эпиков
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().equals("Список эпиков пуст."), "Список эпиков не пуст.");
    }

    @Test
    void getEpicSubTasksList() throws IOException, InterruptedException { //тест получения подзадач эпика
        Epic epic = new Epic("Эпик 1", "Описание");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Status.IN_PROGRESS, 0,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Status.IN_PROGRESS, 0,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/0/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Подзадача 1") && response.body().contains("Подзадача 2"));
    }

    @Test
    void deleteEpicByIdTest() throws IOException, InterruptedException { // тест удаления одного эпика по id
        Epic epic1 = new Epic("Эпик 1", "Описание");
        Epic epic2 = new Epic("Эпик 2", "Описание");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        HttpRequest httpRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/0"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode(), "Неверный код статуса.");
        assertTrue(response2.body().contains("Эпик удален"), "Эпик удален некорректно");
        assertEquals(1, taskManager.getAllEpics().size(), "Эпик удален некорректно");
    }

    @Test
    void deleteAllEpicsTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание");
        Epic epic2 = new Epic("Эпик 2", "Описание");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код статуса.");
        assertTrue(response.body().contains("Все эпики удалены"), "Эпики удалены некорректно");
        assertEquals(0, taskManager.getAllEpics().size(), "Эпики удалены некорректно");
    }
}