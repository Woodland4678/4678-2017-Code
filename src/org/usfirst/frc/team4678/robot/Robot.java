package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import com.ctre.CANTalon;
import java.util.*;

import org.usfirst.frc.team4678.robot.Baller.PanelStates;

import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/**
 * TODO - Classes - Controls for Gear simplified - Tune PID for gear -
 * Switchable Drive
 *
 */

public class Robot extends IterativeRobot {

	/// Robot Port Mappings
	// Compressor
	public static final int COMPRESSOR = 0;
	// Motors
	public static final int LEFT_DRIVE_MOTOR = 1; // pwm 1
	public static final int RIGHT_DRIVE_MOTOR = 0; // pwm 0
	public static final int RIGHT_ENC_CHANNEL_A = 0;
	public static final int RIGHT_ENC_CHANNEL_B = 1;
	public static final int LEFT_ENC_CHANNEL_A = 2;
	public static final int LEFT_ENC_CHANNEL_B = 3;
	public static final int CLAW_PIVOT_MOTOR = 2;
	public static final int CLIMBER_MOTOR = 2; // pwm 2
	public static final int BALL_PIVOT_MOTOR = 0;
	public static final int BALL_ROLLER_MOTOR = 1;
	public static final int WIND_MILL_SPIN_MOTOR = 3; // pwm 3
	public static final int WIND_MILL_LIFT_MOTOR = 4; // pwm 4
	public static final int PICKUP_PANEL_SERVO_LEFT_ID = 5; // pwm 5
	public static final int PICKUP_PANEL_SERVO_RIGHT_ID = 6; // pwm 6
	//public static final int SHOOTER_MOTOR = 3;
	//public static final int ELEVATOR_MOTOR = 7; //pwm 7
	public static int drivingDirection = 0;
	
	public static final int gearSensor1 = 2;
	public static final int gearSensor2 = 3;
	public static boolean GOSCORE = false;
	
	// Pneumatics
	public static final int PCM = 0;
	public static final int LOW_GEAR = 2;
	public static final int HIGH_GEAR = 3;
	public static final int CLAW_RETRACT = 1;
	public static final int CLAW_EXTEND = 0;
	public static final int HOPPER_OPEN = 4;
	public static final int HOPPER_CLOSE = 5;
	public static final boolean DEBUG = true;

	// Controllers
	public static final int DRIVERGAMEPAD = 0;
	public static final int OPERATORGAMEPAD = 1;
	public static final int SELECTORSWITCHES = 2;

	// PIDConstants
	// Claw
	public static final double clawPIDP = 4;
	public static final double clawPIDI = 0;
	public static final double clawPIDD = 0;

	// GamePadMapping

	// Driver
	public static final int LEFT_AXISX = 0;
	public static final int LEFT_AXISY = 1;
	public static final int RIGHT_AXISX = 2;
	public static final int RIGHT_AXISY = 3;
	public static final int PICKUP_BTN = 4;
	public static final int CLAMP_BTN = 3;
	public static final int READY_TO_SCORE_BTN = 2;
	public static final int PLACE_BTN = 1;
	public static final int SHIFT_UP_BTN = 6;
	public static final int SHIFT_DOWN_BTN = 5;
	public static final int CLIMB_FAST_BTN = 7;
	public static final int CLIMB_SLOW_BTN = 8;
	public static String autoModes[] = { "Do Nothing", "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Mode 6",
			"Mode 7", "Mode 8", "Mode 9", "Mode 10" };

	// Operator

	// DriveTrain

	// Controllers
	public static Joystick driverGamePad;
	public static Joystick operatorGamePad;
	public static Joystick selectorswitches;

	// Claw
	public static DoubleSolenoid clawGrabber;
	public static CANTalon clawPivot;

	// ROBOT CLASSES
	public static Climber climber;
	public static GearClaw claw;
	public static DriveTrain driveTrain;
	public static Baller baller;

	// Camera
	public static UsbCamera camera;
	public static PowerDistributionPanel pdp;

	public static int gdist = 0;
	
	public static boolean oscillate = false;
	// State Machine Enums
	// DriveStateMachine

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public static int autoMode = 0;
	public static int teleIterations = 0;
	public static int autoIterations = 0;
	//public static String hopperState = (baller.)
	public static AutoState nothing;
	public static ArrayList<AutoState> nothingList;
	public static AutoMode nothingAuto;
	
