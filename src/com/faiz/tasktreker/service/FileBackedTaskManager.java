package com.faiz.tasktreker.service;


import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;
import com.faiz.tasktreker.model.TaskType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String file;

    public FileBackedTaskManager(String filename) {
        super();
        this.file = filename;
    }


    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : super.getTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }

            for (Epic epic : super.getEpics()) {
                writer.write(epic.toString());
                writer.newLine();
            }

            for (SubTask subtask : super.getSubTasks()) {
                writer.write(subtask.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении состояния в файл: " + file, e);
        }
    }


    private Task fromString(String value) {
        String[] params = value.split(",");

        int id;
        try {
            id = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID должен быть числом: " + params[0]);
        }

        TaskType taskType = TaskType.valueOf(params[1]);

        switch (taskType) {
            case EPIC:
                Epic epic = new Epic(params[4], params[2]);
                epic.setId(id);
                return epic;

            case SUBTASK:
                SubTask subtask = new SubTask(params[4], params[2], Integer.parseInt(params[5])); // params[5] - ID эпика
                subtask.setId(id);
                return subtask;

            case TASK:
                Task task = new Task(params[4], params[2]);
                task.setId(id);
                return task;

            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + taskType);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());

        try {
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("id,type")) {
                    continue;  // Пропускаем пустые строки и заголовок
                }

                try {
                    // Теперь мы должны убедиться, что ID правильно извлекается
                    Task task = manager.fromString(line);
                    manager.createTask(task); // Добавляем задачу, извлекшуюся из строки
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка при восстановлении задачи: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return manager;
    }


    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public SubTask createSubTask(SubTask subtask) {
        SubTask newSubtask = super.createSubTask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void delById(int id) {
        super.delById(id);
        save();
    }

    @Override
    public void delAllTasks() {
        super.delAllTasks();
        save();
    }

    @Override
    public void delAllEpics() {
        super.delAllEpics();
        save();
    }

    @Override
    public void delAllSubTasks() {
        super.delAllSubTasks();
        save();
    }

    @Override
    public Task getById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}
