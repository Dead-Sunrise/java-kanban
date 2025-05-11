import controller.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    File file;
    FileBackedTaskManager manager;

    @BeforeEach
    void createFile() throws IOException {
        file = File.createTempFile("test", ".csv");
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
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Epic epic = new Epic("Эпик 1", "Описание 1");
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1");
        manager.createTask(task1, Status.NEW);
        manager.createTask(task2, Status.NEW);
        manager.createEpic(epic);
        manager.createSubTask(subTask, 2, Status.NEW);
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        assertEquals(manager.getTaskById(0).toString(), br.readLine());
        assertEquals(manager.getTaskById(1).toString(), br.readLine());
        assertEquals(manager.getEpicById(2).toString(), br.readLine());
        assertEquals(manager.getSubTaskById(3).toString(), br.readLine());
    }

    @Test
    void testSaveAndWriteFromFile() { //тест сохранения, затем записи задач из файла
        manager = new FileBackedTaskManager(file.toPath());
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Epic epic = new Epic("Эпик 1", "Описание 1");
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1");
        manager.createTask(task1, Status.NEW);
        manager.createTask(task2, Status.NEW);
        manager.createEpic(epic);
        manager.createSubTask(subTask, 2, Status.NEW);
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(manager.getTaskById(0), newManager.getTaskById(0));
        assertEquals(manager.getTaskById(1), newManager.getTaskById(1));
        assertEquals(manager.getEpicById(2), newManager.getEpicById(2));
        assertEquals(manager.getSubTaskById(3), newManager.getSubTaskById(3));
    }
}
