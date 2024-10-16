package com.faiz.tasktreker.service;


import com.faiz.tasktreker.exceptions.ManagerSaveException;
import com.faiz.tasktreker.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String file;
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(String filename) {
        super();
        this.file = filename;
    }


    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER);
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
        Status status = Status.valueOf(params[3]);

        switch (taskType) {
            case EPIC:
                Epic epic = new Epic(params[2], params[4]);
                epic.setId(id);
                epic.setStatus(status);
                return epic;

            case SUBTASK:
                SubTask subtask = new SubTask(params[2], params[4], Integer.parseInt(params[5]));
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;

            case TASK:
                Task task = new Task(params[2], params[4]);
                task.setId(id);
                task.setStatus(status);
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
                if (line.trim().isEmpty() || line.startsWith(HEADER)) {
                    continue;
                }

                try {
                    Task task = manager.fromString(line);

                    // Прямое добавление задач в зависимости от типа
                    if (task instanceof Epic) {
                        manager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof SubTask) {
                        SubTask subTask = (SubTask) task;
                        Epic epic = manager.epics.get(subTask.getEpicId());
                        if (epic != null) {
                            manager.subTasks.put(subTask.getId(), subTask);
                            epic.createSubTask(subTask);
                        }
                    } else if (task instanceof Task) {
                        manager.tasks.put(task.getId(), task);
                    }

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
