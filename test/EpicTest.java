package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.Test;
import task.Epic;
import task.Status;
import task.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @Test
    public void updateEpicStatus() {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Тест название", "Тест описание");
        int epicId = manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Первая подзадача эпика", epicId);
        Subtask subtask2 = new Subtask("Подзадача 2", "Вторая подзадача эпика", epicId);
        Subtask subtask3 = new Subtask("Подзадача 3", "Третья подзадача эпика", epicId);

        int subtask1Id = manager.addSubtask(subtask1);
        int subtask2Id = manager.addSubtask(subtask2);
        int subtask3Id = manager.addSubtask(subtask3);

        assertEquals(Status.NEW, epic.getStatus());

        manager.updateSubtask(subtask1, Status.DONE);
        manager.updateSubtask(subtask2, Status.DONE);
        manager.updateSubtask(subtask3, Status.DONE);

        assertEquals(Status.DONE, epic.getStatus());

        manager.updateSubtask(subtask1, Status.NEW);
        manager.updateSubtask(subtask2, Status.DONE);
        manager.updateSubtask(subtask3, Status.DONE);

        assertEquals(Status.NEW, epic.getStatus());

        manager.updateSubtask(subtask1, Status.NEW);
        manager.updateSubtask(subtask2, Status.DONE);
        manager.updateSubtask(subtask3, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());

    }

    @Test
    public void connectedEpicAndSubtask() {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Тест название", "Тест описание");
        int epicId = manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Первая подзадача эпика", epicId);
        Subtask subtask2 = new Subtask("Подзадача 2", "Вторая подзадача эпика", epicId);
        Subtask subtask3 = new Subtask("Подзадача 3", "Третья подзадача эпика", epicId);

        int subtask1Id = manager.addSubtask(subtask1);
        int subtask2Id = manager.addSubtask(subtask2);
        int subtask3Id = manager.addSubtask(subtask3);


        assertEquals(subtask1.getEpicId(), epic.getId());
        assertEquals(subtask2.getEpicId(), epic.getId());
        assertEquals(subtask3.getEpicId(), epic.getId());
        assertNotNull(subtask1.getEpicId());
        assertNotNull(subtask2.getEpicId());
        assertNotNull(subtask2.getEpicId());
    }

    @Test
    public void updateEpicEndStartTimeAndDuration() {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Эпик", "Эпик с тремя подзадачами");
        int epicId = manager.addEpic(epic);

        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertNull(epic.getDuration());


        Subtask subtask1 = new Subtask("Подзадача 1", "Первая подзадача эпика", epicId,
                Duration.ofMinutes(90), LocalDateTime.of(2024, 6, 3, 9, 0));

        Subtask subtask2 = new Subtask("Подзадача 2", "Вторая подзадача эпика", epicId,
                Duration.ofHours(2), LocalDateTime.of(2024, 6, 3, 11, 0));


        Subtask subtask3 = new Subtask("Подзадача 3", "Третья подзадача эпика", epicId,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 6, 3, 14, 30));


        int subtask1Id = manager.addSubtask(subtask1);
        int subtask2Id = manager.addSubtask(subtask2);
        int subtask3Id = manager.addSubtask(subtask3);

        assertNotNull(epic.getStartTime());
        assertNotNull(epic.getEndTime());
        assertNotNull(epic.getDuration());

        assertEquals(epic.getDuration(),
                subtask1.getDuration().plus(subtask2.getDuration().plus(subtask3.getDuration())));

    }
}