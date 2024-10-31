package com.faiz.tasktreker.service;

import com.faiz.tasktreker.exceptions.ValidationException;
import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.Status;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagersTest<T extends TaskManager> {
    protected T manager;
    protected Task task = new Task("Тестовая задача", "Тестовое описание",
            LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
    ;
    protected Epic epic;
    protected SubTask subTask;


//    @BeforeEach
//    void beforeEach() { не работает, приходится создавать в каждом тесте в ручную, причину неисправности не понял
//        task = new Task("Тестовое описание", "Тестовая задача", LocalDateTime.of(2024, 8, 10, 12, 0),
//                Duration.ofMinutes(30));
//        manager.createTask(task);
//        epic = new Epic("Тестовый эпик", "Тестовое описание");
//        manager.createEpic(epic);
//        subTask = new SubTask("Тестовое описание", "Тестовая подзадача", epic.getId(),
//                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
//        manager.createSubTask(subTask);
//    }

    @Test
    void addNewEpic() {
        epic = new Epic("Тестовый эпик", "Тестовое описание");
        manager.createEpic(epic);
        subTask = new SubTask("Тестовая задача", "Тестовое описание", epic.getId(),
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        manager.createSubTask(subTask);
        final Epic savedEpic = manager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        final Map<Integer, Epic> epics = manager.getEpicsMap();
        assertNotNull(epics, "Список пуст.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
    }


    @Test
    void addNewTask() {
        task = new Task("Тестовая задача", "Тестовое описание", LocalDateTime.of(2024, 8, 10, 12, 0),
                Duration.ofMinutes(30));
        manager.createTask(task);
        final Task savedTask = manager.getTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не равны.");
        final Map<Integer, Task> tasks = manager.getTaskMap();
        assertNotNull(tasks, "Список пуст.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не равны.");
    }


    @Test
    void removeTaskPerId() {
        manager.createTask(task);
        Map<Integer, Task> tasks = manager.getTaskMap();
        assertEquals(1, tasks.size());
        manager.delById(task.getId());
        assertEquals(0, tasks.size(), "Задача не была удалена.");
    }

    @Test
    void removeSubtaskPerId() {
        epic = new Epic("Тестовый эпик", "Тестовое описание");
        manager.createEpic(epic);
        subTask = new SubTask("Тестовая подзадача", "Тестовое описание", epic.getId(),
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        manager.createSubTask(subTask);
        Map<Integer, SubTask> subtasks = manager.getSubtasksMap();
        manager.delById(subTask.getId());
        assertEquals(0, subtasks.size(), "Подзадача не была удалена.");
    }

    @Test
    void removeEpicPerId() {
        epic = new Epic("Тестовый эпик", "Тестовое описание");
        manager.createEpic(epic);
        subTask = new SubTask("Тестовая подзадача", "Тестовая описание", epic.getId(),
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        manager.createSubTask(subTask);
        Map<Integer, Epic> epics = manager.getEpicsMap();
        manager.delById(epic.getId());
        assertEquals(0, epics.size(), "Эпик не был удален.");
    }

    @Test
    void updateTasks() {
        manager.createTask(task);
        Map<Integer, Task> tasks = manager.getTaskMap();
        assertEquals(task, tasks.get(task.getId()));
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(task, tasks.get(task.getId()), "Задача не обновлена.");
    }


    @Test
    void validationTest() {
        task = new Task("Тестовая задача", "Тестовая задача",
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        manager.createTask(task);
        Task task1 = new Task("Тестовая задача", "Тестовая задача",
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        assertThrows(ValidationException.class, () -> manager.createTask(task1), "Задачи некорректно валидируются.");
    }

    @Test
    void inValidationTest() {
        Task inValidTask = new Task("Тестовая задача", "Тестовая задача",
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        Task inValidTask1 = new Task("Тестовая задача", "Тестовое описание",
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        epic = new Epic("Тестовый эпик", "Тестовое описание");
        manager.createTask(inValidTask1);
        manager.createEpic(epic);
        assertThrows(ValidationException.class, () -> manager.createTask(inValidTask), "Tasks are incorrectly validated");
    }

    @Test
    void validationTime() {
        Task timeTask1 = new Task("Тестовая задача 1", "Описание теста 1",
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        manager.createTask(timeTask1);
        Task timeTask2 = new Task("Тестовая задача 2", "Описание теста 2",
                LocalDateTime.of(2024, 8, 10, 12, 0), Duration.ofMinutes(30));
        assertThrows(ValidationException.class, () -> manager.createTask(timeTask2),
                "Новая задача не включена в существующую задачу");
    }
}