package manager;

public class HistoryList <T> {
    public  Node <T> head;
    public  Node <T> tail;

    public int size(){
        int count = 0;
        Node<T> current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

}

    class Node <T> {
        public T data;
        public Node <T> next;
        public Node <T> prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

