package taskmanagement;

public class Node<Task> {
    public Task task;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Task task, Node<Task> prev, Node<Task> next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }
}
