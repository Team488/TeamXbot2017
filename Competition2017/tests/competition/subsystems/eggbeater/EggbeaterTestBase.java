package competition.subsystems.eggbeater;

import static org.junit.Assert.assertEquals;

import competition.subsystems.eggbeater.EggbeaterSubsystem;
import xbot.common.injection.BaseWPITest;

public class EggbeaterTestBase extends BaseWPITest{
    protected EggbeaterSubsystem agitator;
    
    public void setUp() {        
        super.setUp();
        
        agitator = injector.getInstance(EggbeaterSubsystem.class);
    }
    
    public void verifyEggbeaterSetpoints(double power){
        assertEquals(power, agitator.eggbeaterMotor.get(), 0.001);
    }

}
