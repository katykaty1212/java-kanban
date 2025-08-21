package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.Test;
import task.Epic;
import task.Status;
import task.Subtask;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    public void createEpicWithTitleAndDescriptionAndStatusAndId() {
        Epic epic = new Epic("Тест название", "Тест описание");

        assertEquals("Тест название", epic.getTitle());
        assertEquals("Тест описание", epic.getDescription());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(0, epic.getId());
    }

    @Test
    public void epicsWithSameIdEqual() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым ID должны быть равны");
    }

    @Test
    public void setIdForEpic() {
        Epic epic = new Epic("Тест название", "Тест описание");

        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, epic.getStatus());

    }

    @Test
    public void epicNotAddInEpicAsSubtask() {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Тест название", "Тест описание");
        int epicId = manager.addEpic(epic);

        Subtask subtaskEpic = new Subtask("Добавим эпик", "как подзадачу", epicId);
        subtaskEpic.setId(epicId);
        manager.addSubtask(subtaskEpic);

        assertFalse(epic.getSubtaskIds().contains(epicId));
    }
}