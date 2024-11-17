package com.faiz.tasktreker.http;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.service.TaskManager;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        switch (Methods.valueOf(method)) {
            case Methods.GET:
                handleGet(httpExchange);
                break;

            case Methods.POST:
                handlePost(httpExchange);
                break;

            case Methods.DELETE:
                handleDelete(httpExchange);
                break;

            default:
                sendResponse(httpExchange, 405, "Неподдерживаемый метод: " + method);
                break;
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String path = String.valueOf(httpExchange.getRequestURI());
        String[] pathParts = path.split("/");

        if (pathParts.length >= 4 && "subtasks".equals(pathParts[pathParts.length - 1])) {
            try {
                Integer epicId = Integer.parseInt(pathParts[pathParts.length - 2]);
                if (taskManager.getEpicById(epicId) == null) {
                    sendNotFound(httpExchange, "Эпик с ID " + epicId + " не найден.");
                } else {
                    String jsonString = gson.toJson(taskManager.getSubTaskList(epicId));
                    System.out.println("GET SUBTASKS FOR EPIC ID " + epicId + ": " + jsonString);
                    sendText(httpExchange, jsonString);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                sendResponse(httpExchange, 400, "Неверный формат идентификатора эпика или запроса.");
            }
        } else if (query == null) {
            String jsonString = gson.toJson(taskManager.getEpics());
            System.out.println("GET EPICS: " + jsonString);
            sendText(httpExchange, jsonString);
        } else {
            try {
                Integer id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    String taskJson = gson.toJson(epic);
                    sendText(httpExchange, taskJson);
                } else {
                    sendNotFound(httpExchange, "Эпик не существует!");
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                sendResponse(httpExchange, 400, "Неверный формат идентификатора эпика или запроса.");
            }
        }
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String bodyRequest = readText(httpExchange);

        if (bodyRequest == null || bodyRequest.isEmpty()) {
            sendResponse(httpExchange, 400, "Тело запроса не может быть пустым.");
            return;
        }
        try {
            Epic epic = gson.fromJson(bodyRequest, Epic.class);
            Epic epicCreated = taskManager.createEpic(epic);
            System.out.println("CREATED EPIC: " + epicCreated);
            sendResponse(httpExchange, 201, "Эпик создан.");
        } catch (JsonSyntaxException e) {
            sendResponse(httpExchange, 400, "Ошибка синтаксиса в JSON: " + e.getMessage());
        }
    }

    private void handleDelete(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null || query.isEmpty()) {
            sendResponse(httpExchange, 400, "Обязательный параметр id не может быть пустым.");
            return;
        }
        try {
            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
            taskManager.delById(id);
            sendText(httpExchange, "Эпик успешно удален.");
        } catch (NumberFormatException e) {
            sendResponse(httpExchange, 400, "Неверный формат идентификатора эпика.");
        }
    }
}
