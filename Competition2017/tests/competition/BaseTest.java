package competition;

import org.junit.Ignore;

import competition.networking.OffboardCommunicationServer;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.UnitTestModule;

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
