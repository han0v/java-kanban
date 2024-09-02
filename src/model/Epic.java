package model;

import java.util.HashMap;

import service.TaskManager;


public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTaskList;

    public Epic(int id, String name, String description) {
        super(id, name, description); // Передаем id в родительский конструктор
        subTaskList = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void createSubTask(SubTask sub) {
        subTaskList.put(sub.getId(), sub);
        sub.setEpicId(this.getId());
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

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}


