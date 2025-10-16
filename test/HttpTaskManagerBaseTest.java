import http.GsonCreate;
import http.HttpTaskServer;
import com.google.gson.Gson;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class HttpTaskManagerBaseTest {

    HttpTaskServer taskServer;
    TaskManager manager;
    Gson gson = GsonCreate.createGson();

    @BeforeEach
    public void setUp() throws IOException {
        taskServer = new HttpTaskServer();
        manager = taskServer.getManager();

        manager.deleteAllTask();
        manager.deleteAllSubtask();
        manager.deleteAllEpics();
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    public HttpResponse<String> sendRequest(String method, String path, String body)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080" + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}