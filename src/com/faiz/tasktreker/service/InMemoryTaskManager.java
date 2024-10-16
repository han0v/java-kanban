package com.faiz.tasktreker.service;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int uniqId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
        this(Managers.getDefaultHistory());
    }


    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;

    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
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
            for (Integer taskId : tasks.keySet()) {
                historyManager.remove(taskId);
            }
            tasks.clear();
            System.out.println("Список задач очищен!");
        }
        if (!epicEmpty) {
            for (Epic epic : epics.values()) {
                for (SubTask subTask : epic.getSubTaskList().values()) {
                    historyManager.remove(subTask.getId());
                }
                historyManager.remove(epic.getId());
            }
            epics.clear();
            System.out.println("Список эпиков очищен!");
        }
        if (!subTaskEmpty) {
            for (Integer subTaskId : subTasks.keySet()) {
                historyManager.remove(subTaskId);
            }
            subTasks.clear();
            System.out.println("Список подзадач очищен!");
        }
    }

    @Override
    public void delAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
        } else {
            for (Integer taskId : tasks.keySet()) {
                historyManager.remove(taskId);
            }
            tasks.clear();
            System.out.println("Список задач очищен!");
        }
    }

    @Override
    public void delAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("Список эпиков пуст!");
        } else {
            for (Epic epic : epics.values()) {
                for (SubTask subTask : epic.getSubTaskList().values()) {
                    historyManager.remove(subTask.getId());
                }
                historyManager.remove(epic.getId());
            }
            epics.clear();
            subTasks.clear();
            System.out.println("Список эпиков очищен!");
        }
    }


    @Override
    public void delAllSubTasks() {
        if (subTasks.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        } else {
            for (SubTask subTask : subTasks.values()) {
                historyManager.remove(subTask.getId());
            }
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
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        }
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        }
        if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            historyManager.add(subTask);
            return subTask;
        }
        return null;
    }


    @Override
    public Task createTask(Task task) {
        task.setId(getNextUniqId());
        tasks.put(task.getId(), task);
        System.out.println("Задача с ID " + task.getId() + " добавлена.");
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextUniqId());
        epics.put(epic.getId(), epic);
        System.out.println("Эпик с ID " + epic.getId() + " добавлен.");
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
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
        return subTask;
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
            historyManager.remove(id);
            System.out.println("Задача с ID " + id + " удалена.");
            return;
        }

        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (SubTask subTask : epic.getSubTaskList().values()) {
                subTasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
            historyManager.remove(id);
            System.out.println("Эпик с ID " + id + " и его подзадачи удалены.");
            return;
        }

        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic parentEpic = epics.get(subTask.getEpicId());
            if (parentEpic != null) {
                parentEpic.getSubTaskList().remove(subTask.getId());
                parentEpic.updateStatus();
                historyManager.remove(id);
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int getNextUniqId() {
        return uniqId++;
    }
}
