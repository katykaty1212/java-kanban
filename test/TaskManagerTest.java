import manager.TaskManager;
import org.junit.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TaskManagerTest<T extends TaskManager> {

    abstract T createmanager();


    @Test
    public void addTasksInFileManagerAndManager() {
        T manager = createmanager();

        Task task1 = new Task("Помыть машину", "Заехать на мойку");
        int taskId1 = manager.addTask(task1);

        Epic epic1 = new Epic("Переезд", "Собираем вещи");
        int epicId1 = manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Сложить все в коробки", "5 картонных коробок", epicId1);
        int subtaskId1 = manager.addSubtask(subtask1);

        //сравнение добавленных
        assertEquals(task1, manager.getTask(taskId1));
        assertEquals(epic1, manager.getEpicById(epicId1));
        assertEquals(subtask1, manager.getSubtaskById(subtaskId1));

    }

    @Test
    public void generateDifferentTaskType() {
        T manager = createmanager();

        Task task1 = new Task("Помыть машину", "Заехать на мойку");
        int taskId1 = manager.addTask(task1);

        Epic epic1 = new Epic("Переезд", "Собираем вещи");
        int epicId1 = manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Сложить все в коробки", "5 картонных коробок", epicId1);
        int subtaskId1 = manager.addSubtask(subtask1);

        assertNotEquals("Тип задач должен быть разный", task1, epic1);
        assertNotEquals("Тип задач должен быть разный", task1, subtask1);

        //задача добавлена и находится по айди
        assertNotNull(manager.getTask(taskId1));
        assertNotNull(manager.getEpicById(epicId1));
        assertNotNull(manager.getSubtaskById(subtaskId1));
    }

    @Test
    public void managersReturnTaskManagerNotNull() {
        T manager = createmanager();
        assertNotNull("Не должен быть Null", manager);
    }

    @Test
    public void notConflictIDGeneratedIDAndGivenID() throws IOException {
        T manager = createmanager();

        Task task = new Task("Помыть машину", "Заехать на мойку");
        task.setId(0);
        manager.addTask(task);

        Task taskG = new Task("Помыть кота", "Намылить кота");
        manager.addTask(taskG);

        assertNotEquals("не должны совпадать", task.getId(), taskG.getId());

    }

    @Test
    public void taskNotChangeAfterAddManager() {
        T manager = createmanager();

        Task task = new Task("Помыть машину", "Заехать на мойку");
        task.setId(10);
        manager.addTask(task);

        assertEquals(task, manager.getTask(task.getId()));
        assertEquals(task.getTitle(), manager.getTask(task.getId()).getTitle());
        assertEquals(task.getDescription(), manager.getTask(task.getId()).getDescription());
        assertEquals(task.getStatus(), manager.getTask(task.getId()).getStatus());
        assertEquals(task.getId(), manager.getTask(task.getId()).getId());

    }

    @Test
    public void taskOverlayInTime() {
        T manager = createmanager();

        Task task1 = new Task("Задача 1", " ",
                Duration.ofMinutes(90), LocalDateTime.of(2024, 6, 3, 9, 0));

        Task task2 = new Task("Задача 2", " ",
                Duration.ofHours(2), LocalDateTime.of(2024, 6, 3, 11, 0));


        Task task3 = new Task("Задача 3", " ",
                Duration.ofMinutes(45), LocalDateTime.of(2024, 6, 3, 11, 30));


        assertFalse(manager.isTaskOverlay(task1, task2));
        assertTrue(manager.isTaskOverlay(task2, task3));

        int task1Id = manager.addTask(task1);
        int task2Id = manager.addTask(task2);
        int task3Id = manager.addTask(task3);

        assertNull(task3.getStartTime());
    }
}