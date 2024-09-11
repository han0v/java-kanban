package service;

import com.faiz.tasktreker.service.InMemoryHistoryManager;
import com.faiz.tasktreker.service.InMemoryTaskManager;
import com.faiz.tasktreker.service.Managers;
import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.Status;
import com.faiz.tasktreker.model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagersTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }
}