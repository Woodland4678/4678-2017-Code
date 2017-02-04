package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */



public class Robot extends IterativeRobot {
	
	///Robot Port Mappings
	//Compressor
	public static final int COMPRESSOR = 0;
	//Motors
	public static final int LEFTDRIVEMOTOR = 1;
	public static final int RIGHTDRIVEMOTOR = 0;
	public static final int CLIMBERMOTOR = 2;
	public static final int CLAWPIVOTMOTOR = 2;
	
	//Pneumatics
	public static final int PCM = 0;
	public static final int LOWGEAR = 0;
	public static final int HIGHGEAR = 1;
	public static final int CLAWRETRACT = 2;
	public static final int CLAWEXTEND = 3;
	
	public static final boolean DEBUG = true;
	
	//CANTalon
	
	//Controllers
	public static final int DRIVERGAMEPAD = 0;
	public static final int OPERATORGAMEPAD = 1;
	
	//PIDConstants
	//Claw
	public static final double clawPIDP = 0.01;
	public static final double clawPIDI = 0;
	public static final double clawPIDD = 0;
	
	
	//GamePadMapping
	
	//Driver
	public static final int LEFTAXISX = 0;
	public static final int LEFTAXISY = 1;
	public static final int RIGHTAXISX = 2;
	public static final int RIGHTAXISY = 3;
	public static final int CLAWUPANDEXTENDBTN = 3;
	public static final int CLAWUPANDRETRACTBTN = 1;
	public static final int CLAWDOWNANDEXTENDBTN = 4;
	public static final int CLAWDOWNANDRETRACTBTN = 2;
	public static final int SHIFTUPBTN = 5;
	public static final int SHIFTDOWNBTN = 6;
	public static final int CLIMBFASTBTN = 7;
	public static final int CLIMBSLOWBTN = 8;
	public static String autoModes[] = {
			"Do Nothing",
			"Mode 1",
			"Mode 2",
			"Mode 3",
			"Mode 4",
			"Mode 5",
			"Mode 6",
			"Mode 7",
			"Mode 8",
			"Mode 9",
			"Mode 10"
	}; 
	
	//Operator
	
	
	//DriveTrain
	public static VictorSP driveTrainLeftMotor;
	public static VictorSP driveTrainRightMotor; 
	public static Compressor driveTrainCompressor;
	public static DoubleSolenoid driveTrainShifter;
	

	
	//Controllers
	public static Joystick driverGamePad;
	public static Joystick operatorGamePad;
	
	
	//Claw
	public static DoubleSolenoid clawGrabber;
	public static CANTalon clawPivot;
	
	//Climber
	public static VictorSP climber;
	
	//State Machine Enums
	//DriveStateMachine
	public static enum DriveStates{
		JOYSTICKDRIVE, AUTO, DISABLED
	}
	public static DriveStates currentDriveState = DriveStates.AUTO;
	
	public static enum ClawStates{
		DOWNANDEXTENDED, DOWNANDRETRACTED, UPANDRETRACTED, UPANDEXTENDED
	}
	public static ClawStates currentClawState = ClawStates.UPANDRETRACTED;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public static int autoMode = 0;
	
	
	
	
	
	@Override
	public void robotInit() {
		driveTrainInit();
		controllerInit();
		clawInit();
		climberInit();
		smartDashboard();
		
	}
	
	@Override
	public void autonomousInit() {
		
	}
	

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if(operatorGamePad.getRawButton(0)){
			autoMode = 0;
		}
		if(operatorGamePad.getRawButton(1)){
			autoMode = 1;
		}
		if(operatorGamePad.getRawButton(2)){
			autoMode = 2;
		}
		if(operatorGamePad.getRawButton(3)){
			autoMode = 3;
		}
		if(operatorGamePad.getRawButton(4)){
			autoMode = 4;
		}
		if(operatorGamePad.getRawButton(5)){
			autoMode = 5;
		}
		if(operatorGamePad.getRawButton(6)){
			autoMode = 6;
		}
		if(operatorGamePad.getRawButton(7)){
			autoMode = 7;
		}
		if(operatorGamePad.getRawButton(8)){
			autoMode = 8;
		}
		if(operatorGamePad.getRawButton(9)){
			autoMode = 9;
		}
		if(operatorGamePad.getRawButton(10)){
			autoMode = 10;
		}
		
		
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopInit(){
		currentDriveState = DriveStates.JOYSTICKDRIVE;
	}
	@Override
	public void teleopPeriodic() {
		driveStateMachine(currentDriveState);
		driverControls();
		clawStateMachine(currentClawState);
		smartDashboard();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
	}
	
