package com.faiz.tasktreker.service;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    void delAll();

    void delAllTasks();

    void delAllEpics();

    void delAllSubTasks();

    Task getById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask sub);

    void delById(int id);

    ArrayList<SubTask> getSubTaskList(int epicId);


    List<Task> getHistory();
}
