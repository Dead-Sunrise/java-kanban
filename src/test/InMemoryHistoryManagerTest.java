import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanagement.Status;
import taskmanagement.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    void newManagers() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testHistorySize() { // тест размера списка истории просмотров, если добавлено больше 10 элементов
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.createTask(task1, Status.NEW);
        taskManager.getTaskById(0);
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.createTask(task2, Status.NEW);
        taskManager.getTaskById(1);
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.createTask(task3, Status.NEW);
        taskManager.getTaskById(2);
        Task task4 = new Task("Задача 4", "Описание 4");
        taskManager.createTask(task4, Status.NEW);
        taskManager.getTaskById(3);
        Task task5 = new Task("Задача 5", "Описание 5");
        taskManager.createTask(task5, Status.NEW);
        taskManager.getTaskById(4);
        Task task6 = new Task("Задача 6", "Описание 6");
        taskManager.createTask(task6, Status.NEW);
        taskManager.getTaskById(5);
        Task task7 = new Task("Задача 7", "Описание 7");
        taskManager.createTask(task7, Status.NEW);
        taskManager.getTaskById(6);
        Task task8 = new Task("Задача 8", "Описание 8");
        taskManager.createTask(task8, Status.NEW);
        taskManager.getTaskById(7);
        Task task9 = new Task("Задача 9", "Описание 9");
        taskManager.createTask(task9, Status.NEW);
        taskManager.getTaskById(8);
        Task task10 = new Task("Задача 10", "Описание 10");
        taskManager.createTask(task10, Status.NEW);
        taskManager.getTaskById(9);
        Task task11 = new Task("Задача 11", "Описание 11");
        taskManager.createTask(task11, Status.NEW);
        taskManager.getTaskById(10);
        assertEquals(10, taskManager.getHistory().size(), "В списке истории больше 10 задач");

    }

    @Test
    void testSavePreviousVersionOfTasks() { // тест сохранения разных версий одной задачи в историю просмотров
        Task taskVersion1 = new Task("Задача", "Описание");
        taskManager.createTask(taskVersion1, Status.NEW);
        taskManager.getTaskById(0);
        Task taskVersion2 = new Task("Задача", "Новое описание");
        taskManager.updateTask(taskVersion2, taskVersion1.getId(), Status.IN_PROGRESS);
        taskManager.getTaskById(0);
        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "Задачи добавлены некорректно");
        assertEquals(Status.NEW, history.get(0).getStatus(), "Статус первой версии некорректный");
        assertEquals(Status.IN_PROGRESS, history.get(1).getStatus(), "Статус второй версии некорректный");
    }
}