	public static AutoState middleGearAutoState1;
	public static AutoState middleGearAutoState2;
	public static AutoState middleGearAutoState3;
	public static AutoState middleGearAutoState4;
	public static ArrayList<AutoState> middleGearAutoArrayList;
	public static AutoMode middleGearAuto;
	
	public static AutoState rightGearAutoState1;
	public static AutoState rightGearAutoState2;
	public static AutoState rightGearAutoState3;
	public static AutoState rightGearAutoState4;
	public static AutoState rightGearAutoState5;
	public static AutoState rightGearAutoState6;
	public static ArrayList<AutoState> rightGearAutoArrayList;
	public static AutoMode rightGearAuto;
	
	public static AutoState leftGearAutoState1;
	public static AutoState leftGearAutoState2;
	public static AutoState leftGearAutoState3;
	public static AutoState leftGearAutoState4;
	public static AutoState leftGearAutoState5;
	public static AutoState leftGearAutoState6;
	public static ArrayList<AutoState> leftGearAutoArrayList;
	public static AutoMode leftGearAuto;
	
	
	public static AutoState leftToMiddleAutoState1;
	public static AutoState leftToMiddleAutoState2;
	public static AutoState leftToMiddleAutoState3;
	public static AutoState leftToMiddleAutoState4;
	public static AutoState leftToMiddleAutoState5;
	public static AutoState leftToMiddleAutoState6;
	public static AutoState leftToMiddleAutoState7;
	public static AutoState leftToMiddleAutoState8;
	public static ArrayList<AutoState> leftGearToMiddleAutoArrayList;
	public static AutoMode leftGearToMiddleAuto;
	
	public static AutoState leftToBoilerAutoState1;
	public static AutoState leftToBoilerAutoState2;
	public static AutoState leftToBoilerAutoState3;
	public static AutoState leftToBoilerAutoState4;
	public static AutoState leftToBoilerAutoState5;
	public static AutoState leftToBoilerAutoState6;
	public static AutoState leftToBoilerAutoState7;
	public static AutoState leftToBoilerAutoState8;
	public static ArrayList<AutoState> leftGearToBoilerAutoArrayList;
	public static AutoMode leftGearToBoilerAuto;
	
	public static AutoState rightToMiddleAutoState1;
	public static AutoState rightToMiddleAutoState2;
	public static AutoState rightToMiddleAutoState3;
	public static AutoState rightToMiddleAutoState4;
	public static AutoState rightToMiddleAutoState5;
	public static AutoState rightToMiddleAutoState6;
	public static AutoState rightToMiddleAutoState7;
	public static AutoState rightToMiddleAutoState8;
	public static ArrayList<AutoState> rightGearToMiddleAutoArrayList;
	public static AutoMode rightGearToMiddleAuto;
	
	public static AutoState rightToBoilerAutoState1;
	public static AutoState rightToBoilerAutoState2;
	public static AutoState rightToBoilerAutoState3;
	public static AutoState rightToBoilerAutoState4;
	public static AutoState rightToBoilerAutoState5;
	public static AutoState rightToBoilerAutoState6;
	public static AutoState rightToBoilerAutoState7;
	public static AutoState rightToBoilerAutoState8;
	public static ArrayList<AutoState> rightGearToBoilerAutoArrayList;
	public static AutoMode rightGearToBoilerAuto;
	
	public static AutoMode selectedAutoMode;
	public static String autoName = "Nothing"; 
	
