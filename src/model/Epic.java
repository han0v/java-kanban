package model;

import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTaskList;

    public Epic(String name, String description) {
        super(name, description);
        subTaskList = new HashMap<>();
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
            if (this.getStatus().equals(Status.NEW.toString())) {
                this.setStatus(Status.IN_PROGRESS.toString());
            } else this.setStatus(Status.DONE.toString());
        } else {
            boolean flag = true;
            for (SubTask sub : subTaskList.values()) {
                if (!sub.getStatus().equals(Status.DONE.toString())) {
                    flag = false;
                }
                if (sub.getStatus().equals(Status.IN_PROGRESS.toString())) {
                    this.setStatus(Status.IN_PROGRESS.toString());
                    flag = false;
                    break;
                }
            }
            if (flag) this.setStatus(Status.DONE.toString());
        }
    }
}
