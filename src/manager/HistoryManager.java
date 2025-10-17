package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task);

    void removeFromHistory(int id);//удаление из истории

    List<Task> getHistory(); // получение истории просмотров
}
