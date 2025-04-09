import controller.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;
    @BeforeEach
    void newTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
    @Test
    void testDifferentTypesOfTasksAndGetById() { // добавление задач разного типа и их получение по id
        Task task = new Task("Задача", "Описание");
        Epic epic = new Epic("Эпик", "Описание");
        SubTask subTask = new SubTask("Подзадача", "Описание");
        taskManager.createTask(task, Status.NEW);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, 1, Status.NEW);
        assertEquals(task, taskManager.getTaskById(0));
        assertEquals(epic, taskManager.getEpicById(1));
        assertEquals(subTask, taskManager.getSubTaskById(2));
    }

    @Test
    void testFieldsAfterAddTask() { //Тест на совпадение полей после добавления задачи
        Task firstTask = new Task("Задача", "Подзадача");
        firstTask.setId(0);
        firstTask.setStatus(Status.NEW);
        taskManager.createTask(firstTask, Status.NEW);
        Task task = taskManager.getTaskById(0);
        assertEquals(firstTask.getId(), task.getId());
        assertEquals(firstTask.getStatus(), task.getStatus());
        assertEquals(firstTask.getName(), task.getName());
        assertEquals(firstTask.getDescription(), task.getDescription());
    }

    @Test
    void testTaskId() {// при присвоении id до добавления задачи, генерируется новый уникальный id после добавления
        Task taskGenerateId = new Task("Задача", "id сгенерирован");
        taskManager.createTask(taskGenerateId, Status.NEW);
        Task taskCreateId = new Task("Задача", "id задан");
        taskCreateId.setId(5);
        taskManager.createTask(taskCreateId, Status.NEW);
        assertNull(taskManager.getTaskById(5));
        assertNotNull(taskManager.getTaskById(0));
        assertNotNull(taskManager.getTaskById(1));
    }
}