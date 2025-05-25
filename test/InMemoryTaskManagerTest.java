import controller.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void clearManager() {
        manager = new InMemoryTaskManager();
    }
}