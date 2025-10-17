import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerSubtasksTest extends HttpTaskManagerBaseTest {

    @Test
    public void addSubtaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT5M"), LocalDateTime.now());
        String taskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpResponse<String> response = sendRequest("POST", "/subtasks", taskJson);

        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void getAllSubtasksTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 1", "Testing subtask 1", epicId, Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);

        HttpResponse<String> response = sendRequest("GET", "/subtasks", "");

        assertEquals(200, response.statusCode());
        assertNotNull(response.body(), "Тело ответа не должно быть null");

        class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
        }

        List<Subtask> tasks = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());

        assertNotNull(tasks, "Задачи из тела не добавились в список задач.");
        assertEquals(2, tasks.size(), "Не верное кол-во задач.");


    }

    @Test
    public void getSubtaskByIdTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 1", "Testing subtask 1", epicId, Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);

        HttpResponse<String> response = sendRequest("GET", "/subtasks/3", "");

        assertEquals(200, response.statusCode());
        assertNotNull(response.body(), "Тело ответа не должно быть null");

        Subtask subtaskResponse = gson.fromJson(response.body(), Subtask.class);

        assertEquals(subtask1.getTitle(), subtaskResponse.getTitle(), "Задачи должны быть с одинаковым названием.");
        assertEquals(subtask1.getId(), subtaskResponse.getId(), "ID должен совпадать.");
    }

    @Test
    public void updateSubtaskTest() throws IOException, InterruptedException {

        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 1", "Testing subtask 1", epicId, Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));

        manager.addSubtask(subtask);

        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        subtask1.setId(2);

        String taskJson = gson.toJson(subtask1);

        // создаём HTTP-клиент и запрос
        HttpResponse<String> response = sendRequest("POST", "/subtasks", taskJson);

        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void deleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 1", "Testing subtask 1", epicId, Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);

        HttpResponse<String> response = sendRequest("DELETE", "/subtasks/2", "");

        assertEquals(200, response.statusCode());

        assertEquals(1, manager.getAllSubtask().size(), "Некорректное количество задач");
        assertEquals(manager.getSubtaskById(3).getTitle(), subtask1.getTitle(), "Некорректное имя задачи ");
    }
}