import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.DurationTypeAdapter;
import controller.InMemoryTaskManager;
import controller.LocalDateTimeAdapter;
import controller.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandlerTest {

    TaskManager taskManager;
    HttpTaskServer taskServer;
    HttpClient httpClient;
    Gson gson;

    @BeforeEach
    void newManager() throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    void serverStop() {
        taskServer.stop();
    }
}
