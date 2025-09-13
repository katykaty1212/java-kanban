import manager.FileBackedTaskManager;
import org.junit.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileBackedTaskManagerTest {


    @Test
    public void tasksAddInFileManager() throws IOException {

        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task1 = new Task("Помыть машину", "Заехать на мойку");
        int taskId1 = manager.addTask(task1);

        Epic epic1 = new Epic("Переезд", "Собираем вещи");
        int epicId1 = manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Сложить все в коробки", "5 картонных коробок", epicId1);
        int subtaskId1 = manager.addSubtask(subtask1);

        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(manager1);

        assertEquals(manager.getAllTasks().size(), manager1.getAllTasks().size());
        assertEquals(manager.getAllEpics().size(), manager1.getAllEpics().size());
        assertEquals(manager.getAllSubtask().size(), manager1.getAllSubtask().size());

        assertEquals(manager.getTask(taskId1), manager1.getTask(taskId1));
        assertEquals(manager.getTask(taskId1).getTitle(), manager1.getTask(taskId1).getTitle());
        assertEquals(manager.getEpicById(epicId1).getTitle(), manager1.getEpicById(epicId1).getTitle());
        assertEquals(manager.getSubtaskById(subtaskId1).getTitle(), manager1.getSubtaskById(subtaskId1).getTitle());

        assertTrue(manager1.getEpicById(epicId1).getSubtaskIds().contains(subtaskId1));
    }
}