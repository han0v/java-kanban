package com.faiz.tasktreker.service;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagersTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }
}