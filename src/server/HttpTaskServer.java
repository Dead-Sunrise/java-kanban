package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import controller.Managers;
import controller.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private HttpServer httpServer;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        gson = new Gson();
        this.taskManager = taskManager;
        setHandlers();
    }

    private void setHandlers() {
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryTaskHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedTaskHandler(taskManager, gson));
    }

    public void start() {
        System.out.println("Сервер запущен.");
        httpServer.start();
    }

    public void stop() {
        System.out.println("Работа сервера прекращена.");
        httpServer.stop(0);
    }

    public Gson getGson() {
        return gson;
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }
}
