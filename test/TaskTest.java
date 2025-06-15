import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testTasksWithEqualId() { //проверка на то что задачи с одинаковым id равны
        Task task1 = new Task("Задача 1", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        task1.setId(0);
        Task task2 = new Task("Задача 2", "Описание", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        task2.setId(task1.getId());
        assertEquals(task1, task2, "Задачи не равны");
    }

    @Test
    void testEpicAndSubTaskWithEqualId() { // проверка на то что наследники с одинаковым id равны
        Task task = new Task("Задача", "Подзадача", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        task.setId(1);
        Epic epic = new Epic("Эпик", "Описание");
        epic.setId(1);
        epic.setStatus(Status.NEW);
        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, 1,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        subTask.setId(1);
        assertEquals(task, subTask, "Наследники не равны");
        assertEquals(task, subTask, "Наследники не равны");
    }
}