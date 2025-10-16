package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        String[] pathParts = path.split("/");

        switch (method) {
            case "GET":

                if (pathParts.length == 2) {
                    String jsonString = gson.toJson(manager.getAllSubtask());
                    sendText(exchange, jsonString, 200);
                } else if (pathParts.length == 3) {
                    Subtask subtask = manager.getSubtaskById(Integer.parseInt(pathParts[2]));
                    if (subtask != null) {
                        String jsonString = gson.toJson(subtask);
                        sendText(exchange, jsonString, 200);
                    } else {
                        sendNotFound(exchange, "Задача не найдена"); //404
                    }
                }

                break;
            case "POST":
                String subtaskInBody = readBody(exchange);
                Subtask subtask = gson.fromJson(subtaskInBody, Subtask.class);
                Epic epic = manager.getEpicById(subtask.getEpicId());

                if (epic.getSubtaskIds() == null) {
                    List<Integer> newList = new ArrayList<>();
                    epic.setSubtaskIds(newList);
                }

                if (subtask.getStartTime() != null) {
                    if (manager.isTaskOverlayInPrioritizedList(subtask)) {
                        sendHasOverlaps(exchange, "Новая задача пересекается по времени с существующей.");
                        return;//406
                    }
                }

                if (subtask.getId() == 0) {
                    subtask.setStatus(Status.NEW);


                    int newId = manager.addSubtask(subtask);
                    String jsonString = gson.toJson(Map.of("id", newId));
                    sendText(exchange, jsonString, 201);
                } else {

                    if (subtask.getStatus() == null) {
                        subtask.setStatus(Status.NEW);
                    }

                    int id = manager.updateSubtask(subtask, subtask.getStatus());
                    String jsonString = gson.toJson(Map.of("id", id));
                    sendText(exchange, jsonString, 201);
                }

                break;
            case "DELETE":

                if (pathParts.length == 3) {
                    Subtask subtaskToDel = manager.getSubtaskById(Integer.parseInt(pathParts[2]));
                    if (subtaskToDel != null) {
                        manager.deleteSubtask(Integer.parseInt(pathParts[2]));
                        sendText(exchange, "Задача с ID" + pathParts[2] + "удалена.", 200);
                    } else {
                        sendNotFound(exchange, "Задача с ID" + pathParts[2] + "не найдена.");
                    }
                }

                break;
            default:
                sendNotFound(exchange, "Объект не был найден.");

        }
    }
}
