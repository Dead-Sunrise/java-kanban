package controller;

import taskmanagement.Node;
import taskmanagement.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node<Task>> historyTask = new HashMap<>();
    Node<Task> head;
    Node<Task> tail;

    public void linkLast(Task task) {
        int id = task.getId();
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, tail, null);
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
        Node<Task> node = head;
        while (node != null) {
            historyList.add(node.task);
            node = node.next;
        }
        return historyList;
    }

    public void removeNode(Node<Task> node) {
        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;
        if (node == null) return;
        if (prevNode != null) prevNode.next = nextNode;
        if (nextNode != null) nextNode.prev = prevNode;
        if (head == node) head = nextNode;
        if (tail == node) tail = prevNode;
    }

    @Override

    public void add(Task task) {
        if (historyTask.containsKey(task.getId())) {
            Node<Task> deleteNode = historyTask.get(task.getId());
            removeNode(deleteNode);
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override

    public void remove(int id) {
        if (historyTask.containsKey(id)) {
            Node<Task> node = historyTask.get(id);
            removeNode(node);
            historyTask.remove(id);
        } else {
            return;
        }
    }
}
