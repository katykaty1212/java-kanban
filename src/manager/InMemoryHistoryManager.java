package manager;

import task.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final HistoryList<Task> historyList = new HistoryList<>();
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    public class HistoryList<T> {
        public Node<T> head;
        public Node<T> tail;

        public int size() {
            int count = 0;
            Node<T> current = head;
            while (current != null) {
                count++;
                current = current.next;
            }
            return count;
        }

    }

    @Override
    public void addToHistory(Task task) {
        if (task != null) {
            if (historyMap.containsKey(task.getId())) {
                removeFromHistory(task.getId());//удалить узел
            }
            Node<Task> newNode = new Node<>(task);
            linkLast(newNode);
            historyMap.put(task.getId(), newNode);
        }
    }

    public void linkLast(Node<Task> newNode) {
        if (historyList.head == null) {
            historyList.head = newNode;
            historyList.tail = newNode;
        } else {
            newNode.prev = historyList.tail;
            historyList.tail.next = newNode;
            historyList.tail = newNode;

        }
    }

    @Override
    public void removeFromHistory(int id) {
        Node<Task> removeNode = historyMap.get(id);

        if (removeNode != null) {
            removeNode(removeNode);
            historyMap.remove(id);
        }
    }

    public void removeNode(Node<Task> node) { //удаление и обновление связей узла
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                historyList.head = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                historyList.tail = node.prev;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> resultHistory = new ArrayList<>();
        Node<Task> current = historyList.head;

        while (current != null) {
            resultHistory.add(current.data);
            current = current.next;
        }
        return resultHistory;
    }

}