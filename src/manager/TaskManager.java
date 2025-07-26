package manager;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
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


    //удаление задач по ID
    public void deleteTask(int id){
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id); //удаляем эпик
        if (epic != null) { // проверяем на ноль
            for (int subtaskId : epic.getSubtaskIds()) {  //перебираем его подзадачи
                subtasks.remove(subtaskId); // удаляем
            }
        }
    }

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


    //удаление всех задач/подзадач/эпиков
    public void deleteAllTask(){
        tasks.clear();
    }

    public void deleteAllSubtask(){
        subtasks.clear();

    }

    public void deleteAllEpics(){
        deleteAllSubtask();
        epics.clear();
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


    //вывод задач
    public void printAllTask() {
        for (Task task : getAllTasks()) {
            System.out.println();
            System.out.println("[СТАТУС: " + task.getStatus() + "]");
            System.out.println("Задача: " + task.getTitle());
            System.out.println("Описание задачи: " + task.getDescription());
        }
    }

    //вывод эпиков с подзадачами
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
                    System.out.println("Подзадача: " + subtask.getTitle() + " "
                            + "[Статус подзадачи: " + subtask.getStatus() + "]");
                }
            }
        }
    }

    //вывод всех подзадач
    public void printAllSubtask(){
        if (subtasks.isEmpty()){
            System.out.println("Нет подзадач");
            return;
        }
        for (Subtask subtask : subtasks.values()){
            System.out.println("[СТАТУС: " + subtask.getStatus() + "]");
            System.out.println("Подзадача: " + subtask.getTitle());
        }
    }

    //вывод всех эпиков без подзадач
    public void printAllEpic(){
        if (epics.isEmpty()) {
            System.out.println("Нет эпиков!");
            return;
        }
        for (Epic epic : epics.values()) {
            System.out.println("[СТАТУС: " + epic.getStatus() + "]");
            System.out.println("Задача Эпик: " + epic.getTitle());
        }
    }

    //вывод эпика по id
    public void printEpicById(int id){
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Нет такого эпика");
        } else {
            System.out.println(epic.getTitle());
        }
    }

    //вывод подзадачи по ID
    public void printSubtaskById(int id){
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Нет такой подзадачи");
        } else {
            System.out.println(subtask.getTitle());
        }
    }

    //обновление
    public int updateTask(Task task, Status status) {
        if (task != null && tasks.containsKey(task.getId())) {
            task.setStatus(status);
            tasks.put(task.getId(), task);
        }

        return task.getId();
    }

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

    //вывод
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getAllEpicsSubtasks(int epicId) {
        List<Subtask> result = new ArrayList<>();
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