import controller.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void testHistoryManagerNotNull() {
        assertNotNull(Managers.getDefaultHistory(), "Менеджер не инициализирован");
    }

    @Test
    void testTaskManagerNotNull() {
        assertNotNull(Managers.getDefault(), "Менеджер не инициализирован");
    }
}