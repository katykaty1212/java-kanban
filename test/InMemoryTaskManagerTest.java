
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createmanager() {
        return new InMemoryTaskManager();
    }
}