package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskList = new HashMap<>();


    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }


    public void delAll() {
        boolean taskEmpty = taskList.isEmpty();
        boolean epicEmpty = epicList.isEmpty();
        boolean subTaskEmpty = subTaskList.isEmpty();

        if (taskEmpty && epicEmpty && subTaskEmpty) {
            System.out.println("Нет задач для удаления! Трекер задач пуст!");
            return;
        }
        if (!taskEmpty) {
            taskList.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
        if (!epicEmpty) {
            epicList.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
        if (!subTaskEmpty) {
            subTaskList.clear();
            System.out.println("Список подзадач очищен!");
        } else {
            System.out.println("Список подзадач пуст!");
        }
    }

    public Task getById(int id) {
        boolean taskKey = taskList.containsKey(id);
        boolean epicKey = epicList.containsKey(id);
        if (taskKey) {
            return taskList.get(id);
        } else if (epicKey) {
            return epicList.get(id);
        } else {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    if (sub.getId() == id) {
                        return sub;
                    }
                }
            }
        }
        return null;
    }


    public void create(Task task) {
        if (task instanceof Epic) {
            epicList.put(task.getId(), (Epic) task);
        } else if (task instanceof SubTask) {
            if (!epicList.isEmpty()) {
                if (epicList.containsKey(((SubTask) task).getEpicId())) {
                    epicList.get(((SubTask) task).getEpicId()).createSubTask((SubTask) task);
                    subTaskList.put(task.getId(), (SubTask) task);
                }
            }
        } else taskList.put(task.getId(), task);


        if (task instanceof Epic) {
            epicList.put(task.getId(), (Epic) task);
        } else if (task instanceof SubTask) {
            if (!epicList.isEmpty()) {
                if (epicList.containsKey(((SubTask) task).getEpicId())) {
                    epicList.get(((SubTask) task).getEpicId()).createSubTask((SubTask) task);
                    subTaskList.put(task.getId(), (SubTask) task);
                }
            }
        } else taskList.put(task.getId(), task);
    }


    public void updateTask(Task task) {
        if (taskList.containsKey(task.getId())) {
            int id = task.getId();
            taskList.get(id).setName(task.getName());
            taskList.get(id).setDescription(task.getDescription());
            if (taskList.get(id).getStatus().equals(Status.NEW.toString())) {
                taskList.get(id).setStatus(Status.IN_PROGRESS.toString());
            } else if (taskList.get(id).getStatus().equals(Status.IN_PROGRESS.toString())) {
                taskList.get(id).setStatus(Status.DONE.toString());
            } else System.out.println("Эта задача уже была завершена");
        } else {
            System.out.println("Такой задачи не существует!");
        }
    }

    public void updateTask(SubTask sub) {
        int id = sub.getId();
        boolean exist = false;
        for (Epic task : epicList.values()) {
            for (SubTask subj : task.getAllSubTask().values()) {
                if (subj.getId() == id) {
                    exist = true;
                    int epicId = task.getId();
                    subj.setName(sub.getName());
                    subj.setDescription(sub.getDescription());
                    if (subj.getStatus().equals(Status.NEW.toString())) {
                        subj.setStatus(Status.IN_PROGRESS.toString());
                    } else if (subj.getStatus().equals(Status.IN_PROGRESS.toString())) {
                        subj.setStatus(Status.DONE.toString());
                    }
                    epicList.get(epicId).updateStatus();
                }
            }
        }
        if (!exist) System.out.println("Такой задачи не существует!");
    }

    public void delById(int id) {
        boolean taskKey = taskList.containsKey(id);
        boolean epicKey = epicList.containsKey(id);
        if (taskKey) {
            taskList.remove(id);
            return;
        } else if (epicKey) {
            epicList.get(id).getSubTaskList().clear();
            epicList.remove(id);
            return;
        } else {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    if (sub.getId() == id) {
                        subTaskList.remove(sub.getId());
                        task.getSubTaskList().remove(id);
                        return;
                    }
                }
            }
        }
        System.out.println("Задачи с таким ID (" + id + ") нет в Трекере!");
    }

    public HashMap<Integer, SubTask> getSubTaskList(Epic epic) {
        if (epicList.containsValue(epic)) {
            return epic.getSubTaskList();
        } else {
            return null;
        }
    }
}
