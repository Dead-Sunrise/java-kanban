import controller.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File file;

    @BeforeEach
    void createFile() throws IOException {
        file = File.createTempFile("test", ".csv");
        manager = new FileBackedTaskManager(file.toPath());
    }

    @AfterEach
    void deleteFile() {
        file.delete();
    }

    @Test
    void testLoadFromEmptyFile() { //тест загрузки менеджера из пустого файла
        manager = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(manager);
        assertTrue(manager.getAllTasks().isEmpty(), "Список задач не пуст");
        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков не пуст");
        assertTrue(manager.getAllSubTasks().isEmpty(), "Список подзадач не пуст");
    }

    @Test
    void testSavingTasksToFile() throws IOException { //тест сохранения задач с разными типами в файл
        manager = new FileBackedTaskManager(file.toPath());
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        Epic epic = new Epic("Эпик 1", "Описание 1");
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, 2,
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        assertEquals(manager.getTaskById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .toString(), br.readLine());
        assertEquals(manager.getTaskById(1)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .toString(), br.readLine());
        assertEquals(manager.getEpicById(2)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .toString(), br.readLine());
        assertEquals(manager.getSubTaskById(3)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .toString(), br.readLine());
    }

    @Test
    void testSaveAndWriteFromFile() { //тест сохранения, затем записи задач из файла
        manager = new FileBackedTaskManager(file.toPath());
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        Epic epic = new Epic("Эпик 1", "Описание 1");
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, 2,
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(manager.getTaskById(0), newManager.getTaskById(0));
        assertEquals(manager.getTaskById(1), newManager.getTaskById(1));
        assertEquals(manager.getEpicById(2), newManager.getEpicById(2));
        assertEquals(manager.getSubTaskById(3), newManager.getSubTaskById(3));
    }
}