	public void driveTrainInit(){
		driveTrainLeftMotor = new VictorSP(LEFTDRIVEMOTOR);
		driveTrainRightMotor = new VictorSP(RIGHTDRIVEMOTOR);
		driveTrainCompressor = new Compressor(COMPRESSOR);
		driveTrainShifter = new DoubleSolenoid(PCM, HIGHGEAR, LOWGEAR);
		driveTrainCompressor.setClosedLoopControl(true);
	}
	public void controllerInit(){

		driverGamePad = new Joystick(DRIVERGAMEPAD);
		operatorGamePad = new Joystick(OPERATORGAMEPAD);
	}
	public void clawInit(){
		clawGrabber = new DoubleSolenoid(PCM, CLAWEXTEND, CLAWRETRACT);
		clawPivot = new CANTalon(CLAWPIVOTMOTOR);
		clawPivot.changeControlMode(TalonControlMode.Position);
		clawPivot.setPID(clawPIDP, clawPIDI, clawPIDD);
	}
	public void climberInit(){
		climber = new VictorSP(CLIMBERMOTOR);
	}
	
	public void driveStateMachine(DriveStates DriveState){
		switch(DriveState){
			case JOYSTICKDRIVE:
				joyStickDrive();
				break;
			case AUTO:
				
				break;
			case DISABLED:
				
				break;

		}
	}
	public void clawStateMachine(ClawStates ClawState){	
		switch(ClawState){
		case DOWNANDEXTENDED:
			clawDown();
			clawExtend();
			break;
		case DOWNANDRETRACTED:
			clawDown();
			clawRetract();
			break;
		case UPANDEXTENDED:
			clawUp();
			clawExtend();
			break;
		case UPANDRETRACTED:
			clawUp();
			clawRetract();
			break;
		}
	}
	
	public void shiftUp(){
		driveTrainShifter.set(DoubleSolenoid.Value.kReverse);
	}
	public void shiftDown(){
		driveTrainShifter.set(DoubleSolenoid.Value.kForward);
	}
	
	public void joyStickDrive(){
		double gamePadY, gamePadX, leftPower, rightPower;
		gamePadX = driverGamePad.getRawAxis(0);
		gamePadY = driverGamePad.getRawAxis(1);
		leftPower = gamePadY + gamePadX;
		rightPower = gamePadY - gamePadX;
		driveTrainLeftMotor.set(-leftPower);
		driveTrainRightMotor.set(rightPower);
		
		
	}
	public void driverControls(){
		if(driverGamePad.getRawButton(SHIFTUPBTN)){
			shiftUp();
		}
		if(driverGamePad.getRawButton(SHIFTDOWNBTN)){
			shiftDown();
		}
		if(driverGamePad.getRawButton(CLAWUPANDEXTENDBTN)){
			currentClawState = ClawStates.UPANDEXTENDED;
		}
		if(driverGamePad.getRawButton(CLAWUPANDRETRACTBTN)){
			currentClawState = ClawStates.UPANDRETRACTED;
		}
		if(driverGamePad.getRawButton(CLIMBFASTBTN)){
			climbFast();
		}
		if(driverGamePad.getRawButton(CLIMBSLOWBTN)){
			climbSlow();
		}
		if(driverGamePad.getRawButton(CLAWDOWNANDEXTENDBTN)){
			currentClawState = ClawStates.DOWNANDEXTENDED;
		}
		if(driverGamePad.getRawButton(CLAWDOWNANDRETRACTBTN)){
			currentClawState = ClawStates.DOWNANDRETRACTED;
		}
	}
	
	public void operatorControls(){
		
	}
	
	public void clawExtend(){
		clawGrabber.set(DoubleSolenoid.Value.kForward);
	}
	public void clawRetract(){
		clawGrabber.set(DoubleSolenoid.Value.kReverse);
	}
	public void clawDown(){
		//Enc PW Pos of 1920
		clawPivot.set(1920);
		
	}
	public void clawUp(){
		//Enc PW Pos of 890
		clawPivot.set(890);
	}
	public void climbSlow(){
		climber.set(0.2);
	}
	public void climbFast(){
		climber.set(0.5);
	}
	

	public void smartDashboard(){
		if(DEBUG){
			SmartDashboard.putNumber("Claw Encoder", clawPivot.getPulseWidthPosition());
			SmartDashboard.putString("Auto Mode", autoModes[autoMode]);
		}else {
			
		}
	}
	
}

