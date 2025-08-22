import manager.Managers;
import manager.TaskManager;
import task.*;


public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Помыть машину", "Заехать на мойку");
        Task task2 = new Task("Помыть кота", "Намылить мылом для кота");

        int taskId1 = manager.addTask(task1);
        int taskId2 = manager.addTask(task2);


        Epic epic1 = new Epic("Переезд", "Собираем вещи");
        int epicId1 = manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Сложить все в коробки", "5 картонных коробок", epicId1);
        Subtask subtask2 = new Subtask("Заказать машину", "Газель", epicId1);

        int subtaskId1 = manager.addSubtask(subtask1);
        int subtaskId2 = manager.addSubtask(subtask2);

        System.out.println("\nВсе задачи");
        manager.printAllTask();


        System.out.println("\nЭпик с подзадачами");
        manager.printAllEpicWithSubtask();

        System.out.println("\nЭпик без подзадач");
        manager.printAllEpic();

        System.out.println("\nВсе подзадачи");
        manager.printAllSubtask();

        System.out.println("\nВсе задачи Id");
        Task taskResult = manager.getTask(taskId2);
        System.out.println(taskResult.getTitle());

        System.out.println("\nЭпик по ID");
        Epic epicResult = manager.getEpicById(epicId1);
        System.out.println(epicResult.getTitle());

        System.out.println("\nПодзадача по ID");
        Subtask subtaskResult1 = manager.getSubtaskById(subtaskId1);
        System.out.println(subtaskResult1.getTitle());
        Subtask subtaskResult2 = manager.getSubtaskById(subtaskId2);
        System.out.println(subtaskResult2.getTitle());

        Task updatedTask = new Task("Новая задача", "Новое описание");
        updatedTask.setId(taskId1);
        manager.updateTask(updatedTask, Status.NEW);

        Epic updatedEpic = new Epic("Новый эпик", "Новое описание");
        updatedEpic.setId(epicId1);
        manager.updateEpic(updatedEpic);

        Subtask updatedSubtask = new Subtask("Новая подзадача", "Новое описание", epicId1);
        updatedSubtask.setId(subtaskId1);
        manager.updateSubtask(updatedSubtask, Status.NEW);

        System.out.println("\nВсе задачи(new)");
        manager.printAllTask();


        System.out.println("\nЭпик с подзадачами(new)");
        manager.printAllEpicWithSubtask();

        manager.updateTask(task1, Status.IN_PROGRESS);
        manager.updateTask(task2, Status.DONE);
        manager.updateSubtask(subtask1, Status.DONE);
        manager.updateSubtask(subtask2, Status.IN_PROGRESS);
        manager.updateEpicStatus(epic1);


        System.out.println("\nВсе задачи(статус)");
        manager.printAllTask();


        System.out.println("\nЭпик с подзадачами(статус)");
        manager.printAllEpicWithSubtask();


        System.out.println("\nПосле удаления");
        manager.deleteSubtask(subtaskId1);
        manager.printAllEpicWithSubtask();

        System.out.println("история");
        System.out.println();
        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask.getTitle());
        }

        manager.deleteAllTask();

        System.out.println();
        System.out.println("удалить все задачи");
        manager.printAllTask();

        //ФЗ6

        Task task3 = new Task("Задача 1", "Описание");
        Task task4 = new Task("Задача 2", "Описание");

        int taskId3 = manager.addTask(task3);
        int taskId4 = manager.addTask(task4);

        Epic epic4 = new Epic("Эпик 1 с подзадачами", "Описание");
        int epicId4 = manager.addEpic(epic4);

        Epic epic5 = new Epic("Эпик 2 без подзадачи", "Описание");
        int epicId5 = manager.addEpic(epic5);

        Subtask subtask3 = new Subtask("Подзадача 1", "Описание", epicId4);
        Subtask subtask4 = new Subtask("Подзадача 2", "Описание", epicId4);
        Subtask subtask5 = new Subtask("Подзадача 3", "Описание", epicId4);

        int subtaskId3 = manager.addSubtask(subtask3);
        int subtaskId4 = manager.addSubtask(subtask4);
        int subtaskId5 = manager.addSubtask(subtask5);

        System.out.println("история");

        System.out.println();
        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask.getTitle());
        }

        manager.getTask(taskId3);
        manager.getTask(taskId4);
        manager.getEpicById(epicId4);
        manager.getEpicById(epicId5);

        System.out.println("Запрошено 4 задачи");
        System.out.println();

        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask.getTitle());
        }

        manager.getEpicById(epicId4);
        manager.getTask(taskId3);
        manager.getEpicById(epicId5);
        manager.getTask(taskId4);

        System.out.println();
        System.out.println("Запрошены в другом порядке");

        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask.getTitle());
        }

        manager.deleteTask(taskId4);

        System.out.println();
        System.out.println("Удалена задачу 2");

        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask.getTitle());
        }

        manager.deleteEpic(epicId5);

        System.out.println();
        System.out.println("Удален эпик 1");

        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask.getTitle());
        }

        System.out.println();

        if (manager.getAllSubtask().size() == 0) {
            System.out.println("Список подзадач пуст");
        } else {
            System.out.println("Список подзадач");
            manager.printAllSubtask();
        }
    }
}