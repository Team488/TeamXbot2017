package competition;

import org.junit.Ignore;

import competition.networking.OffboardCommunicationServer;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.UnitTestModule;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Ignore
public class BaseTest extends BaseWPITest {
    
    protected class TestModule extends UnitTestModule {
        @Override
        protected void configure() {
            super.configure();
            
            this.bind(OffboardCommunicationServer.class).to(MockOffboardCommServer.class);
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
