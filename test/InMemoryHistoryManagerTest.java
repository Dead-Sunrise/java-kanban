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
    void testEmptyHistoryTask() { //проверка пуст ли список при создании объекта
        assertTrue(taskManager.getHistory().isEmpty(), "Список не пуст");
    }

    @Test
    void testHistorySize() { // тест размера списка истории просмотров, если добавлено больше 10 элементов
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.createTask(task1);
        taskManager.getTaskById(0);
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.createTask(task2);
        taskManager.getTaskById(1);
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.createTask(task3);
        taskManager.getTaskById(2);
        Task task4 = new Task("Задача 4", "Описание 4");
        taskManager.createTask(task4);
        taskManager.getTaskById(3);
        Task task5 = new Task("Задача 5", "Описание 5");
        taskManager.createTask(task5);
        taskManager.getTaskById(4);
        Task task6 = new Task("Задача 6", "Описание 6");
        taskManager.createTask(task6);
        taskManager.getTaskById(5);
        Task task7 = new Task("Задача 7", "Описание 7");
        taskManager.createTask(task7);
        taskManager.getTaskById(6);
        Task task8 = new Task("Задача 8", "Описание 8");
        taskManager.createTask(task8);
        taskManager.getTaskById(7);
        Task task9 = new Task("Задача 9", "Описание 9");
        taskManager.createTask(task9);
        taskManager.getTaskById(8);
        Task task10 = new Task("Задача 10", "Описание 10");
        taskManager.createTask(task10);
        taskManager.getTaskById(9);
        Task task11 = new Task("Задача 11", "Описание 11");
        taskManager.createTask(task11);
        taskManager.getTaskById(10);
        assertEquals(11, taskManager.getHistory().size(), "В списке истории не 11 задач");
    }

    @Test
    void testSavePreviousVersionOfTasks() { // тест сохранения новой версии задачи в список и удаление старой версии
        Task taskVersion1 = new Task("Задача", "Описание");
        taskManager.createTask(taskVersion1);
        taskManager.getTaskById(0);
        Task taskVersion2 = new Task("Задача", "Новое описание");
        taskManager.updateTask(taskVersion2, taskVersion1.getId(), Status.IN_PROGRESS);
        taskManager.getTaskById(0);
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "Задачи добавлены некорректно");
        assertEquals(Status.IN_PROGRESS, history.getFirst().getStatus(), "Статус второй версии некорректный");
    }

    @Test
    void testAddTwoIdenticalTasks() { // проверка удаления из списка истории при просмотре идентичной задачи
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.createTask(task1);
        taskManager.getTaskById(0);
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.createTask(task2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(0);
        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void testViewTasksInOrderOfAddition() {
        List<Task> correctList = new ArrayList<>();// тест отображения задач в порядке добавления в историю
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.createTask(task2);
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.createTask(task3);
        Task task4 = new Task("Задача 4", "Описание 4");
        taskManager.createTask(task4);
        Task task5 = new Task("Задача 5", "Описание 5");
        taskManager.createTask(task5);
        Task task6 = new Task("Задача 6", "Описание 6");
        taskManager.createTask(task6);
        correctList.add(task1); // добавление задач в список в определенном порядке
        correctList.add(task5);
        correctList.add(task4);
        correctList.add(task2);
        correctList.add(task6);
        correctList.add(task3);
        taskManager.getTaskById(0); // просмотр задач в том же порядке
        taskManager.getTaskById(4);
        taskManager.getTaskById(3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(5);
        taskManager.getTaskById(2);
        List<Task> historyList = taskManager.getHistory();
        assertEquals(correctList, historyList, "порядок нарушен");
    }

    @Test
    void testRemoveTaskFromHistory() {
        List<Task> correctList = new ArrayList<>();// тест удаления задачи из истории
        Task task1 = new Task("Задача 1", "Описание 1");
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.createTask(task2);
        Task task3 = new Task("Задача 3", "Описание 3");
        taskManager.createTask(task3);
        Task task4 = new Task("Задача 4", "Описание 4");
        taskManager.createTask(task4);
        correctList.add(task1); // добавление задач в список в определенном порядке
        correctList.add(task4);
        correctList.add(task2);
        correctList.add(task3);
        taskManager.getTaskById(0); // просмотр задач в том же порядке
        taskManager.getTaskById(3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        correctList.remove(2); // удаление задачи из обоих списков
        taskManager.deleteTaskById(1);
        List<Task> historyList = taskManager.getHistory();
        assertEquals(correctList.size(), historyList.size(), "размер истории некорректный");
        assertEquals(correctList, historyList, "порядок нарушен");
    }
}