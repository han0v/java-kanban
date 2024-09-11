package com.faiz.tasktreker.service;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int uniqId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {

    }


    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }


    @Override
    public void delAll() {
        boolean taskEmpty = tasks.isEmpty();
        boolean epicEmpty = epics.isEmpty();
        boolean subTaskEmpty = subTasks.isEmpty();

        if (taskEmpty && epicEmpty && subTaskEmpty) {
            System.out.println("Нет задач для удаления! Трекер задач пуст!");
            return;
        }
        if (!taskEmpty) {
            tasks.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
        if (!epicEmpty) {
            epics.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
        if (!subTaskEmpty) {
            subTasks.clear();
            System.out.println("Список подзадач очищен!");
        } else {
            System.out.println("Список подзадач пуст!");
        }
    }

    @Override
    public void delAllTasks() {
        boolean taskEmpty = tasks.isEmpty();
        if (!taskEmpty) {
            tasks.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
    }

    @Override
    public void delAllEpics() {
        boolean epicEmpty = epics.isEmpty();
        if (!epicEmpty) {
            epics.clear();
            subTasks.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
    }

    @Override
    public void delAllSubTasks() {
        if (subTasks.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        } else {
            for (Epic epic : epics.values()) {
                epic.getSubTaskList().clear();
                epic.updateStatus();
            }
            subTasks.clear();
            System.out.println("Список подзадач очищен!");
        }
    }


    @Override
    public Task getById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }
        return null;
    }


    @Override
    public void createTask(Task task) {
        task.setId(getNextUniqId());
        tasks.put(task.getId(), task);
        System.out.println("Задача с ID " + task.getId() + " добавлена.");
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNextUniqId());
        epics.put(epic.getId(), epic);
        System.out.println("Эпик с ID " + epic.getId() + " добавлен.");
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(getNextUniqId());
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.createSubTask(subTask);
            epic.updateStatus();
            System.out.println("Подзадача с ID " + subTask.getId() + " добавлена к эпику с ID " + epic.getId());
        } else {
            System.out.println("Ошибка: Эпик с ID " + subTask.getEpicId() + " не найден.");
        }
    }


    @Override
    public void updateTask(Task task) {
        final Task oldTask = tasks.get(task.getId());
        if (oldTask != null) {
            oldTask.setName(task.getName());
            oldTask.setDescription(task.getDescription());
            oldTask.setStatus(task.getStatus());
        } else {
            System.out.println("Задача не найдена!");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic existingEpic = epics.get(epic.getId());
        if (existingEpic != null) {
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
        } else {
            System.out.println("Эпик не найден!");
        }
    }

    @Override
    public void updateSubTask(SubTask sub) {
        final SubTask existingSubTask = subTasks.get(sub.getId());
        if (existingSubTask != null) {
            existingSubTask.setName(sub.getName());
            existingSubTask.setDescription(sub.getDescription());
            existingSubTask.setStatus(sub.getStatus());
            Epic epic = epics.get(existingSubTask.getEpicId());
            epic.updateStatus();
        } else {
            System.out.println("Подзадача не найдена!");
        }
    }


    @Override
    public void delById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача с ID " + id + " удалена.");
            return;
        }

        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (SubTask subTask : epic.getSubTaskList().values()) {
                subTasks.remove(subTask.getId());
            }
            System.out.println("Эпик с ID " + id + " и его подзадачи удалены.");
            return;
        }

        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic parentEpic = epics.get(subTask.getEpicId());
            if (parentEpic != null) {
                parentEpic.getSubTaskList().remove(subTask.getId());
                parentEpic.updateStatus();
                System.out.println("Подзадача с ID " + id + " удалена из эпика.");
            } else {
                System.out.println("Эпик для подзадачи с ID " + id + " не найден.");
            }
            return;
        }

        System.out.println("Задачи с ID " + id + " не существует.");
    }


    @Override
    public ArrayList<SubTask> getSubTaskList(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubTaskList().values());
        } else {
            return null;
        }
    }

    @Override
    public int getNextUniqId() {
        return uniqId++;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
