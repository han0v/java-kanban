package com.faiz.tasktreker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Epic extends Task {
    private final Map<Integer, SubTask> subTaskList;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTaskList = new HashMap<>();
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
    }

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        subTaskList = new HashMap<>();
    }

    public Map<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    public TaskType getType() {
        return TaskType.EPIC;
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

    public void updateEpicData() {
        if (subTaskList.isEmpty()) {
            this.setStatus(Status.NEW);
            this.startTime = null;
            this.endTime = null;
            this.duration = Duration.ZERO;
            return;
        }

        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        Duration totalDuration = Duration.ZERO;

        for (SubTask sub : subTaskList.values()) {
            LocalDateTime subStart = sub.getStartTime();
            Duration subDuration = sub.getDuration();

            if (earliestStart == null || (subStart != null && subStart.isBefore(earliestStart))) {
                earliestStart = subStart;
            }

            LocalDateTime subEnd = sub.getEndTime();
            if (latestEnd == null || (subEnd != null && subEnd.isAfter(latestEnd))) {
                latestEnd = subEnd;
            }

            totalDuration = totalDuration.plus(subDuration);
        }

        this.startTime = earliestStart;
        this.endTime = latestEnd;
        this.duration = totalDuration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Проверка на идентичность
        if (!(o instanceof Epic)) return false; // Проверка на тип
        if (!super.equals(o)) return false; // Проверка полей родительского класса

        Epic epic = (Epic) o;
        return Objects.equals(getId(), epic.getId()) &&  // Сравнение по уникальному идентификатору
                Objects.equals(getName(), epic.getName()) && // Сравнение по имени
                Objects.equals(getDescription(), epic.getDescription()) && // Сравнение по описанию
                getStatus() == epic.getStatus(); // Сравнение по статусу
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus()); // Генерация хэш-кода
    }




    @Override
    public String toString() {
        return getId() + "," + TaskType.EPIC + "," + getName() + "," + getStatus() + "," + getEpicId() + ","
                + getDescription() + "," + startTime + "," + duration.toMinutes() + ",";
    }
}


