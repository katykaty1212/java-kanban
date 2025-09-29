package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {

    //---Создание таска/сабтаска/эпика---
    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    //---Обновление таска/сабтаска/эпика---
    int updateTask(Task task, Status status);

    int updateEpic(Epic epic);

    int updateSubtask(Subtask subtask, Status status);

    //-Получение тасков-
    List<Task> getAllTasks();

    //-Печать тасков-
    void printAllTask();

    //-Получение эпиков без подзадач-
    List<Epic> getAllEpics();

    //-Печать всех эпиков без подзадач-
    void printAllEpic();

    //-Поучение эпиков с подзадачами-
    List<Subtask> getAllEpicsSubtasks(int epicId);

    //-Печать эпиков с подзадачами-
    void printAllEpicWithSubtask();

    //-Получение всех подзадач-
    List<Subtask> getAllSubtask();

    //-Печать всех подзадач-
    void printAllSubtask();

    //-Получение таска по ID-
    Task getTask(int id);

    //-Получение эпика по id-
    Epic getEpicById(int id);

    //-Получение подзадачи по ID-
    Subtask getSubtaskById(int id);

    //удаление задач по ID
    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    //удаление всех задач/подзадач/эпиков
    void deleteAllTask();

    void deleteAllSubtask();

    void deleteAllEpics();

    void updateEpicStatus(Epic epic);

    //история
    List<Task> getHistory();

    void removeFromHistory(int id);

    List<Task> getPrioritizedTasks();

    boolean isTaskOverlay(Task task1, Task task2);
}
