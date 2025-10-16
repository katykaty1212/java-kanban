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

public class HttpTaskManagerEpicsTest extends HttpTaskManagerBaseTest {

    @Test
    public void addEpicTest() throws IOException, InterruptedException {

        Epic epic = new Epic("Test 2", "Testing epic 2");
        String taskJson = gson.toJson(epic);

        HttpResponse<String> response = sendRequest("POST", "/epics", taskJson);

        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void getAllEpicsTest() throws IOException, InterruptedException {

        Epic epic = new Epic("Test 1", "Testing epic 1");
        Epic epic1 = new Epic("Test 2", "Testing epic 2");

        manager.addEpic(epic);
        manager.addEpic(epic1);

        HttpResponse<String> response = sendRequest("GET", "/epics", "");

        assertEquals(200, response.statusCode());
        assertNotNull(response.body(), "Тело ответа не должно быть null");

        class EpicListTypeToken extends TypeToken<List<Epic>> {
        }

        List<Epic> tasks = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertNotNull(tasks, "Задачи из тела не добавились в список задач.");
        assertEquals(2, tasks.size(), "Не верное кол-во задач.");
    }

    @Test
    public void getEpicByIdTest() throws IOException, InterruptedException {

        Epic epic = new Epic("Test 1", "Testing epic 1");
        Epic epic1 = new Epic("Test 2", "Testing epic 2");

        manager.addEpic(epic);
        manager.addEpic(epic1);

        HttpResponse<String> response = sendRequest("GET", "/epics/2", "");

        assertEquals(200, response.statusCode());
        assertNotNull(response.body(), "Тело ответа не должно быть null");

        Epic taskResponse = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic1.getTitle(), taskResponse.getTitle(), "Задачи должны быть с одинаковым названием.");
        assertEquals(epic1.getId(), taskResponse.getId(), "ID должен совпадать.");
    }

    @Test
    public void getEpicSubtasksTest() throws IOException, InterruptedException {

        Epic epic = new Epic("Epic", "Epic desk");
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask("Test 1", "Testing subtask 1", epicId, Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Subtask subtask1 = new Subtask("Test 2", "Testing subtask 2", epicId, Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);


        String taskJson = gson.toJson(manager.getAllEpicsSubtasks(epicId));

        HttpResponse<String> response = sendRequest("GET", "/epics/1/subtasks", taskJson);

        assertEquals(200, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getAllEpicsSubtasks(1);

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {

        Epic epic = new Epic("Test 1", "Testing epic 1");
        Epic epic1 = new Epic("Test 2", "Testing epic 2");

        manager.addEpic(epic);
        manager.addEpic(epic1);

        HttpResponse<String> response = sendRequest("DELETE", "/epics/1", "");

        assertEquals(200, response.statusCode());

        assertEquals(1, manager.getAllEpics().size(), "Некорректное количество задач");
        assertEquals(manager.getEpicById(2).getTitle(), epic1.getTitle(), "Некорректное имя задачи ");
    }
}