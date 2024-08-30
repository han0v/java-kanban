import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Почистить зубы", "Тщательно");
        Task task2 = new Task("Побриться", "Основательно");
        Epic epic1 = new Epic("Переезд", "В новый дом");
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!");
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощаемся с кошкой!");
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья");
        epic1.createSubTask(sub1);
        epic1.createSubTask(sub2);
        epic1.createSubTask(sub3);
        Epic epic2 = new Epic("Учёба", "Изучаем JAVA");
        SubTask sub4 = new SubTask("Порадоваться выполненному заданию", "Но ждать замечаний :)");
        epic2.createSubTask(sub4);

        TaskManager taskManager = new TaskManager();
        taskManager.create(epic1);
        taskManager.create(epic2);
        taskManager.create(task1);
        taskManager.create(task2);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());

        System.out.println("Получение списка всех подзадач определённого эпика: " + 1);
        System.out.println(taskManager.getSubTaskList(epic1));
        System.out.println();

        System.out.println("Обновление задачи TASK");
        task1.setDescription("Помыть щётку");
        taskManager.updateTask(task1);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        task1.setDescription("Убрать щётку");
        task2.setDescription("Помыть бритву");
        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        taskManager.updateTask(task2);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println();

        System.out.println("Обновление EPIC'а");
        sub1.setDescription("Загрузить в машину");
        taskManager.updateTask(sub1);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка в клетке");
        taskManager.updateTask(sub2);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println("-----------------------------------------------------------");
        sub1.setDescription("Найти водителя");
        taskManager.updateTask(sub1);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка села за руль и уехала");
        taskManager.updateTask(sub2);
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println();

        System.out.println("Получение задачи по id: ");
        System.out.println(taskManager.getById(4));
        System.out.println(taskManager.getById(7));
        System.out.println(taskManager.getById(1));
        System.out.println(taskManager.getById(2));
        System.out.println(taskManager.getById(5));
        System.out.println(taskManager.getById(6));
        System.out.println(taskManager.getById(3));
        System.out.println(taskManager.getById(1));
        System.out.println(taskManager.getById(5));
        System.out.println(taskManager.getById(3));
        System.out.println();


        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println("Удаление задачи по id: " + 3 + " и " + 1 + ". А так же несуществующий id: " + 32);
        taskManager.delById(32);
        taskManager.delById(3);
        taskManager.delById(1);

        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
        System.out.println();

        System.out.println("Очистка трекера задач");
        taskManager.delAll();
        System.out.println("Список задач:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpicList());
    }
}