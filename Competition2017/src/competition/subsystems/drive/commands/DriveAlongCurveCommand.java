package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class DriveAlongCurveCommand extends BaseDriveCommand {
    
        private final PoseSubsystem poseSubsystem;
        private double EquationCoefficient;
        private final DoubleProperty XFinal;
        private final DoubleProperty YFinal;
        
        private final PIDManager headingDrivePid;
        private ContiguousHeading targetHeading;
        public final double defaultPValue = 1/80d;
    
    @Inject
    public DriveAlongCurveCommand
            (DriveSubsystem driveSubsystem,
            XPropertyManager prop,
            PIDFactory pidFactory,
            PoseSubsystem pose) {
        
        super(driveSubsystem);
        this.requires(driveSubsystem);
        
        this.XFinal = prop.createPersistentProperty("Final X position", 3);
        this.YFinal = prop.createPersistentProperty("Final Y position" , 2);
        headingDrivePid = pidFactory.createPIDManager("Heading module", defaultPValue, 0, 0);
        targetHeading = new ContiguousHeading();
        this.poseSubsystem = pose;
        
    }
    
    @Override
    public void initialize(){
        setEquation();
    }
    
    @Override
    public void execute(){
        
    }
    
    //Creates equation/path which robot should take
    public void setEquation(){
        this.EquationCoefficient = YFinal.get() / Math.pow(XFinal.get(), 2);
    }
    
    //Creates next point in which robot should go to
    public void nextPoint(){
        
    }
    
    public double calculateHeadingPower() {

        double errorInDegrees = targetHeading.difference(poseSubsystem.getCurrentHeading());
        double normalizedError = errorInDegrees / 180;
        double rotationalPower = headingDrivePid.calculate(0, normalizedError);

        return rotationalPower;
    }
    
    @Override
    public boolean isFinished(){
        
    }
    
    @Override
    public void end(){
        
    }

}
