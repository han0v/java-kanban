package com.faiz.tasktreker.http;

import com.faiz.tasktreker.model.Task;
import com.faiz.tasktreker.service.TaskManager;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
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
        if (query == null) {
            try {
                System.out.println(taskManager.getTasks());
                String jsonString = gson.toJson(taskManager.getTasks());
                sendText(httpExchange, jsonString);
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(httpExchange, 500, "Ошибка при сериализации задач.");
            }
        } else {
            try {
                Integer id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                Task task = taskManager.getTaskById(id);
                if (task != null) {
                    String taskJson = gson.toJson(task);
                    sendText(httpExchange, taskJson);
                } else {
                    sendNotFound(httpExchange, "Задача не существует!");
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
            Task task = gson.fromJson(bodyRequest, Task.class);
            Integer id = task.getId();
            if (id == null) {
                taskManager.updateTask(task);
                sendResponse(httpExchange, 201, "Задача успешно обновлена.");
            } else {
                Optional<Task> existingTask = taskManager.getTasks().stream()
                        .filter(t -> t.getId().equals(id))
                        .findFirst();

                if (!existingTask.isPresent()) {
                    taskManager.createTask(task);
                    sendResponse(httpExchange, 201, "Задача успешно создана.");
                } else {
                    sendHasInteractions(httpExchange, "Задача с данным ID уже существует.");
                }
            }
        } catch (JsonSyntaxException e) {
            sendNotFound(httpExchange, "Oбъект не был найден;");
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
            sendText(httpExchange, "Задача успешно удалена.");
        } catch (StringIndexOutOfBoundsException e) {
            sendResponse(httpExchange, 400, "Обязательный параметр id не может быть пустым");
        } catch (NumberFormatException e) {
            sendResponse(httpExchange, 400, "Неверный формат для параметра id");
        }
    }
}