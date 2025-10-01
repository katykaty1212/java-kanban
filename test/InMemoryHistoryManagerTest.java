import manager.Managers;
import manager.TaskManager;
import org.junit.Test;
import task.Status;
import task.Task;

import static org.junit.Assert.*;

public class InMemoryHistoryManagerTest {

    protected TaskManager manager = Managers.getDefault();

    @Test
    public void addHistoryAndSaveUpdateTask() {

        Task task = new Task("Название", "Описание");
        manager.addTask(task);
        manager.getTask(task.getId());

        assertNotNull(manager.getHistory());

        Task task1 = new Task("Новое название", "Новое описание");
        manager.addTask(task1);
        manager.updateTask(task1, Status.IN_PROGRESS);
        manager.getTask(task1.getId());

        assertEquals(2, manager.getHistory().size());

    }

    @Test
    public void deleteDuplicateHistory() {

        Task task = new Task("Название", "Описание");
        manager.addTask(task);
        manager.getTask(task.getId());

        Task task1 = new Task("Название1", "Описание1");
        manager.addTask(task1);
        manager.getTask(task1.getId());

        Task task2 = new Task("Название2", "Описание2");
        manager.addTask(task2);
        manager.getTask(task2.getId());

        manager.getTask(task1.getId());

        assertEquals(3, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().getLast());

    }

    @Test
    public void deleteTaskInHistory() {

        Task task = new Task("Название", "Описание");
        manager.addTask(task);
        manager.getTask(task.getId());

        Task task1 = new Task("Название1", "Описание1");
        manager.addTask(task1);
        manager.getTask(task1.getId());

        Task task2 = new Task("Название2", "Описание2");
        manager.addTask(task2);
        manager.getTask(task2.getId());

        assertEquals(3, manager.getHistory().size());

        manager.removeFromHistory(task1.getId());

        assertEquals(2, manager.getHistory().size());
        assertTrue(manager.getHistory().contains(task));
        assertTrue(manager.getHistory().contains(task2));

        manager.getTask(task1.getId());

        manager.removeFromHistory(task.getId());

        assertEquals(2, manager.getHistory().size());
        assertTrue(manager.getHistory().contains(task1));
        assertTrue(manager.getHistory().contains(task2));
    }
}