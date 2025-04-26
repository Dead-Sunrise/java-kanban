package controller;

import taskmanagement.Node;
import taskmanagement.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> historyTask = new HashMap<>();
    Node head;
    Node tail;

    public void linkLast(Task task) {
        int id = task.getId();
        final Node oldTail = tail;
        final Node newNode = new Node(task, tail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyTask.put(id, newNode);
    }

    @Override

    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.task);
            node = node.next;
        }
        return historyList;
    }

    public void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (node == null) return;
        if (prevNode != null) prevNode.next = nextNode;
        if (nextNode != null) nextNode.prev = prevNode;
        if (head == node) head = nextNode;
        if (tail == node) tail = prevNode;
    }

    @Override

    public void add(Task task) {
        if (historyTask.containsKey(task.getId())) {
            Node deleteNode = historyTask.get(task.getId());
            removeNode(deleteNode);
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override

    public void remove(int id) {
        if (historyTask.containsKey(id)) {
            Node node = historyTask.get(id);
            removeNode(node);
            historyTask.remove(id);
        } else {
            return;
        }
    }
}
