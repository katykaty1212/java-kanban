package test;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import task.Status;
import task.Task;

public class TaskTest {

    @Test
    public void createTaskWithTitleAndDescriptionAndStatusAndId() { //проверка создание задачи/описания/статуса/ID
        Task task = new Task("Тест название", "Тест описание");

        Assertions.assertEquals("Тест название", task.getTitle());
        Assertions.assertEquals("Тест описание", task.getDescription());
        Assertions.assertEquals(Status.NEW, task.getStatus());
        Assertions.assertEquals(0, task.getId());
    }

    @Test
    public void setStatusAndUpdateStatus() { //  установка статуса
        Task task = new Task("Тест название", "Тест описание");

        task.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(Status.IN_PROGRESS, task.getStatus());

        task.setStatus(Status.DONE);
        Assertions.assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    public void setId() { //присваивание ID
        Task task = new Task("Тест название", "Тест описание");

        task.setId(15);
        Assertions.assertEquals(15, task.getId());
    }

    @Test
    public void equalityOfDifferentTasksWithSameId() { //разные задачи с одним ID равны
        Task task = new Task("Тест название", "Тест описание");
        task.setId(1);
        Task task1 = new Task("Тест название1", "Тест описание1");
        task1.setId(1);

        Assertions.assertEquals(task, task1, "Задачи с одинаковыми ID должны быть равны.");
    }

    @Test
    public void differentTaskNotEquals() { //одинаковые задачи с разными ID не равны
        Task task = new Task("Тест название", "Тест описание");
        task.setId(1);
        Task task1 = new Task("Тест название", "Тест описание");
        task1.setId(2);

        Assertions.assertNotEquals(task, task1, "Задачи с разными ID не должны быть равны");
    }

    @Test
    public void taskNotEqualsNull() {  //таск не равен null
        Task task = new Task("Тест название", "Тест описание");
        Assertions.assertNotEquals(null, task);
    }
}