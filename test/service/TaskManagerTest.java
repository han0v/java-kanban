package service;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.Status;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.faiz.tasktreker.service.HistoryManager;
import com.faiz.tasktreker.service.InMemoryHistoryManager;
import com.faiz.tasktreker.service.TaskManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagersTest<T extends TaskManager> {
    protected T manager;
    protected Task task = new Task("Test description", "TestTask");
    protected Epic epic = new Epic("TestEpic", "Test description");
    protected SubTask subTask = new SubTask("Test description", "TestSubTask", epic.getId());


    @Test
    void addNewTask() {
        manager.createTask(task);
        Task savedTask = manager.getTaskById(task.getId());
        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks are not equal.");
        final ArrayList<Task> tasks = manager.getTasks();
        assertNotNull(tasks, "List is empty.");
        assertEquals(1, tasks.size(), "Wrong quantity of tasks.");
        ;
    }

    @Test
    void addNewEpic() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        final Epic savedEpic = manager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Task not found.");
        final ArrayList<Epic> epics = manager.getEpics();
        assertNotNull(epics, "List is empty.");
        assertEquals(1, epics.size(), "Wrong quantity of tasks.");
    }

    @Test
    public void shouldDeleteTaskById() {
        manager.createTask(task);
        manager.delById(task.getId());
        assertEquals(Collections.emptyList(), manager.getTasks());
    }

    @Test
    public void shouldDeleteEpicById() {
        manager.createEpic(epic);
        manager.delById(epic.getId());
        assertEquals(Collections.emptyList(), manager.getEpics());
    }

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        manager.createTask(task);
        manager.delById(777);
        assertEquals(List.of(task), manager.getTasks());
    }
}