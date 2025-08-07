package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.Test;
import task.Status;
import task.Task;

import static org.junit.Assert.*;

public class InMemoryHistoryManagerTest {

    @Test
    public void addHistoryAndSaveUpdateTask() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Название", "Описание");
        manager.addTask(task);
        manager.getTask(task.getId());

        assertNotNull(manager.getHistory());

        Task task1 = new Task("Новое название", "Новое описание");
        manager.updateTask(task1, Status.NEW);
        manager.getTask(task.getId());

        assertEquals(2, manager.getHistory().size());

    }
}