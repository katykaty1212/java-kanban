import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;


    //добавление задач
    public int addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubtaskIds().add(subtask.getId());
            updateEpicStatus(epic);
        }
        return subtask.getId();
    }


    //удаление задач
    public void deleteTask(int id) {  //удаление обычных задач
        tasks.remove(id);
    }

    // удаление эпиков
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id); //удаляем эпик
        if (epic != null) { // проверяем на ноль
            for (int subtaskId : epic.getSubtaskIds()) {  //перебираем его подзадачи
                subtasks.remove(subtaskId); // удаляем
            }
        }
    }

    // удаление подзадач
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);

        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove((Integer) id);
                updateEpicStatus(epic);
            }
        }
    }


    //обновление статуса
    public void updateTaskStatus(int id, Status status) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setStatus(status);
        }
    }

    public void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) { // смотрим все подзадачи
            epicSubtasks.add(subtasks.get(subtaskId)); // добавляем подзадачи в лист
        }
        if (epicSubtasks.isEmpty()) {  // проверяем есть ли подзадачи
            epic.setStatus(Status.NEW);
            return;  // если нет ставим статус нью и выходим из метода
        }

        boolean allDone = true; // все задачи завершены (yes)
        boolean anyInProgress = false; // есть задачи в процессе (no)

        //проверяем каждую задачу на статус

        for (Subtask subtask : epicSubtasks) { // идем по подзадачам
            if (subtask != null) {
                if (subtask.getStatus() != Status.DONE) {//если есть незавершенные
                    allDone = false;
                }

                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    anyInProgress = true;
                }

                //определяем статус
                if (allDone) {
                    epic.setStatus(Status.DONE);
                } else if (anyInProgress) {
                    epic.setStatus(Status.IN_PROGRESS);
                } else {
                    epic.setStatus(Status.NEW);
                }
            }
        }
    }

    //вывод всех задач
    public void printAllTask() {
        for (Task task : getAllTasks()) {
            System.out.println();
            System.out.println("[СТАТУС: " + task.getStatus() + "]");
            System.out.println("Задача: " + task.getTitle());
            System.out.println("Описание задачи: " + task.getDescription());
        }
    }

    //вывод эпиков
    public void printAllEpicWithSubtask() {
        if (epics.isEmpty()) {
            System.out.println("Нет задач!");
            return;
        }
        for (Epic epic : epics.values()) {
            System.out.println("[СТАТУС: " + epic.getStatus() + "]");
            System.out.println("Задача Эпик: " + epic.getTitle());

            ArrayList<Subtask> subtasks = getAllSubtaskByEpics(epic.getId());

            if (subtasks.isEmpty()) {
                System.out.println("Нет задач!");
            } else {
                for (Subtask subtask : subtasks) {
                    System.out.println("Подзадача: " + subtask.getTitle() + " "
                            + "[Статус подзадачи: " + subtask.getStatus() + "]");
                }
            }
        }
    }

    //обновление
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            ArrayList<Integer> existingSubtaskIds = epics.get(epic.getId()).getSubtaskIds();
            epics.put(epic.getId(), epic);

            epic.getSubtaskIds().clear();
            epic.getSubtaskIds().addAll(existingSubtaskIds);

            updateEpicStatus(epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epic);
            }
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtaskByEpics(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);

        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    result.add(subtask);
                }
            }
        }
        return result;
    }
}