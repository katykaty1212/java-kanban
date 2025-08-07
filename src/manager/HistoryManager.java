package manager;

import task.Task;
import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task); //добавление в историю
    List<Task> getHistory(); // получение истории просмотров
}
