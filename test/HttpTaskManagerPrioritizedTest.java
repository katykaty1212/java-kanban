import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerPrioritizedTest extends HttpTaskManagerBaseTest {

    @Test
    public void getHttpPrioritizedTest() {

        Task task = new Task("Test 1", "Testing task 1", Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 14, 14, 30, 0));
        Task task1 = new Task("Test 2", "Testing task 2", Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 1", "Testing subtask 1", epicId, Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 15, 14, 30, 0));
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 13, 18, 20, 0));

        manager.addTask(task);
        manager.addTask(task1);
        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);

        List<Task> prioritized = manager.getPrioritizedTasks();

        assertNotNull(prioritized, "Задачи не возвращаются");
        assertEquals(4, prioritized.size(), "Некорректное количество задач");
    }
}