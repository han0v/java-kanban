import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(taskManager.getNextUniqId(), "Почистить зубы", "Тщательно");
        Task task2 = new Task(taskManager.getNextUniqId(), "Побриться", "Основательно");

        Epic epic1 = new Epic(taskManager.getNextUniqId(), "Переезд", "В новый дом");
        Epic epic2 = new Epic(taskManager.getNextUniqId(), "Учёба", "Изучаем JAVA");

        // Создание подзадач для первого эпика
        SubTask sub1 = new SubTask(taskManager.getNextUniqId(), "Собрать все коробки", "Да, да... Все коробочки!", epic1.getId());
        SubTask sub2 = new SubTask(taskManager.getNextUniqId(), "Упаковать кошку", "Прощаемся с кошкой!", epic1.getId());
        SubTask sub3 = new SubTask(taskManager.getNextUniqId(), "Сделать домашнее задание", "До воскресенья", epic1.getId());

        epic1.createSubTask(sub1);
        epic1.createSubTask(sub2);
        epic2.createSubTask(sub3);



        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(sub1);
        taskManager.createSubTask(sub2);
        taskManager.createSubTask(sub3);

        displayCurrentState(taskManager);



        System.out.println("Обновление задачи TASK");
        task1.setDescription("Помыть щётку");
        taskManager.updateTask(task1);
        displayCurrentState(taskManager);


        System.out.println("Обновление подзадач:");
        sub1.setDescription("Загрузить все коробки в машину");
        taskManager.updateSubTask(sub1);
        displayCurrentState(taskManager);

        sub2.setDescription("Проверить, чтобы кошка была в клетке");
        taskManager.updateSubTask(sub2);
        displayCurrentState(taskManager);



        System.out.println("Получение задачи по ID:");
        System.out.println(taskManager.getById(4));
        System.out.println(taskManager.getById(7));
        System.out.println(taskManager.getById(1));
        System.out.println(taskManager.getById(2));
        System.out.println(taskManager.getById(5));
        System.out.println(taskManager.getById(6));
        System.out.println(taskManager.getById(3));
        System.out.println();




        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Удаление задачи по id: " + 3 + " и " + 1 + ". А так же несуществующий id: " + 32);
        taskManager.delById(32);
        taskManager.delById(3);
        taskManager.delById(1);

        displayCurrentState(taskManager);

        System.out.println("Очистка трекера задач");
        displayCurrentState(taskManager);
    }

    private static void displayCurrentState(TaskManager taskManager) {
        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTasks());
        System.out.println("-----------------------------------------------------------");
    }
}