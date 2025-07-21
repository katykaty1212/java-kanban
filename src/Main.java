public class Main {
        public static void main(String[] args) {
            TaskManager manager = new TaskManager();

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

            Task updatedTask = new Task("Новая задача", "Новое описание");
            updatedTask.setId(taskId1);
            manager.updateTask(updatedTask);

            Epic updatedEpic = new Epic("Новый эпик", "Новое описание");
            updatedEpic.setId(epicId1);
            manager.updateEpic(updatedEpic);

            Subtask updatedSubtask = new Subtask("Новая подзадача", "Новое описание", epicId1);
            updatedSubtask.setId(subtaskId1);
            manager.updateSubtask(updatedSubtask);

            System.out.println("\nВсе задачи(new)");
            manager.printAllTask();


            System.out.println("\nЭпик с подзадачами(new)");
            manager.printAllEpicWithSubtask();

            System.out.println("\nПосле удаления");
            manager.deleteSubtask(subtaskId1);
            manager.printAllEpicWithSubtask();

            manager.updateTaskStatus(taskId1, Status.IN_PROGRESS);
            subtask1.setStatus(Status.DONE);
            subtask2.setStatus(Status.IN_PROGRESS);
            manager.updateEpicStatus(epic1);

            System.out.println("\nВсе задачи(статус)");
            manager.printAllTask();


            System.out.println("\nЭпик с подзадачами(статус)");
            manager.printAllEpicWithSubtask();

            System.out.println("\nПосле удаления");
            manager.deleteSubtask(subtaskId1);
            manager.printAllEpicWithSubtask();
        }
    }

