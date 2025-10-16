package HTTP;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final int PORT = 8080;
    private final TaskManager manager;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.manager = Managers.getDefault();
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));

    }

    public void startServer() {
        httpServer.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    public void stopServer(){
        httpServer.stop(1);
    }

    public TaskManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer();
            server.startServer();


        } catch (IOException e) {
            System.out.println("Сервер не запущен" + e.getMessage());
        }

    }
}