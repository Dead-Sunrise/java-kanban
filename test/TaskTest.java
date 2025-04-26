import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testTasksWithEqualId() { //проверка на то что задачи с одинаковым id равны
        Task task1 = new Task("Задача 1", "Описание");
        task1.setId(0);
        task1.setStatus(Status.NEW);
        Task task2 = new Task("Задача 2", "Описание");
        task2.setId(task1.getId());
        task2.setStatus(Status.NEW);
        assertEquals(task1, task2, "Задачи не равны");
    }

    @Test
    void testEpicAndSubTaskWithEqualId() { // проверка на то что наследники с одинаковым id равны
        Task task = new Task("Задача", "Подзадача");
        task.setId(1);
        task.setStatus(Status.NEW);
        Epic epic = new Epic("Эпик", "Описание");
        epic.setId(1);
        epic.setStatus(Status.NEW);
        SubTask subTask = new SubTask("Подзадача", "Описание");
        subTask.setId(1);
        subTask.setStatus(Status.NEW);
        assertEquals(task, subTask, "Наследники не равны");
        assertEquals(task, subTask, "Наследники не равны");
    }
}