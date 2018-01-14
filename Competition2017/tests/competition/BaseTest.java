package competition;

import org.junit.Ignore;

import competition.subsystems.offboard.MockOffboardCommsInterface;
import competition.subsystems.offboard.XOffboardCommsInterface;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.UnitTestModule;

@Ignore
public class BaseTest extends BaseWPITest {
    
    protected class TestModule extends UnitTestModule {
        @Override
        protected void configure() {
            super.configure();
            super.bind(XOffboardCommsInterface.class).to(MockOffboardCommsInterface.class);
        }
    }
    
    public BaseTest() {
        guiceModule = new TestModule();
    }
    
    @Override
    public void setUp() {
        super.setUp();
    }
}
