package com.faiz.tasktreker.http;

import com.faiz.tasktreker.service.TaskManager;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer httpServer;
    private static final int PORT = 8080;
    TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;

        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("Сервер остановлен.");
    }
}