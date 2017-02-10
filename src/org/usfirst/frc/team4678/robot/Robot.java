package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import com.ctre.CANTalon;




/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/**
 * TODO
 * - Classes
 * - Controls for Gear simplified
 * - Tune PID for gear
 * - Switchable Drive
 *
 */
 


public class Robot extends IterativeRobot {
	
	///Robot Port Mappings
	//Compressor
	public static final int COMPRESSOR = 0;
	//Motors
	public static final int LEFTDRIVEMOTOR = 1;
	public static final int RIGHTDRIVEMOTOR = 0;
	public static final int CLAWPIVOTMOTOR = 2;
	public static final int CLIMBERMOTOR = 2;

	//Pneumatics
	public static final int PCM = 0;
	public static final int LOWGEAR = 2;
	public static final int HIGHGEAR = 3;
	public static final int CLAWRETRACT = 1;
	public static final int CLAWEXTEND = 0;
	
	public static final boolean DEBUG = true;
	
	//Controllers
	public static final int DRIVERGAMEPAD = 0;
	public static final int OPERATORGAMEPAD = 1;
	
	//PIDConstants
	//Claw
	public static final double clawPIDP = 3;
	public static final double clawPIDI = 0;
	public static final double clawPIDD = 0;
	
	
	//GamePadMapping
	
	//Driver
	public static final int LEFTAXISX = 0;
	public static final int LEFTAXISY = 1;
	public static final int RIGHTAXISX = 2;
	public static final int RIGHTAXISY = 3;
	public static final int PICKUPBTN = 4;
	public static final int CLAMPBTN = 3;
	public static final int READYTOSCOREBTN = 2;
	public static final int PLACEBTN = 1;
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

	
	

	
	//Controllers
	public static Joystick driverGamePad;
	public static Joystick operatorGamePad;
	
	
	//Claw
	public static DoubleSolenoid clawGrabber;
	public static CANTalon clawPivot;
	
	//ROBOT CLASSES
	public static Climber climber;
	public static GearClaw claw;
	public static DriveTrain driveTrain;
	//State Machine Enums
	//DriveStateMachine
	
	
	
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public static int autoMode = 0;
	
	
	
	
	
	@Override
	public void robotInit() {
		controllerInit();
		driveTrain = new DriveTrain(LEFTDRIVEMOTOR, RIGHTDRIVEMOTOR, COMPRESSOR, PCM, HIGHGEAR, LOWGEAR, driverGamePad);
		climber = new Climber(CLIMBERMOTOR);
		claw = new GearClaw(PCM, CLAWEXTEND, CLAWRETRACT, CLAWPIVOTMOTOR, clawPIDP, clawPIDI, clawPIDD);
		
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
		driveTrain.setState(DriveTrain.states.JOYSTICKDRIVE);
	}
	@Override
	public void teleopPeriodic() {
		driverControls();
		driveTrain.stateMachine();
		claw.stateMachine();
		smartDashboard();
	}
	
	@Override
	public void disabledPeriodic(){
		smartDashboard();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
	}
	
	public void controllerInit(){

		driverGamePad = new Joystick(DRIVERGAMEPAD);
		operatorGamePad = new Joystick(OPERATORGAMEPAD);
	}
	
	
	public void driverControls(){
		if(driverGamePad.getRawButton(SHIFTUPBTN)){
			driveTrain.shiftUp();
		}
		if(driverGamePad.getRawButton(SHIFTDOWNBTN)){
			driveTrain.shiftDown();
		}
		if(driverGamePad.getRawButton(PICKUPBTN)){
			claw.setState(GearClaw.states.PICKUP);
		}
		if(driverGamePad.getRawButton(CLAMPBTN)){
			claw.setState(GearClaw.states.CLAMP);
		}
		if(driverGamePad.getRawButton(7)){
			claw.retract();
		}
		if(driverGamePad.getRawButton(8)){
			claw.extend();
		}
		if(driverGamePad.getRawButton(READYTOSCOREBTN)){
			claw.setState(GearClaw.states.READYTOSCORE);;
		}
		if(driverGamePad.getRawButton(PLACEBTN)){
			claw.setState(GearClaw.states.SCORE);;
		}
		
	}
	
	public void operatorControls(){
		
	}
	


	public void smartDashboard(){
		if(DEBUG){
			SmartDashboard.putNumber("Claw Encoder", claw.pivotMotor.getPulseWidthPosition());
			//SmartDashboard.putNumber("Claw Encoder 2", clawPivot.getEncPosition());
			

			SmartDashboard.putString("Auto Mode", autoModes[autoMode]);
		}else {
			
		}
	}
	
}

