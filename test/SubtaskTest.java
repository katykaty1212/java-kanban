import org.junit.jupiter.api.Test;
import task.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", 1);

        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны");
    }
}