import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest extends HttpTaskManagerBaseTest {

    @Test
    public void addTaskTest() throws IOException, InterruptedException {

        Task task = new Task("Test 2", "Testing task 2", Duration.parse("PT5M"), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpResponse<String> response = sendRequest("POST", "/tasks", taskJson);

        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void getAllTasksTest() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Task task1 = new Task("Test 2", "Testing task 2", Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addTask(task);
        manager.addTask(task1);

        HttpResponse<String> response = sendRequest("GET", "/tasks", "");

        assertEquals(200, response.statusCode());
        assertNotNull(response.body(), "Тело ответа не должно быть null");

        class TaskListTypeToken extends TypeToken<List<Task>> {
        }

        List<Task> tasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasks, "Задачи из тела не добавились в список задач.");
        assertEquals(2, tasks.size(), "Не верное кол-во задач.");
    }

    @Test
    public void getTaskByIdTest() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Task task1 = new Task("Test 2", "Testing task 2", Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addTask(task);
        manager.addTask(task1);

        HttpResponse<String> response = sendRequest("GET", "/tasks/2", "");

        assertEquals(200, response.statusCode());
        assertNotNull(response.body(), "Тело ответа не должно быть null");

        Task taskResponse = gson.fromJson(response.body(), Task.class);

        assertEquals(task1.getTitle(), taskResponse.getTitle(), "Задачи должны быть с одинаковым названием.");
        assertEquals(task1.getId(), taskResponse.getId(), "ID должен совпадать.");
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));

        manager.addTask(task);

        Task task1 = new Task("Test 2", "Testing task 2", Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        task1.setId(1);

        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpResponse<String> response = sendRequest("POST", "/tasks", taskJson);

        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Duration.parse("PT5M"),
                LocalDateTime.of(2023, 12, 10, 14, 30, 0));
        Task task1 = new Task("Test 2", "Testing task 2", Duration.parse("PT30M"),
                LocalDateTime.of(2023, 11, 10, 13, 20, 0));

        manager.addTask(task);
        manager.addTask(task1);

        HttpResponse<String> response = sendRequest("DELETE", "/tasks/1", "");

        assertEquals(200, response.statusCode());

        assertEquals(1, manager.getAllTasks().size(), "Некорректное количество задач");
        assertEquals(manager.getTask(2).getTitle(), task1.getTitle(), "Некорректное имя задачи ");
    }
}