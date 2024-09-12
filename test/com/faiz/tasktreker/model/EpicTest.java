package com.faiz.tasktreker.model;

import com.faiz.tasktreker.model.Epic;
import com.faiz.tasktreker.model.Status;
import com.faiz.tasktreker.model.SubTask;
import com.faiz.tasktreker.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void shouldEqualsWithCopy() {

        Epic epic = new Epic("name", "desc");
        Epic epicExpected = new Epic("name", "desc");
        assertEqualsTask(epicExpected, epic, "Эпики должны совпадать");
    }

    private static void assertEqualsTask(Epic expected, Epic actual, String message) {
        assertEquals(expected.getId(), actual.getId(), message + ", id");
        assertEquals(expected.getName(), actual.getName(), message + ", name");
        assertEquals(expected.getDescription(), actual.getDescription(), message + ", description");
    }
}