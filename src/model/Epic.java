package model;

import java.util.HashMap;
import service.TaskManager;


public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTaskList;

    public Epic(int id, String name, String description) {
        super(id, name, description); // Передаем id в родительский конструктор
        subTaskList = new HashMap<>();
    }

    public void addSubTask(String name, String description, TaskManager taskManager) {
        int subTaskId = taskManager.getNextUniqId();
        SubTask subTask = new SubTask(subTaskId, name, description, this.getId());
        subTaskList.put(subTaskId, subTask);
        taskManager.addSubTask(subTask);
        updateStatus();
    }

    public HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void createSubTask(SubTask sub) {
        subTaskList.put(sub.getId(), sub);
        sub.setEpicId(this.getId());
    }

    public HashMap<Integer, SubTask> getAllSubTask() {
        return subTaskList;
    }

    public void updateStatus() {
        if (this.subTaskList.isEmpty()) {
            this.setStatus(Status.NEW);
        } else {
            boolean allDone = true;
            boolean allNew = true;

            for (SubTask sub : subTaskList.values()) {
                if (!sub.getStatus().equals(Status.DONE)) {
                    allDone = false;
                }
                if (!sub.getStatus().equals(Status.NEW)) {
                    allNew = false;
                }
            }

            if (allNew) {
                this.setStatus(Status.NEW);
            } else if (allDone) {
                this.setStatus(Status.DONE);
            } else {
                this.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}


