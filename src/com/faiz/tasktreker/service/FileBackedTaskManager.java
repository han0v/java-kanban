package com.faiz.tasktreker.service;


import com.faiz.tasktreker.exceptions.ManagerSaveException;
import com.faiz.tasktreker.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String file;
    private static final String HEADER = "id,type,name,status,description,epic, starTime, duration";

    public FileBackedTaskManager(String filename) {
        super();
        this.file = filename;
        this.uniqId = 1;
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

        // Определяем индексы
        final int ID_INDEX = 0;
        final int TYPE_INDEX = 1;
        final int NAME_INDEX = 2;
        final int STATUS_INDEX = 3;
        final int DESCRIPTION_INDEX = 4;
        final int EPIC_INDEX = 5;

        int id;
        try {
            id = Integer.parseInt(params[ID_INDEX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID должен быть числом: " + params[ID_INDEX]);
        }

        TaskType taskType = TaskType.valueOf(params[TYPE_INDEX]);
        Status status = Status.valueOf(params[STATUS_INDEX]);

        LocalDateTime startTime = null;
        Duration duration = null;

        // Проверка наличия значений для startTime и duration
        if (params[6] != null && !params[6].isEmpty() && !params[6].equals("null")) {
            startTime = LocalDateTime.parse(params[6]);
        }
        if (params[7] != null && !params[7].isEmpty() && !params[7].equals("null")) {
            duration = Duration.ofMinutes(Long.parseLong(params[7]));
        }

        switch (taskType) {
            case EPIC:
                Epic epic = new Epic(params[NAME_INDEX], params[DESCRIPTION_INDEX]);
                epic.setId(id);
                epic.setStatus(status);
                epic.setStartTime(startTime); // Устанавливаем startTime, если он не null
                epic.setDuration(duration); // Устанавливаем duration, если он не null
                return epic;

            case SUBTASK:
                SubTask subtask = new SubTask(params[NAME_INDEX], params[DESCRIPTION_INDEX],
                        Integer.parseInt(params[EPIC_INDEX]), startTime, duration);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;

            case TASK:
                Task task = new Task(params[NAME_INDEX], params[DESCRIPTION_INDEX], startTime, duration);
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
            int maxId = 0;

            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith(HEADER)) {
                    continue;
                }

                try {
                    Task task = manager.fromString(line);
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }

                    switch (task.getType()) {
                        case EPIC:
                            manager.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            SubTask subTask = (SubTask) task;
                            Epic epic = manager.epics.get(subTask.getEpicId());
                            if (epic != null) {
                                manager.subTasks.put(subTask.getId(), subTask);
                                epic.createSubTask(subTask);
                            }
                            break;
                        case TASK:
                            manager.tasks.put(task.getId(), task);
                            break;
                        default:
                            throw new IllegalArgumentException("Неизвестный тип задачи: " + task.getType());
                    }
                } catch (IllegalArgumentException e) {
                    // Пробрасываем исключение выше
                    throw new ManagerSaveException("Ошибка при восстановлении задачи: " + e.getMessage(), e);
                }
            }
            manager.uniqId = maxId + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + file.getAbsolutePath(), e);
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
