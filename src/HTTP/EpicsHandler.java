package HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.util.Map;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicsHandler(TaskManager manager) {
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
                    String jsonString = gson.toJson(manager.getAllEpics());
                    sendText(exchange, jsonString, 200);
                } else if (pathParts.length == 3) {
                    Epic epic = manager.getEpicById(Integer.parseInt(pathParts[2]));
                    if (epic != null) {
                        String jsonString = gson.toJson(epic);
                        sendText(exchange, jsonString, 200);
                    } else {
                        sendNotFound(exchange, "Задача не найдена"); //404
                    }
                } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
                    Epic epic = manager.getEpicById(Integer.parseInt(pathParts[2]));
                    if (epic != null) {
                        String jsonString = gson.toJson(manager.getAllEpicsSubtasks(epic.getId()));
                        sendText(exchange, jsonString, 200);
                    } else {
                        sendNotFound(exchange, "Задача не найдена"); //404
                    }
                }

                break;
            case "POST":
                String epicInBody = readBody(exchange);
                Epic epic = gson.fromJson(epicInBody, Epic.class);

                if (epic.getId() == 0) {
                    int newId = manager.addEpic(epic);
                    String jsonString = gson.toJson(Map.of("id", newId));
                    sendText(exchange, jsonString, 201);
                }

                break;
            case "DELETE":

                if (pathParts.length == 3) {
                    Epic epicToDel = manager.getEpicById(Integer.parseInt(pathParts[2]));
                    if (epicToDel != null) {
                        manager.deleteEpic(Integer.parseInt(pathParts[2]));
                        sendText(exchange, "Задача с ID" + pathParts[2] + "удалена c подзадачами.", 200);
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
