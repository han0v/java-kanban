package com.faiz.tasktreker.http;

import com.faiz.tasktreker.service.TaskManager;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager managers) {
        super(managers);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        switch (Methods.valueOf(method)) {
            case Methods.GET:
                handleGet(httpExchange);
                break;
            default:
                sendResponse(httpExchange, 405, "Неподдерживаемый метод: " + method);
                break;
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        try {
            String jsonString = gson.toJson(taskManager.getHistory());
            System.out.println("GET HISTORY: " + jsonString); // исправлено имя
            sendText(httpExchange, jsonString);
        } catch (Exception e) {
            sendNotFound(httpExchange, "Ошибка при получении истории задач: " + e.getMessage());
        }
    }
}
