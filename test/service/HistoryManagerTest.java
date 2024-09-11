package service;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.model.Task;
import com.faiz.tasktreker.service.HistoryManager;
import com.faiz.tasktreker.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class HistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void beforeEach() {

        task = new Task("Test description", "TestTask");
        task.setId(1);
        epic = new Epic("Test description", "TestEpic");
        epic.setId(2);
        subTask = new SubTask("Test description", "TestSubTask", 2);
        subTask.setId(3);
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }
}