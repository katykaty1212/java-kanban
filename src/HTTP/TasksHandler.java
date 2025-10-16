package HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Status;
import task.Task;

import java.io.IOException;
import java.util.Map;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":

                    if (pathParts.length == 2) {
                        String jsonString = gson.toJson(manager.getAllTasks());
                        sendText(exchange, jsonString, 200);
                    } else if (pathParts.length == 3) {
                        Task task = manager.getTask(Integer.parseInt(pathParts[2]));
                        if (task != null) {
                            String jsonString = gson.toJson(task);
                            sendText(exchange, jsonString, 200);
                        } else {
                            sendNotFound(exchange, "Задача не найдена"); //404
                        }
                    }

                    break;
                case "POST":
                    String taskInBody = readBody(exchange);
                    Task task = gson.fromJson(taskInBody, Task.class);

                    if (task.getStartTime() != null) {
                        if (manager.isTaskOverlayInPrioritizedList(task)) {
                            sendHasOverlaps(exchange, "Новая задача пересекается по времени с существующей.");
                            return;//406
                        }
                    }

                    if (task.getId() == 0) {
                        task.setStatus(Status.NEW);
                        int newId = manager.addTask(task);
                        String jsonString = gson.toJson(Map.of("id", newId));
                        sendText(exchange, jsonString, 201);
                    } else {

                        if (task.getStatus() == null) {
                            task.setStatus(Status.NEW);
                        }

                        int id = manager.updateTask(task, task.getStatus());
                        String jsonString = gson.toJson(Map.of("id", id));
                        sendText(exchange, jsonString, 201);
                    }
                    break;


                case "DELETE":
                    if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        Task taskToDel = manager.getTask(taskId);

                        if (taskToDel != null) {
                            manager.deleteTask(taskId);
                            sendText(exchange, "Задача с ID " + pathParts[2] + " удалена.", 200);
                        } else {
                            sendNotFound(exchange, "Задача с ID " + pathParts[2] + " не найдена.");
                        }
                    }
                    break;
                default:
                    sendNotFound(exchange, "Объект не был найден.");

            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}