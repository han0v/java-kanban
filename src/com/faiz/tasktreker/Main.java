package com.faiz.tasktreker;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;
import com.faiz.tasktreker.service.InMemoryTaskManager;
import com.faiz.tasktreker.service.Managers;
import com.faiz.tasktreker.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Почистить зубы", "Тщательно");
        Task task2 = new Task("Побриться", "Основательно");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Переезд", "В новый дом");
        Epic epic2 = new Epic("Учёба", "Изучаем JAVA");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);


        // Создание подзадач для первого эпика
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!", epic1.getId());
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощаемся с кошкой!", epic1.getId());
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья", epic1.getId());


        taskManager.createSubTask(sub1);
        taskManager.createSubTask(sub2);
        taskManager.createSubTask(sub3);

        displayCurrentState(taskManager);


        System.out.println("Обновление задачи TASK");
        task1.setDescription("Помыть щётку");
        taskManager.updateTask(task1);
        displayCurrentState(taskManager);


        System.out.println("Обновление подзадач:");
        sub1.setDescription("Загрузить все коробки в машину");
        taskManager.updateSubTask(sub1);
        displayCurrentState(taskManager);

        sub2.setDescription("Проверить, чтобы кошка была в клетке");
        taskManager.updateSubTask(sub2);
        displayCurrentState(taskManager);


        System.out.println("Получение задачи по ID:");
        System.out.println(taskManager.getById(4));
        System.out.println(taskManager.getById(7));
        System.out.println(taskManager.getById(1));
        System.out.println(taskManager.getById(2));
        System.out.println(taskManager.getById(5));
        System.out.println(taskManager.getById(6));
        System.out.println(taskManager.getById(3));
        System.out.println();


        displayCurrentState(taskManager);
        System.out.println("Удаление задачи по id: " + 3 + " и " + 1 + ". А так же несуществующий id: " + 32);
        taskManager.delById(32);
        taskManager.delById(3);
        taskManager.delById(1);

        displayCurrentState(taskManager);
    }

    private static void displayCurrentState(TaskManager taskManager) {
        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTasks());
        System.out.println("-----------------------------------------------------------");
    }
}