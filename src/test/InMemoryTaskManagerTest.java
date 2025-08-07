package test;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.Subtask;
import task.Task;

import static org.junit.Assert.*;

public class InMemoryTaskManagerTest {
    TaskManager manager;


    @Test
    public void tasksAddInManagerAndFindID() {
        manager = Managers.getDefault();

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
        manager = Managers.getDefault();

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
        manager = Managers.getDefault();
        //создание менеджера
        assertNotNull("не должен быть Null", manager);
    }

    @Test
    public void notConflictIDGeneratedIDAndGivenID() {
        manager = Managers.getDefault();
        Task task = new Task("Помыть машину", "Заехать на мойку");
        task.setId(0);
        manager.addTask(task);

        Task taskG = new Task("Помыть кота", "Намылить кота");
        manager.addTask(taskG);

        assertNotEquals("не должны совпадать", task.getId(), taskG.getId());

    }

    @Test
    public void taskNotChangeAfterAddManager() {
        manager = Managers.getDefault();

        Task task = new Task("Помыть машину", "Заехать на мойку");
        task.setId(10);
        manager.addTask(task);

        assertEquals(task, manager.getTask(task.getId()));
        assertEquals(task.getTitle(), manager.getTask(task.getId()).getTitle());
        assertEquals(task.getDescription(), manager.getTask(task.getId()).getDescription());
        assertEquals(task.getStatus(), manager.getTask(task.getId()).getStatus());
        assertEquals(task.getId(), manager.getTask(task.getId()).getId());

    }
}