	public void autoModeAssemble(){
		middleGearAutoState1 = new AutoState(0,0,GearClaw.states.LIFT, 30, this);
		middleGearAutoState2 = new AutoState(-230,-230, 0.75, 15,25,0.5,0.4, GearClaw.states.READYTOSCORE, this);
		middleGearAutoState3 = new AutoState(0,0,GearClaw.states.SCORE,15, this);
		middleGearAutoState4 = new AutoState(-1500, 0,GearClaw.states.SCORE,0, this);
		middleGearAutoArrayList = new ArrayList<AutoState>();
		middleGearAutoArrayList.add(middleGearAutoState1);
		middleGearAutoArrayList.add(middleGearAutoState2);
		middleGearAutoArrayList.add(middleGearAutoState3);
		middleGearAutoArrayList.add(middleGearAutoState4);
		middleGearAuto = new AutoMode(middleGearAutoArrayList);
		
		nothing = new AutoState(0,0, this);
		nothingList = new ArrayList<AutoState>();
		nothingList.add(nothing);
		nothingAuto = new AutoMode(nothingList);
		selectedAutoMode = nothingAuto;
		rightGearAutoState1 = new AutoState(0,0,GearClaw.states.LIFT, 30, this);
		//rightGearAutoState2 = new AutoState(8200,0,GearClaw.states.LIFT, 0, this);
		rightGearAutoState2 = new AutoState(-340,-270, GearClaw.states.READYTOSCORE,  this);
		//rightGearAutoState3 = new AutoState(0,-45, GearClaw.states.READYTOSCORE, 0 ,this);
		rightGearAutoState3 = new AutoState(1000,0,GearClaw.states.READYTOSCORE, 0, this);
		rightGearAutoState4 = new AutoState(0,0,GearClaw.states.SCORE, 15, this);
		rightGearAutoState5 = new AutoState(-2400,0,GearClaw.states.SCORE, 0, this);
		rightGearAutoArrayList = new ArrayList<AutoState>();
		rightGearAutoArrayList.add(rightGearAutoState1);
		rightGearAutoArrayList.add(rightGearAutoState2);
		rightGearAutoArrayList.add(rightGearAutoState3);
		rightGearAutoArrayList.add(rightGearAutoState4);
		rightGearAutoArrayList.add(rightGearAutoState5);
		//rightGearAutoArrayList.add(rightGearAutoState6);
		rightGearAuto = new AutoMode(rightGearAutoArrayList);
		
		
		
		rightToMiddleAutoState1 = rightGearAutoState1;
		rightToMiddleAutoState2 = rightGearAutoState2;
		rightToMiddleAutoState3 = rightGearAutoState3;
		rightToMiddleAutoState4 = rightGearAutoState4;
		rightToMiddleAutoState5 = rightGearAutoState5;
		//rightToMiddleAutoState6 = rightGearAutoState6;
		rightToMiddleAutoState6 = new AutoState(0,45,GearClaw.states.LIFT, 0, this);
		rightToMiddleAutoState7 = new AutoState(17000,0,GearClaw.states.LIFT, 0, this);
		rightGearToMiddleAutoArrayList = new ArrayList<AutoState>();
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState1);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState2);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState3);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState4);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState5);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState6);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState7);
		//rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState8);
		rightGearToMiddleAuto = new AutoMode(rightGearToMiddleAutoArrayList);
		
		
		rightToBoilerAutoState1 = rightGearAutoState1;
		rightToBoilerAutoState2 = rightGearAutoState2;
		rightToBoilerAutoState3 = rightGearAutoState3;
		rightToBoilerAutoState4 = rightGearAutoState4;
		rightToBoilerAutoState5 = rightGearAutoState5;
		//rightToMiddleAutoState6 = rightGearAutoState6;
		rightToBoilerAutoState6 = new AutoState(GearClaw.states.LIFT, this);
		rightToBoilerAutoState7 = new AutoState(268,276,Baller.autoStates.DEPLOY,this);
		rightToBoilerAutoState8 = new AutoState(Baller.autoStates.SHOOT, this);
		rightGearToBoilerAutoArrayList = new ArrayList<AutoState>();
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState1);
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState2);
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState3);
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState4);
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState5);
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState6);
		rightGearToBoilerAutoArrayList.add(rightToMiddleAutoState7);
		rightGearToMiddleAutoArrayList.add(rightToMiddleAutoState8);
		rightGearToBoilerAuto = new AutoMode(rightGearToMiddleAutoArrayList);
		
		
		leftGearAutoState1 = new AutoState(0,0,GearClaw.states.LIFT, 30, this);
		leftGearAutoState2 = new AutoState(-270,-340, GearClaw.states.READYTOSCORE,  this);
		//leftGearAutoState3 = new AutoState(0,50, GearClaw.states.READYTOSCORE, 0 ,this);
		leftGearAutoState3 = new AutoState(1000,0,GearClaw.states.READYTOSCORE, 0, this);
		leftGearAutoState4 = new AutoState(0,0,GearClaw.states.SCORE, 15, this);
		leftGearAutoState5 = new AutoState(-2400,0,GearClaw.states.SCORE, 0, this);
		leftGearAutoArrayList = new ArrayList<AutoState>();
		leftGearAutoArrayList.add(leftGearAutoState1);
		leftGearAutoArrayList.add(leftGearAutoState2);
		leftGearAutoArrayList.add(leftGearAutoState3);
		leftGearAutoArrayList.add(leftGearAutoState4);
		leftGearAutoArrayList.add(leftGearAutoState5);
		//leftGearAutoArrayList.add(leftGearAutoState6);
		leftGearAuto = new AutoMode(leftGearAutoArrayList);
		
		leftToMiddleAutoState1 = leftGearAutoState1;
		leftToMiddleAutoState2 = leftGearAutoState2;
		leftToMiddleAutoState3 = leftGearAutoState3;
		leftToMiddleAutoState4 = leftGearAutoState4;
		leftToMiddleAutoState5 = leftGearAutoState5;
		//leftToMiddleAutoState6 = leftGearAutoState6;
		leftToMiddleAutoState6 = new AutoState(0,-50,GearClaw.states.LIFT, 0, this);
		leftToMiddleAutoState7 = new AutoState(17000,0,GearClaw.states.LIFT, 0, this);
		leftGearToMiddleAutoArrayList = new ArrayList<AutoState>();
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState1);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState2);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState3);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState4);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState5);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState6);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState7);
		//leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState8);
		leftGearToMiddleAuto = new AutoMode(leftGearToMiddleAutoArrayList);
		
		leftToBoilerAutoState1 = leftGearAutoState1;
		leftToBoilerAutoState2 = leftGearAutoState2;
		leftToBoilerAutoState3 = leftGearAutoState3;
		leftToBoilerAutoState4 = leftGearAutoState4;
		leftToBoilerAutoState5 = leftGearAutoState5;
		//rightToMiddleAutoState6 = rightGearAutoState6;
		leftToBoilerAutoState6 = new AutoState(GearClaw.states.LIFT, this);
		leftToBoilerAutoState7 = new AutoState(276,268,Baller.autoStates.DEPLOY,this);
		
		leftToBoilerAutoState8 = new AutoState(Baller.autoStates.SHOOT, this);
		leftGearToBoilerAutoArrayList = new ArrayList<AutoState>();
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState1);
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState2);
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState3);
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState4);
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState5);
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState6);
		leftGearToBoilerAutoArrayList.add(leftToMiddleAutoState7);
		leftGearToMiddleAutoArrayList.add(leftToMiddleAutoState8);
		leftGearToBoilerAuto = new AutoMode(leftGearToMiddleAutoArrayList);
		
	}
	@Override
	public void robotInit() {
		controllerInit();
		driveTrain = new DriveTrain(LEFT_DRIVE_MOTOR, RIGHT_DRIVE_MOTOR, COMPRESSOR, PCM, HIGH_GEAR, LOW_GEAR,
				driverGamePad, RIGHT_ENC_CHANNEL_A, RIGHT_ENC_CHANNEL_B, LEFT_ENC_CHANNEL_A, LEFT_ENC_CHANNEL_B);
		climber = new Climber(CLIMBER_MOTOR);
		claw = new GearClaw(PCM, CLAW_EXTEND, CLAW_RETRACT, CLAW_PIVOT_MOTOR, clawPIDP, clawPIDI, clawPIDD, gearSensor1, gearSensor2, this);
		baller = new Baller(BALL_PIVOT_MOTOR, BALL_ROLLER_MOTOR, WIND_MILL_SPIN_MOTOR, WIND_MILL_LIFT_MOTOR,
				PCM, HOPPER_OPEN,HOPPER_CLOSE );// SHOOTER_MOTOR, ELEVATOR_MOTOR);
		camera = CameraServer.getInstance().startAutomaticCapture(0);
		camera.setResolution(640, 480);
		camera.setFPS(30);
		pdp = new PowerDistributionPanel(0);
		autoModeAssemble();
		resetSensors();
		leftGearAuto.currentState = 0;
		smartDashboard();
	
		
		
	}
	
	@Override
	public void autonomousInit() {
		selectedAutoMode.currentState = 0;
		driveTrain.shiftDown();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		smartDashboard();
		
		autoIterations++;
		SmartDashboard.putNumber("Right Encoder", driveTrain.rightEncoder.get());
		//SmartDashboard.putNumber("Shooter Speed", baller.getShooterSpeed());
		SmartDashboard.putNumber("Left Encoder", driveTrain.leftEncoder.get());
		SmartDashboard.putNumber("Auto State", rightGearAuto.currentState);
		if(autoName == "Nothing"){
			
		}else{
			selectedAutoMode.runMode();
		}
		//driveTrain.pidTurn(90);
		//driveTrain.pidEncTurn(500);
	}

	/**
	 * This function is called periodically during operator control
	 */

	@Override
	public void teleopInit() {
		driveTrain.setState(DriveTrain.states.JOYSTICKDRIVE);
	}

	@Override
	public void teleopPeriodic() {
		baller.hopperDashboardPrint();
		SmartDashboard.putNumber("Tele Iterations", teleIterations);
		SmartDashboard.putNumber("Auto Iterations", autoIterations);
		SmartDashboard.putString("Hopper: ", Baller.hopperPrint);
		teleIterations++;
		driverControls();
		//baller.printShooterSpeed();
		operatorControls();
		//operatorBTNpadControls();
		driveTrain.stateMachine();
		claw.stateMachine();
		smartDashboard();
//		if (oscillate) {
//			baller.oscillate();
//		}
	}

	@Override
	public void disabledPeriodic() {
		if(readSelector1() == 0){
			if(readSelector2() == 0){
				selectedAutoMode = leftGearAuto; 
				autoName = "leftGearAuto";
			}else if(readSelector2() == 1){
				selectedAutoMode = middleGearAuto;
				autoName = "middleGearAuto";
			}else if(readSelector2() == 2){
				selectedAutoMode = rightGearAuto;
				autoName = "rightGearAuto";
			}else if(readSelector2() == 3){
				selectedAutoMode = leftGearToMiddleAuto;
				autoName = "leftGearToMiddleAuto";
			}else if(readSelector2() == 4){
				selectedAutoMode = rightGearToMiddleAuto;
				autoName = "rightGearToMiddleAuto";
			}else if(readSelector2() == 5){
				selectedAutoMode = rightGearToBoilerAuto;
				autoName = "rightGearToBoilerAuto";
			}else if(readSelector2() == 6){
				
			}else if(readSelector2() == 7){
				
			}
		}else if(readSelector1() == 1){
			if(readSelector2() == 0){
				selectedAutoMode = leftGearAuto; 
				autoName = "leftGearAuto";
			}else if(readSelector2() == 1){
				selectedAutoMode = middleGearAuto;
				autoName = "middleGearAuto";
			}else if(readSelector2() == 2){
				selectedAutoMode = rightGearAuto;
				autoName = "rightGearAuto";
			}else if(readSelector2() == 3){
				selectedAutoMode = leftGearToMiddleAuto;
				autoName = "leftGearToMiddleAuto";
			}else if(readSelector2() == 4){
				selectedAutoMode = rightGearToMiddleAuto;
				autoName = "rightGearToMiddleAuto";
			}else if(readSelector2() == 5){
				selectedAutoMode = leftGearToBoilerAuto;
				autoName = "leftGearToBoilerAuto";
			}else if(readSelector2() == 6){
				
			}else if(readSelector2() == 7){
				
			}
		}else{
				autoName = "Nothing";
		}
		
		if(driverGamePad.getRawButton(1)){
			resetSensors();
		}
		
		smartDashboard();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}

	public void controllerInit() {

		driverGamePad = new Joystick(DRIVERGAMEPAD);
		operatorGamePad = new Joystick(OPERATORGAMEPAD);
		selectorswitches = new Joystick(SELECTORSWITCHES);
	}

	public void driverControls() {
		//System.out.println("Fish");
		
		if(claw.currentState != GearClaw.states.PICKUP){
			GOSCORE = true;
		}else{
			GOSCORE = false;
		}
		if (driverGamePad.getRawButton(SHIFT_DOWN_BTN)) {
			driveTrain.shiftDown();
		}
		if (driverGamePad.getRawButton(SHIFT_UP_BTN)) {
			driveTrain.shiftUp();
		}
		if (driverGamePad.getRawButton(PICKUP_BTN) && baller.getCanLowerClawStatus() == true && claw.currentState != GearClaw.states.CLAMP && claw.currentState != GearClaw.states.PICKUP) {
			claw.setState(GearClaw.states.PICKUP);
		}
		if (driverGamePad.getRawButton(CLAMP_BTN) && baller.getCanLowerClawStatus() == true) {
			claw.setState(GearClaw.states.CLAMP);
		}
		if (driverGamePad.getRawButton(READY_TO_SCORE_BTN)) {
			claw.setState(GearClaw.states.HOLD);
			;
		}
		if (driverGamePad.getRawButton(PLACE_BTN)) {
			claw.setState(GearClaw.states.SCORE);
			;
		}
		
		
		if(driverGamePad.getRawAxis(DriveTrain.AXIS +1) < -0.3){
			drivingDirection = 1;
		}else if(driverGamePad.getRawAxis(DriveTrain.AXIS +1) > 0.3){
			drivingDirection = -1;
		}
		if(driverGamePad.getRawButton(7) && drivingDirection == 1){
			driveTrain.shiftUp();
			driveTrain.setState(DriveTrain.states.PIVOTLEFTGEARFORWARD);
		}else if(driverGamePad.getRawButton(8) && drivingDirection == 1){
			driveTrain.shiftUp();
			driveTrain.setState(DriveTrain.states.PIVOTRIGHTGEARFORWARD);
		}else if(driverGamePad.getRawButton(7) && drivingDirection == -1){
			driveTrain.shiftUp();
			driveTrain.setState(DriveTrain.states.PIVOTLEFTBALLFORWARD);
		}else if(driverGamePad.getRawButton(8) && drivingDirection == -1){
			driveTrain.shiftUp();
			driveTrain.setState(DriveTrain.states.PIVOTRIGHTBALLFORWARD);
		}else
		{
			driveTrain.setState(DriveTrain.states.JOYSTICKDRIVE);
		}
//		if (driverGamePad.getPOV() == 180) {
//			baller.pickup();
//		}
//		if (driverGamePad.getPOV() == 0) {
//			baller.enclose();
//		}
//		if (driverGamePad.getPOV() == 90) {
//			baller.stopDown();
//		}
//		if (driverGamePad.getPOV() == 270) {
//			baller.release();
//		}
		/*if(driverGamePad.getRawButton(8)){
			claw.setState(GearClaw.states.READYTOSCORE);
		}*/
		
		
		
	}
