package controller;

import taskmanagement.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> historyTask = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (historyTask.size() < 10) {
            historyTask.add(task);
        } else {
            historyTask.removeFirst();
            historyTask.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyTask);
    }
}
