package com.faiz.tasktreker.service;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Создание временного файла для тестов
        tempFile = File.createTempFile("test_manager", ".csv");
        tempFile.deleteOnExit();  // Удалить файл при завершении программы
        manager = new FileBackedTaskManager(tempFile.getAbsolutePath());
    }

    @Test
    public void testSaveAndLoadEmptyFile() {
        // Сохранение пустого менеджера
        manager.save();

        // Загрузка из пустого файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(loadedManager);
        assertTrue(loadedManager.getTasks().isEmpty());
        assertTrue(loadedManager.getSubTasks().isEmpty());
        assertTrue(loadedManager.getEpics().isEmpty());
    }

    @Test
    public void testSaveMultipleTasks() throws IOException {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", 1);
        Epic epic = new Epic("Эпик", "Описание эпика");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createSubTask(subTask1);
        manager.createEpic(epic);

        // Сохранение задач в файл
        manager.save();

        // Проверка сохраненных данных
        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(4, lines.size()); // Убедитесь, что 4 задачи были сохранены в файл
    }


    @AfterEach
    public void tearDown() {
        // Очистка ресурса, если это необходимо
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }
}
