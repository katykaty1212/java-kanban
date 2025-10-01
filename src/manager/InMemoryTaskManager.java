package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int nextId = 1;


    //---Создание таска/сабтаска/эпика---
    @Override
    public int addTask(Task task) {

        if (isTaskOverlayInPrioritizedList(task)) {
            System.out.println("Задача пересекается по времени с существующей. Задача добавлена без даты и времени.");
            task.setDuration(null);
            task.setStartTime(null);
        }

        task.setId(nextId++);
        tasks.put(task.getId(), task);

        if (task.getStartTime() != null || task.getDuration() != null) {
            prioritizedTasks.add(task);
        }


        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {

        if (isTaskOverlayInPrioritizedList(subtask)) {
            System.out.println("Задача пересекается по времени с существующей.Задача добавлена без даты и времени.");
            subtask.setDuration(null);
            subtask.setStartTime(null);
        }

        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());

        if (epic != null) {
            epic.getSubtaskIds().add(subtask.getId());
            updateEpicStatus(epic);
        }

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
            updateEpicTime(epic);
        }


        return subtask.getId();
    }


    //---Обновление таска/сабтаска/эпика---
    @Override
    public int updateTask(Task task, Status status) {
        if (task != null && tasks.containsKey(task.getId())) {
            task.setStatus(status);
            tasks.put(task.getId(), task);
        }

        return task.getId();
    }

    @Override
    public int updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            List<Integer> existingSubtaskIds = epics.get(epic.getId()).getSubtaskIds();

            epic.getSubtaskIds().clear();
            epic.getSubtaskIds().addAll(existingSubtaskIds);

            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
        return epic.getId();
    }

    @Override
    public int updateSubtask(Subtask subtask, Status status) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                subtasks.put(subtask.getId(), subtask);
                subtask.setStatus(status);
                updateEpicStatus(epic);
            }
        }
        return subtask.getId();
    }


    // ---Получение всех тасков/сабтасков/эпиков---

    //-Получение тасков-
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    //-Печать тасков-
    @Override
    public void printAllTask() {
        for (Task task : getAllTasks()) {
            System.out.println();
            System.out.println("[СТАТУС: " + task.getStatus() + "]");
            System.out.println("Задача: " + task.getTitle());
            System.out.println("Описание задачи: " + task.getDescription());
        }
    }

    //-Получение эпиков без подзадач-
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    //-Печать всех эпиков без подзадач-
    @Override
    public void printAllEpic() {
        for (Epic epic : getAllEpics()) {
            System.out.println("[СТАТУС: " + epic.getStatus() + "]");
            System.out.println("Задача Эпик: " + epic.getTitle());
        }
    }

    //-Поучение эпиков с подзадачами-
    @Override
    public List<Subtask> getAllEpicsSubtasks(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic.getSubtaskIds() == null) {
            return Collections.emptyList();
        }

        return epic.getSubtaskIds().stream().map(subtasks::get).toList();
    }

    //-Печать эпиков с подзадачами-
    @Override
    public void printAllEpicWithSubtask() {
        if (epics.isEmpty()) {
            System.out.println("Нет задач!");
            return;
        }
        for (Epic epic : epics.values()) {
            System.out.println("[СТАТУС: " + epic.getStatus() + "]");
            System.out.println("Задача Эпик: " + epic.getTitle());

            List<Subtask> subtasks = getAllEpicsSubtasks(epic.getId());

            if (subtasks.isEmpty()) {
                System.out.println("Нет задач!");
            } else {
                for (Subtask subtask : subtasks) {
                    System.out.println("Подзадача: " + subtask.getTitle() + " " + "[Статус подзадачи: " + subtask.getStatus() + "]");
                }
            }
        }
    }

    //-Получение всех подзадач-
    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    //-Печать всех подзадач-
    @Override
    public void printAllSubtask() {
        for (Subtask subtask : getAllSubtask()) {
            System.out.println("[СТАТУС: " + subtask.getStatus() + "]");
            System.out.println("Подзадача: " + subtask.getTitle());
        }
    }

    //-Получение таска по ID-
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addToHistory(task);
        }
        return task;
    }

    //-Получение эпика по id-
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addToHistory(epic);
        }
        return epic;
    }


    //-Получение подзадачи по ID-
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addToHistory(subtask);
        }
        return subtask;
    }

    //удаление задач по ID
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        removeFromHistory(id);
        prioritizedTasks.remove(tasks.get(id));
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id); //удаляем эпик
        if (epic != null) { // проверяем на ноль
            for (int subtaskId : epic.getSubtaskIds()) {  //перебираем его подзадачи
                subtasks.remove(subtaskId); // удаляем
            }
        }
        removeFromHistory(id);
        prioritizedTasks.remove(epics.get(id));
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        prioritizedTasks.remove(subtasks.get(id));

        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove((Integer) id);
                updateEpicStatus(epic);
                updateEpicTime(epic);
            }
        }
    }

    //удаление всех задач/подзадач/эпиков
    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtask() {
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtask();
        epics.clear();
    }

    @Override
    public void updateEpicStatus(Epic epic) {

        List<Subtask> epicSubtasks = epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .toList();

        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = epicSubtasks.stream()
                .allMatch(subtask -> subtask.getStatus() == Status.DONE);
        boolean anyInProgress = epicSubtasks.stream()
                .anyMatch(subtask -> subtask.getStatus() == Status.IN_PROGRESS);

        //определяем статус
        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (anyInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void removeFromHistory(int id) {
        historyManager.removeFromHistory(id);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void updateEpicTime(Epic epic) {
        List<Subtask> subtasks = getAllEpicsSubtasks(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        epic.setDuration(getEpicDuration(epic));
        epic.setStartTime(getEpicStartTime(epic));
        epic.setEndTime(getEpicEndTime(epic));
    }

    public Duration getEpicDuration(Epic epic) {
        Duration timeAllSubtask = Duration.ZERO;

        for (Subtask subtask : getAllEpicsSubtasks(epic.getId())) {
            timeAllSubtask = subtask.getDuration().plus(timeAllSubtask);
        }
        return timeAllSubtask;
    }

    public LocalDateTime getEpicStartTime(Epic epic) {
        Optional<LocalDateTime> minStartTime = getAllEpicsSubtasks(epic.getId()).stream()
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo);

        return minStartTime.orElse(null);
    }

    public LocalDateTime getEpicEndTime(Epic epic) {
        Optional<LocalDateTime> maxEndTime = getAllEpicsSubtasks(epic.getId()).stream()
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo);

        return maxEndTime.orElse(null);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(prioritizedTasks);
    }

    @Override
    public boolean isTaskOverlay(Task task1, Task task2) {

        boolean noOverlay = task1.getStartTime().plus(task1.getDuration()).isBefore(task2.getStartTime()) ||
                task2.getStartTime().plus(task2.getDuration()).isBefore(task1.getStartTime());

        return !noOverlay;
    }

    public boolean isTaskOverlayInPrioritizedList(Task newTask) {
        return getPrioritizedTasks().stream()
                .filter(task -> task.getId() != newTask.getId())
                .anyMatch(task -> isTaskOverlay(task, newTask));
    }
}