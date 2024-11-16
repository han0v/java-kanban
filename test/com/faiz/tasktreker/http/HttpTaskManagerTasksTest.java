package com.faiz.tasktreker.http;

import com.faiz.tasktreker.adapters.DurationAdapter;
import com.faiz.tasktreker.adapters.LocalDateTimeAdapter;
import com.faiz.tasktreker.model.Task;
import com.faiz.tasktreker.service.TaskManager;
import com.faiz.tasktreker.service.InMemoryTaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    public static final String TASK_URL = "http://localhost:8080/tasks";

    public HttpTaskManagerTasksTest() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson =   new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        manager.delAll();
        taskServer.start();
        TimeUnit.SECONDS.sleep(1); // Даем серверу время на запуск
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.of(2024, 8, 3, 12, 0), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.of(2024, 8, 3, 12, 0), Duration.ofMinutes(5));
        manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);
        assertEquals(200, response.statusCode(), "Статус кода не равен 200");
        assertFalse(response.body().isEmpty(), "Ответ тела пустой");
        assertEquals(1, actual.size());
    }


    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.now(), Duration.ofMinutes(5));
        manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Статус кода не равен 200");

        List<Task> tasksFromManager = manager.getTasks();
        assertTrue(tasksFromManager.isEmpty(), "Задачи не удалены.");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.of(2024, 8, 3, 12, 0), Duration.ofMinutes(5));
        manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?id=" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Статус кода не равен 200");
        assertFalse(response.body().isEmpty(), "Ответ тела пустой");
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);
        assertNotNull(actual);
        assertEquals(task, actual);
    }
}