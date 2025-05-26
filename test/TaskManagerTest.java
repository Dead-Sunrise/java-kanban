import controller.TaskManager;
import org.junit.jupiter.api.Test;
import taskmanagement.Epic;
import taskmanagement.Status;
import taskmanagement.SubTask;
import taskmanagement.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    T manager;

    @Test
    void createAndDeleteTasks() { //тест на создание задач, удаление по одной, удаление всех задач
        Epic epic1 = new Epic("Эпик 1", "Описание");
        Epic epic2 = new Epic("Эпик", "Описание");
        Task task1 = new Task("Задача1", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача2", "Описание",
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        Task task3 = new Task("Задача3", "Описание",
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        Task task4 = new Task("Задача4", "Описание",
                LocalDateTime.of(2025, Month.MAY, 4, 10, 0), Duration.ofHours(1));
        SubTask subTask1Epic1 = new SubTask("Подзадача 1", "Описание1",
                LocalDateTime.of(2025, Month.MAY, 5, 10, 0), Duration.ofHours(1));
        SubTask subTask2Epic1 = new SubTask("Подзадача 2", "Описание2",
                LocalDateTime.of(2025, Month.MAY, 6, 10, 0), Duration.ofHours(1));
        SubTask subTask3Epic1 = new SubTask("Подзадача 3", "Описание3",
                LocalDateTime.of(2025, Month.MAY, 7, 10, 0), Duration.ofHours(1));
        SubTask subTask1Epic2 = new SubTask("Подзадача 1", "Описание1",
                LocalDateTime.of(2025, Month.MAY, 8, 10, 0), Duration.ofHours(1));
        SubTask subTask2Epic2 = new SubTask("Подзадача 2", "Описание2",
                LocalDateTime.of(2025, Month.MAY, 9, 10, 0), Duration.ofHours(1));
        SubTask subTask3Epic2 = new SubTask("Подзадача 3", "Описание3",
                LocalDateTime.of(2025, Month.MAY, 10, 10, 0), Duration.ofHours(1));
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createTask(task1, Status.NEW);
        manager.createTask(task2, Status.NEW);
        manager.createTask(task3, Status.NEW);
        manager.createTask(task4, Status.NEW);
        manager.createSubTask(subTask1Epic1, 0, Status.IN_PROGRESS);
        manager.createSubTask(subTask2Epic1, 0, Status.IN_PROGRESS);
        manager.createSubTask(subTask3Epic1, 0, Status.IN_PROGRESS);
        manager.createSubTask(subTask1Epic2, 1, Status.IN_PROGRESS);
        manager.createSubTask(subTask2Epic2, 1, Status.IN_PROGRESS);
        manager.createSubTask(subTask3Epic2, 1, Status.IN_PROGRESS);
        assertEquals(4, manager.getAllTasks().size(), "Задачи добавлены некорректно");
        assertEquals(2, manager.getAllEpics().size(), "Эпики добавлены некорректно");
        assertEquals(6, manager.getAllSubTasks().size(), "Подзадачи добавлены некорректно");
        assertEquals(3, manager.getAllEpicSubTasks(0).size(), "Подзадачи не добавлены в Эпик 1");
        assertEquals(3, manager.getAllEpicSubTasks(1).size(), "Подзадачи не добавлены в Эпик 2");
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .getStatus(), "Статус эпика некорректный");
        assertEquals(10, manager.getPrioritizedTasks().size(),
                "Список задач по приоритету некорректный");
        manager.deleteTaskById(2);
        manager.deleteEpicById(1);
        manager.deleteSubTaskById(7);
        assertEquals(3, manager.getAllTasks().size(), "Задача удалена некорректно");
        assertEquals(1, manager.getAllEpics().size(), "Эпик удален некорректно");
        assertEquals(2, manager.getAllSubTasks().size(), "Подзадачи удалены некорректно");
        assertEquals(2, manager.getAllEpicSubTasks(0).size(),
                "Список подзадач в Эпике 1 некорректный");
        assertEquals(5, manager.getPrioritizedTasks().size(),
                "Список задач по приоритету некорректный");
        manager.deleteAllSubTasks();
        assertTrue(manager.getAllEpicSubTasks(0).isEmpty(),
                "Подадачи не удалены из списка подзадач эпика");
        assertEquals(Status.NEW, manager.getEpicById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .getStatus(), "Статус эпика обновлён некорректно");
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        assertTrue(manager.getAllTasks().isEmpty(), "Задачи удалены некорректно");
        assertTrue(manager.getAllEpics().isEmpty(), "Эпики удалены некорректно");
        assertTrue(manager.getAllSubTasks().isEmpty(), "Подзадачи удалены некорректно");
        assertTrue(manager.getPrioritizedTasks().isEmpty(), "Список задач по приоритету не пуст");
    }

    @Test
    void testDifferentTypesOfTasksAndGetById() { // добавление задач разного типа и их получение по id
        Task task = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Epic epic = new Epic("Эпик", "Описание");
        SubTask subTask = new SubTask("Подзадача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        manager.createTask(task, Status.NEW);
        manager.createEpic(epic);
        manager.createSubTask(subTask, 1, Status.NEW);
        assertEquals(task, manager.getTaskById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена")));
        assertEquals(epic, manager.getEpicById(1)
                .orElseThrow(() -> new RuntimeException("Задача не найдена")));
        assertEquals(subTask, manager.getSubTaskById(2)
                .orElseThrow(() -> new RuntimeException("Задача не найдена")));
    }

    @Test
    void testFieldsAfterAddTask() { //Тест на совпадение полей после добавления задачи
        Task firstTask = new Task("Задача", "Подзадача",
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        firstTask.setId(0);
        firstTask.setStatus(Status.NEW);
        manager.createTask(firstTask, Status.NEW);
        Task task = manager.getTaskById(0).orElseThrow(() -> new RuntimeException("Задача не найдена"));
        assertEquals(firstTask.getId(), task.getId());
        assertEquals(firstTask.getStatus(), task.getStatus());
        assertEquals(firstTask.getName(), task.getName());
        assertEquals(firstTask.getDescription(), task.getDescription());
    }

    @Test
    void testTaskId() {// при присвоении id до добавления задачи, генерируется новый уникальный id после добавления
        Task taskGenerateId = new Task("Задача", "id сгенерирован");
        manager.createTask(taskGenerateId, Status.NEW);
        Task taskCreateId = new Task("Задача", "id задан");
        taskCreateId.setId(5);
        manager.createTask(taskCreateId, Status.NEW);
        assertNull(manager.getTaskById(5).orElse(null));
        assertNotNull(manager.getTaskById(0));
        assertNotNull(manager.getTaskById(1));
    }

    @Test
    void testDeleteSubTask() { // тест удаления подзадачи из спика подзадач эпика при удалении из менеджера
        Epic epic = new Epic("Эпик", "Описание");
        manager.createEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача1", "Описание1",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        manager.createSubTask(subTask1, 0, Status.NEW);
        SubTask subTask2 = new SubTask("Подзадача2", "Описание2",
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        manager.createSubTask(subTask2, 0, Status.NEW);
        SubTask subTask3 = new SubTask("Подзадача3", "Описание3",
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        manager.createSubTask(subTask3, 0, Status.NEW);
        List<SubTask> epicSubTasks = manager.getAllEpicSubTasks(0);
        assertEquals(3, epicSubTasks.size());
        manager.deleteSubTaskById(1);
        assertEquals(2, epicSubTasks.size(), "подзадача удалена некорректно");
    }

    @Test
    void testEpicStatus() { //тест расчета статуса эпика при разных условиях
        Epic epic = new Epic("Эпик", "Описание");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание1",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание2",
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание3",
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        manager.createEpic(epic);
        manager.createSubTask(subTask1, 0, Status.NEW);
        manager.createSubTask(subTask2, 0, Status.NEW);
        manager.createSubTask(subTask3, 0, Status.NEW);
        assertEquals(Status.NEW, manager.getEpicById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .getStatus(), "Статус NEW рассчитан некорректно");
        manager.updateSubTask(subTask1, 0, 1, Status.DONE);
        manager.updateSubTask(subTask2, 0, 2, Status.DONE);
        manager.updateSubTask(subTask3, 0, 3, Status.DONE);
        assertEquals(Status.DONE, manager.getEpicById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .getStatus(), "Статус DONE рассчитан некорректно");
        manager.updateSubTask(subTask1, 0, 1, Status.IN_PROGRESS);
        manager.updateSubTask(subTask2, 0, 2, Status.IN_PROGRESS);
        manager.updateSubTask(subTask3, 0, 3, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .getStatus(), "Статус IN_PROGRESS рассчитан некорректно");
        manager.updateSubTask(subTask1, 0, 1, Status.NEW);
        manager.updateSubTask(subTask2, 0, 2, Status.DONE);
        manager.updateSubTask(subTask3, 0, 3, Status.DONE);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(0)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"))
                .getStatus(), "Статус IN_PROGRESS рассчитан некорректно");
    }

    @Test
    void testIntersection() { //тест добавления задач с пересечением по времени при разных условиях
        Task firstTask = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        manager.createTask(firstTask, Status.NEW);
        // полное пересечение
        Task task1 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        // начало раньше первой задачи, конец перед концом первой задачи
        Task task2 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 9, 30), Duration.ofHours(1));
        // начало в середине первой задачи, конец после
        Task task3 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 30), Duration.ofHours(1));
        // начало раньше первой задачи, конец равен началу первой задачи
        Task task4 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 9, 0), Duration.ofHours(1));
        // начало равно концу первой задачи, конец после
        Task task5 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 11, 0), Duration.ofHours(1));
        // начало раньше начала первой задачи, конец после конца первой задачи
        Task task6 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 9, 30), Duration.ofHours(2));
        // начало после начала первой задачи, конец перед концом первой задачи
        Task task7 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 10), Duration.ofMinutes(30));
        // начало после начала первой задачи, конец равен концу первой задачи
        Task task8 = new Task("Задача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 9, 30), Duration.ofMinutes(30));
        // пересечение задачи другого типа
        SubTask subTask = new SubTask("Подзадача", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 9, 30), Duration.ofMinutes(30));
        manager.createTask(task1, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 1 с пересечением");
        manager.createTask(task2, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 2 с пересечением");
        manager.createTask(task3, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 3 с пересечением");
        manager.createTask(task4, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 4 с пересечением");
        manager.createTask(task5, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 5 с пересечением");
        manager.createTask(task6, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 6 с пересечением");
        manager.createTask(task7, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 7 с пересечением");
        manager.createTask(task8, Status.NEW);
        assertEquals(1, manager.getAllTasks().size(), "Добавлена задача 8 с пересечением");
        manager.createSubTask(subTask, 1, Status.NEW);
        assertEquals(0, manager.getAllSubTasks().size(), "Добавлена подзадача с пересечением");
    }

    @Test
    void testPrioritizedTasks() { // тест добавления задач/задач без заданного времени, в приоритетный список
        Task task1 = new Task("Задача1", "Описание",
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Задача2", "Описание",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        Task task3 = new Task("Задача3", "Описание",
                LocalDateTime.of(2025, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        Task task4 = new Task("Задача4", "Описание", null, Duration.ofHours(1));
        manager.createTask(task1, Status.NEW);
        manager.createTask(task2, Status.NEW);
        manager.createTask(task3, Status.NEW);
        manager.createTask(task4, Status.NEW);
        TreeSet<Task> set = (TreeSet<Task>) manager.getPrioritizedTasks();
        assertEquals(3, set.size(), "Список задач по приоритету некорректный");
        assertEquals(task2.getName(), set.first().getName(), "Задачи отсортированы некорректно");
        assertEquals(task1.getName(), set.last().getName(), "Задачи отсортированы некорректно");
    }

    @Test
    void testEpicTime() { //тест рассчёта даты начала, продолжительности, даты завершения выполнения эпика
        Epic epic = new Epic("Эпик", "Описание");
        SubTask subTask1 = new SubTask("Подзадача1", "Описание1",
                LocalDateTime.of(2025, Month.MAY, 1, 10, 0), Duration.ofHours(1));
        SubTask subTask2 = new SubTask("Подзадача2", "Описание2",
                LocalDateTime.of(2024, Month.MAY, 2, 10, 0), Duration.ofHours(1));
        SubTask subTask3 = new SubTask("Подзадача3", "Описание3",
                LocalDateTime.of(2025, Month.MAY, 3, 10, 0), Duration.ofHours(1));
        Duration correctDuration = subTask1.getDuration().plus(subTask2.getDuration().plus(subTask3.getDuration()));
        manager.createEpic(epic);
        manager.createSubTask(subTask1, 0, Status.NEW);
        manager.createSubTask(subTask2, 0, Status.NEW);
        manager.createSubTask(subTask3, 0, Status.NEW);
        assertEquals(epic.getStartTime(), subTask2.getStartTime(), "Время начала рассчитано некорректно");
        assertEquals(epic.getEndTime(), subTask3.getEndTime(), "Время завершения рассчитано некорректно");
        assertEquals(epic.getDuration(), correctDuration, "Время продолжительности рассчитано некорректно");
    }
}