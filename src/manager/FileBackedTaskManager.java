package manager;

import task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        if (file == null) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(task.toString());
            }

            for (Epic epic : getAllEpics()) {
                writer.write(epic.toString());
            }

            for (Subtask subtask : getAllSubtask()) {
                writer.write(subtask.toString());
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }
    }

    private Task fromString(String value) {

        if (value == null) {
            return null;
        }

        try {
            String[] parts = value.split(",");

            int id = Integer.parseInt(parts[0]);
            TypeTask type = TypeTask.valueOf(parts[1]);
            String name = parts[2];
            Status status = Status.valueOf(parts[3]);
            String description = parts[4];
            int epicID = 0;

            if (parts.length > 5) {
                epicID = Integer.parseInt(parts[5]);
            }

            switch (type) {
                case TASK:
                    Task task = new Task(name, description);
                    task.setId(id);
                    task.setStatus(status);
                    return task;
                case EPIC:
                    Epic epic = new Epic(name, description);
                    epic.setId(id);
                    epic.setStatus(status);
                    return epic;
                case SUBTASK:
                    Subtask subtask = new Subtask(name, description, epicID);
                    subtask.setId(id);
                    subtask.setStatus(status);
                    return subtask;
                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Ошибка при создании задачи из строки.");
            return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            String fileOneString = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            String[] lines = fileOneString.split("\n");

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();

                Task task = manager.fromString(line);

                if (task.getClass() == Epic.class) {
                    manager.getEpics().put(task.getId(), (Epic) task);
                } else if (task.getClass() == Subtask.class) {
                    manager.getSubtasks().put(task.getId(), (Subtask) task);
                } else if (task.getClass() == Task.class) {
                    manager.getTasks().put(task.getId(), task);
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Что то пошло не так при добавлении в память.", e);
        }

        for (Subtask subtask : manager.getAllSubtask()) {
            Epic epic = manager.getEpicById(subtask.getEpicId());

            if (epic != null) {
                epic.getSubtaskIds().add(subtask.getId());
            }
        }

        for (Epic epicNotStatus : manager.getEpics().values()) {
            manager.updateEpicStatus(epicNotStatus);
        }

        return manager;
    }

    @Override
    public int addTask(Task task) {
        int result = super.addTask(task);
        save();
        return result;
    }

    @Override
    public int addEpic(Epic epic) {
        int result = super.addEpic(epic);
        save();
        return result;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int result = super.addSubtask(subtask);
        save();
        return result;
    }

    @Override
    public int updateTask(Task task, Status status) {
        int result = super.updateTask(task, status);
        save();
        return result;
    }

    @Override
    public int updateEpic(Epic epic) {
        int result = super.updateEpic(epic);
        save();
        return result;
    }

    @Override
    public int updateSubtask(Subtask subtask, Status status) {
        int result = super.updateSubtask(subtask, status);
        save();
        return result;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }
}