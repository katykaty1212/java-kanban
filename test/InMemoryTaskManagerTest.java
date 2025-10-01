
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createmanager() {
        return new InMemoryTaskManager();
    }
}