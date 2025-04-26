import controller.InMemoryTaskManager;
import taskmanagement.Status;
import taskmanagement.Epic;
import taskmanagement.SubTask;
import taskmanagement.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        System.out.println("Создаю две задачи, два эпика, три подзадачи к первому эпику, одна ко второму:");
        System.out.println("");
        manager.createTask(new Task("Задача 1", "Описание"), Status.NEW);
        manager.createTask(new Task("Задача 2", "Описание"), Status.IN_PROGRESS);
        manager.createEpic(new Epic("Эпик 1", "Описание эпика 1"));
        manager.createEpic(new Epic("Эпик 2", "Описание эпика 2"));
        manager.createSubTask(new SubTask("Подзадача 1 Эпик 1", "Описание подзадачи"),
                2, Status.NEW);
        manager.createSubTask(new SubTask("Подзадача 2 Эпик 1", "Описание подзадачи"), 2,
                Status.DONE);
        manager.createSubTask(new SubTask("Подзадача 3 Эпик 1", "Описание подзадачи"), 2,
                Status.IN_PROGRESS);
        manager.createSubTask(new SubTask("Подзадача 1 Эпик 2", "Описание подзадачи"), 3,
                Status.DONE);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        System.out.println("");
        System.out.println("Изменяю название и статус у одной задачи и двух подзадач," +
                " вывожу список подзадач первого эпика. " + "Получаю 11 задач по id и вывожу историю просмотров");
        System.out.println("");
        manager.updateTask(new Task("Новая задача 1", "Описание"),
                1, Status.DONE);
        manager.updateSubTask(new SubTask("Новая подзадача 2", "Описание"),
                2, 5, Status.IN_PROGRESS);
        manager.updateSubTask(new SubTask("Новая подзадача 3", "Описание"),
                2, 6, Status.NEW);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getEpicById(2);
        manager.getSubTaskById(5);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getEpicById(2);
        manager.getSubTaskById(5);
        System.out.println(manager.getHistory());
        manager.getAllEpicSubTasks(2);
        System.out.println("");
        System.out.println("Удаляю одну задачу, один эпик, одну подзадачу у первого эпика:");
        System.out.println("");
        manager.deleteTaskById(1);
        manager.deleteEpicById(3);
        manager.deleteSubTaskById(5);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
    }
}