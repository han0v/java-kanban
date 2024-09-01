package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int uniqId = 1;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public int getNextUniqId() {
        return uniqId++;
    }

    public void addTask(String name, String description) {
        int id = getNextUniqId();
        Task task = new Task(id, name, description);
        tasks.put(id, task);
    }

    public void addEpic(String name, String description) {
        int id = getNextUniqId();
        Epic epic = new Epic(id, name, description);
        epics.put(id, epic);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.getSubTaskList().put(subTask.getId(), subTask);
        }
        epic.updateStatus();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }


    public void setUniqId(int uniqId) {
        this.uniqId = uniqId;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }


    public void delAll() {
        boolean taskEmpty = tasks.isEmpty();
        boolean epicEmpty = epics.isEmpty();
        boolean subTaskEmpty = subTasks.isEmpty();

        if (taskEmpty && epicEmpty && subTaskEmpty) {
            System.out.println("Нет задач для удаления! Трекер задач пуст!");
            return;
        }
        if (!taskEmpty) {
            tasks.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
        if (!epicEmpty) {
            epics.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
        if (!subTaskEmpty) {
            subTasks.clear();
            System.out.println("Список подзадач очищен!");
        } else {
            System.out.println("Список подзадач пуст!");
        }
    }

    public void delAllTasks() {
        boolean taskEmpty = tasks.isEmpty();
        if (!taskEmpty) {
            tasks.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
    }

    public void delAllEpics() {
        boolean epicEmpty = epics.isEmpty();
        if (!epicEmpty) {
            epics.clear();
            subTasks.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
    }

    public void delAllSubTasks() {
        boolean subTaskEmpty = subTasks.isEmpty();
        if (!subTaskEmpty) {
            subTasks.clear();
            System.out.println("Список подзадач очищен!");
        } else {
            System.out.println("Список подзадач пуст!");
        }

    }



    public Task getById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }
        return null;
    }


//    public Task getById(int id) {
//        boolean taskKey = tasks.containsKey(id);
//        boolean epicKey = epics.containsKey(id);
//        if (taskKey) {
//            return tasks.get(id);
//        } else if (epicKey) {
//            return epics.get(id);
//        } else {
//            for (Epic task : epics.values()) {
//                for (SubTask sub : task.getAllSubTask().values()) {
//                    if (sub.getId() == id) {
//                        return sub;
//                    }
//                }
//            }
//        }
//        return null;



//    public void create(Task task) {
//        if (task instanceof Epic) {
//            epics.put(task.getId(), (Epic) task);
//        } else if (task instanceof SubTask) {
//            if (!epics.isEmpty()) {
//                if (epics.containsKey(((SubTask) task).getEpicId())) {
//                    epics.get(((SubTask) task).getEpicId()).createSubTask((SubTask) task);
//                    epics.get(((SubTask) task).getEpicId()).setStatus(Status.NEW);
//                    epic.updateStatus();
//                    subTasks.put(task.getId(), (SubTask) task);
//                }
//            }
//        } else tasks.put(task.getId(), task);
    public void createTask(Task task) {
    tasks.put(task.getId(), task);
    System.out.println("Задача с ID " + task.getId() + " добавлена.");
    }

    // Метод для создания эпика
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        System.out.println("Эпик с ID " + epic.getId() + " добавлен.");
    }

    // Метод для создания подзадачи
    public void createSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.createSubTask(subTask);
            epic.updateStatus();
            System.out.println("Подзадача с ID " + subTask.getId() + " добавлена к эпике с ID " + epic.getId());
        } else {
            System.out.println("Ошибка: Эпик с ID " + subTask.getEpicId() + " не найден.");
        }
    }



    public void updateTask(Task task, Status status) {
        if (tasks.containsKey(task.getId())) {
            tasks.get(task.getId()).setName(task.getName());
            tasks.get(task.getId()).setDescription(task.getDescription());
            tasks.get(task.getId()).setStatus(status);
        } else {
            System.out.println("Задача не найдена!");
        }
    }

    public void updateSubTask(SubTask sub) {
        if (subTasks.containsKey(sub.getId())) {
            SubTask existingSubTask = subTasks.get(sub.getId());
            existingSubTask.setName(sub.getName());
            existingSubTask.setDescription(sub.getDescription());

            if (existingSubTask.getStatus() == Status.NEW) {
                existingSubTask.setStatus(Status.IN_PROGRESS);
            } else if (existingSubTask.getStatus() == Status.IN_PROGRESS) {
                existingSubTask.setStatus(Status.DONE);
            }

            Epic epic = epics.get(existingSubTask.getEpicId());
            epic.updateStatus();
        } else {
            System.out.println("Подзадача не найдена!");
        }
    }


    public void delById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача с ID " + id + " удалена.");
            return;
        }

        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            subTasks.values().removeIf(subTask -> subTask.getEpicId() == epic.getId());
            System.out.println("Эпик с ID " + id + " и его подзадачи удалены.");
            return;
        }

        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic parentEpic = epics.get(subTask.getEpicId());
            if (parentEpic != null) {
                parentEpic.getSubTaskList().remove(subTask.getId());
                parentEpic.updateStatus();
                System.out.println("Подзадача с ID " + id + " удалена из эпика.");
            } else {
                System.out.println("Эпик для подзадачи с ID " + id + " не найден.");
            }
            return;
        }

        System.out.println("Задачи с ID " + id + " не существует.");
    }


    public ArrayList<SubTask> getSubTaskList(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubTaskList().values());
        } else {
            return null;
        }
    }

}