//	public void operatorBTNpadControls(){
//		if(operator16.getRawButton(1)) { // reset GotoDistance code
//			driveTrain.resetGoToDistanceState();
//			gdist = 0; // allow gotodistance to run
//			driveTrain.leftEncoder.reset();
//			driveTrain.rightEncoder.reset();
//  		}
//		if(operator16.getRawButton(2)) { // Test goto distance code
//			driveTrain.setState(DriveTrain.states.AUTO); // Disable the joystick drive control
//			if (gdist == 0) {
//				if(driveTrain.goToDistance(-260, -330, 1, 15, 25, 0.50, 0.50)) { // rightCentimeters, leftCentimeters, power, rampUpDistance,
//					gdist = 1; // end go to Distance when we get true
//					}	// rampDownDistance, startingPower, endingPower
//			}
// 		}
//		else if (DriveTrain.currentState == DriveTrain.states.AUTO)
//		{
//			driveTrain.setState(DriveTrain.states.JOYSTICKDRIVE); // back to JOYSTICK when we let go of the button
//		}
//		if (operator16.getRawButton(3)) {
//			//baller.hopperExtend();
//			//oscillate = true;
//		}
//		if (operator16.getRawButton(4)) {
//			//oscillate = false;
//		}
//		if (operator16.getRawButton(1)) {
//			claw.setState(GearClaw.states.HOLD);
//			;
//		}
//		if (operator16.getRawButton(16)){
//			baller.lowGoalReady();
//		}
//		if (operator16.getRawButton(14)){
//			baller.lowGoalHopper();
//		}
//		if (operator16.getRawButton(15)){
//			baller.lowGoalShoot();
//		}
//		if (operator16.getRawButton(10)){
//			baller.lowGoalReverse();
//		}
//		if (operator16.getRawButton(13)){
//			baller.lowGoalStop();
//		}
//		if (operator16.getRawButton(12)){
//			baller.pickup();
//		}
//		if (operator16.getRawButton(9)){
//			baller.enclose();
//		}
//		if (operator16.getRawButton(8)){
//			baller.stopDown();
//		}
//	}
	public void operatorControls() {
		// System.out.println("HELLO");
		// if(operatorGamePad.getRawButton(1) && claw.canOpenPanel == true) {
		// //can only lower mills if the claw is in an upper position (same
		// situation as the pickup panel)
		// baller.lowerMills();
		// }

		if ((operatorGamePad.getRawButton(1))&&(claw.clawIsInsideBumper())) { // can only lower mills if the
												// claw is in an upper position
												// (same situation as the pickup
												// panel)
			// System.out.println("Trying to lower the wind mills!!!!");
			baller.lowerMills();
		}
		if (operatorGamePad.getRawButton(4)) {
			baller.stopMills(); // stops the mills from spinning
			baller.liftMills(); // lifts mills to rest position
		}
		if (operatorGamePad.getRawButton(3)) {
			baller.spinMillOut(); // spins the wind mills away from the robot
		}
		if (operatorGamePad.getRawButton(2)) {
			baller.spinMillIn(); // spins wind mills towards the intake
		}
		if ((operatorGamePad.getRawButton(5))&&(claw.clawIsInsideBumper())) { 
			//System.out.println("hopperExtend");
			baller.hopperExtend();
			//System.out.println("deploy is allowed, opening pickup panel!");
		}
		//System.out.println("Cat");
		
		if (operatorGamePad.getRawButton(6)) { // always allowed to do this so
												// no need for extra conditions
			//System.out.println("hopperRetract");
			//System.out.println("Dog");
			baller.hopperRetract();
			//SmartDashboard.putNumber("Operator Controls WOrking", 13);
			
			
			// baller.retractPickUpPanel(); //closes pickup panel
		}
		if (operatorGamePad.getRawButton(7)) {
//			if (driveTrain.goToDistance(100, 100, 0.5, 20, 20, 0.3, 0.2)) {
//				driveTrain.stopDriveMotors();
//				driveTrain.resetGoToDistanceState();
//			}
		}
		
		if(operatorGamePad.getRawButton(12)){
			resetSensors();
		}
		//if(operatorGamePad.getRawButton(11)){
		//	claw.setState(GearClaw.states.LIFT);
		//}
		if (operatorGamePad.getRawButton(8)) {
			climber.climbFast();
		}
//		} else if (operatorGamePad.getPOV() == 90) {
//			climber.climbMedium();
//		} else if (operatorGamePad.getPOV() == 180) {
//			climber.climbSlow();
		else {
		climber.climbStop();
		}
		// Use joystick to operate the low goal shooting instead of 16 button pad
		// Left and Right to activate the agitator (right = fwd, left = rvs)
		// Up to run the roller in full speed feed into low goal
		// Down to run the roller in low speed, increasing speed the further the joystick
		// is moved in the downward direction
		// pressing the joystick down (button 11) to reverse the rollers, agitators to
		// run in the away from roller direction (reverse).
		// The agitators can be activate this way at any time.  Other functions may
		// require some pre-requisite conditions
		// If door is closed, only the agitators will work.  Otherwise, low goal
		// functions will be activated, provide the pivot is in the scoring position
		// If it's not, the up joystick function will send it to scoring position and 
		// then allow low goal roller operation.
		// Testing shows we shouldn't do much of anything for joystick axis values
		// below 0.25. Fully diagonal positions do not return 1 for both axis,
		// more like 0.75 so anything over 0.5 should be good for full on.
		// Released position does not always provide a 0.0 return value but it will certainly
		// be less than 0.25
		double ogpxaxis = operatorGamePad.getRawAxis(LEFT_AXISX);
		double ogpyaxis = operatorGamePad.getRawAxis(LEFT_AXISY);
		//System.out.println("ogpxaxis = "+ogpxaxis+" ogpyaxis = "+ogpyaxis);
		if (ogpxaxis > 0.25) { // activate agitator based on x axis
			baller.agitate(1.0);
		}
		else if (ogpxaxis < -0.25) {
			baller.agitate(-1.0);
		}
		else {
			baller.agitate(0.0); // turn it off if joystick is in middle.
		}
		if ((Baller.panelState == PanelStates.DEPLOYED)&&(Baller.intakeState != Baller.IntakeStates.PICKUP)) { // Only when deployed
			// and not in PICKUP mode ...
			if (operatorGamePad.getRawButton(11)) { // If joystick pressed, this is reverse
				baller.lowGoalReverse();
				baller.agitate(-1.0);
			}
			else { // other low goal stuff can happen as long as we're not pressing the joystick
				if (ogpyaxis < -0.5) { // When pressing up ...
						if (!baller.lowGoalIsReady()) {
							baller.lowGoalReady();
						}
						else {
							baller.lowGoalShoot(); // shoot when ready.
						}
					}
				else if (ogpyaxis > 0.25) { // when joystick is moved down, vary speed of shooter from slow to fast. 
					baller.lowGoalVarShoot(ogpyaxis * -35000); // will be from about 9000 to 35000
					}
				else
					baller.lowGoalStop(); // stop rollers if joystick is put in center.
			}
		}
		if (Baller.panelState == PanelStates.DEPLOYED){
			if (operatorGamePad.getPOV() == 90) {
				baller.pickup();
			}
			if (operatorGamePad.getPOV() == 180) {
				baller.enclose();
			}
			if (operatorGamePad.getPOV() == 270) {
				baller.stopDown();
			}
		}
	}
	
	public int readSelector1() { // Read the left selector switch for auto configuration
		int rval;
		
		rval = 0;
		if (selectorswitches.getRawButton(14))
			rval += 1;
		if (selectorswitches.getRawButton(15))
			rval += 2;
		if (selectorswitches.getRawButton(16))
			rval += 4;
		return(rval);
	}
	
	public int readSelector2() { // Read the right selector switch for auto configuration
		int rval;
		
		rval = 0;
		if (selectorswitches.getRawButton(13))
			rval += 1;
		if (selectorswitches.getRawButton(12))
			rval += 2;
		if (selectorswitches.getRawButton(11))
			rval += 4;
		return(rval);
	}

	public void smartDashboard() {
		if (DEBUG) {

			SmartDashboard.putNumber("Right Encoder", driveTrain.rightEncoder.get());
			//SmartDashboard.putNumber("Shooter Speed", baller.getShooterSpeed());
			SmartDashboard.putNumber("Left Encoder", driveTrain.leftEncoder.get());
			SmartDashboard.putNumber("gyro adr", driveTrain.gyro.getAngle());
			SmartDashboard.putNumber("Claw Encoder", claw.pivotMotor.getPulseWidthPosition());
			SmartDashboard.putNumber("Ball Pivot Encoder", baller.pivotMotor.getPulseWidthPosition());
			SmartDashboard.putNumber("First climber motor", pdp.getCurrent(12));
			SmartDashboard.putNumber("Second Climber motor", pdp.getCurrent(2));
			// SmartDashboard.putNumber("Ball Roller Encoder",
			// baller.intakeMotor.getPulseWidthPosition());
			// SmartDashboard.putNumber("Claw Encoder 2",
			// clawPivot.getEncPosition());
			SmartDashboard.putString("Auto Mode", autoName);
		//	SmartDashboard.putNumber("Auto Mode", readSelector1());
			
			SmartDashboard.putNumber("AutoState" , selectedAutoMode.currentState);
			SmartDashboard.putNumber("gear Right", claw.gearRight.getValue());
			SmartDashboard.putNumber("gear Left", claw.gearLeft.getValue());
			SmartDashboard.putBoolean("GoScore", GOSCORE);

		} else {

		}
	}
	
	public void resetSensors(){
		driveTrain.leftEncoder.reset();
		driveTrain.rightEncoder.reset();
		driveTrain.gyro.reset();
	}

}
