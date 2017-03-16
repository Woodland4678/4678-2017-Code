package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import com.ctre.CANTalon;

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
	public static final int OPERATOR16BUTTON = 2;

	// PIDConstants
	// Claw
	public static final double clawPIDP = 3;
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
	public static Joystick operator16;

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

	public static boolean oscillate = false;
	// State Machine Enums
	// DriveStateMachine

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public static int autoMode = 0;

	@Override
	public void robotInit() {
		controllerInit();
		driveTrain = new DriveTrain(LEFT_DRIVE_MOTOR, RIGHT_DRIVE_MOTOR, COMPRESSOR, PCM, HIGH_GEAR, LOW_GEAR,
				driverGamePad, RIGHT_ENC_CHANNEL_A, RIGHT_ENC_CHANNEL_B, LEFT_ENC_CHANNEL_A, LEFT_ENC_CHANNEL_B);
		climber = new Climber(CLIMBER_MOTOR);
		claw = new GearClaw(PCM, CLAW_EXTEND, CLAW_RETRACT, CLAW_PIVOT_MOTOR, clawPIDP, clawPIDI, clawPIDD);
		baller = new Baller(BALL_PIVOT_MOTOR, BALL_ROLLER_MOTOR, WIND_MILL_SPIN_MOTOR, WIND_MILL_LIFT_MOTOR,
				PCM, HOPPER_OPEN,HOPPER_CLOSE );// SHOOTER_MOTOR, ELEVATOR_MOTOR);
		camera = CameraServer.getInstance().startAutomaticCapture(0);
		camera.setResolution(640, 480);
		camera.setFPS(30);
		pdp = new PowerDistributionPanel(0);
	}

	@Override
	public void autonomousInit() {

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if (operatorGamePad.getRawButton(0)) {
			autoMode = 0;
		}
		if (operatorGamePad.getRawButton(1)) {
			autoMode = 1;
		}
		if (operatorGamePad.getRawButton(2)) {
			autoMode = 2;
		}
		if (operatorGamePad.getRawButton(3)) {
			autoMode = 3;
		}
		if (operatorGamePad.getRawButton(4)) {
			autoMode = 4;
		}
		if (operatorGamePad.getRawButton(5)) {
			autoMode = 5;
		}
		if (operatorGamePad.getRawButton(6)) {
			autoMode = 6;
		}
		if (operatorGamePad.getRawButton(7)) {
			autoMode = 7;
		}
		if (operatorGamePad.getRawButton(8)) {
			autoMode = 8;
		}
		if (operatorGamePad.getRawButton(9)) {
			autoMode = 9;
		}
		if (operatorGamePad.getRawButton(10)) {
			autoMode = 10;
		}

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
		driverControls();
		//baller.printShooterSpeed();
		operatorControls();
		operatorBTNpadControls();
		driveTrain.stateMachine();
		claw.stateMachine();
		smartDashboard();
//		if (oscillate) {
//			baller.oscillate();
//		}
	}

	@Override
	public void disabledPeriodic() {
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
		operator16 = new Joystick(OPERATOR16BUTTON);
	}

	public void driverControls() {
		//System.out.println("Fish");
		if (driverGamePad.getRawButton(SHIFT_DOWN_BTN)) {
			driveTrain.shiftDown();
		}
		if (driverGamePad.getRawButton(SHIFT_UP_BTN)) {
			driveTrain.shiftUp();
		}
		if (driverGamePad.getRawButton(PICKUP_BTN) && baller.getCanLowerClawStatus() == true) {
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
		if (driverGamePad.getPOV() == 180) {
			baller.pickup();
		}
		if (driverGamePad.getPOV() == 0) {
			baller.enclose();
		}
		if (driverGamePad.getPOV() == 90) {
			baller.stopDown();
		}
		if (driverGamePad.getPOV() == 270) {
			baller.release();
		}

		if (operatorGamePad.getPOV() == 0) {
			climber.climbFast();
		} else if (operatorGamePad.getPOV() == 90) {
			climber.climbMedium();
		} else if (operatorGamePad.getPOV() == 180) {
			climber.climbSlow();
		} else {
			climber.climbStop();
		}
		if(driverGamePad.getRawButton(8)){
			claw.setState(GearClaw.states.READYTOSCORE);
		}
		
	}
	public void operatorBTNpadControls(){
		if(operator16.getRawButton(1)) { // Start Shooter
			//baller.shooterStart();
 		}
		if(operator16.getRawButton(2)) { // Stop Shooter
			//baller.shooterStop();
 		}
		if (operator16.getRawButton(3)) {
			//baller.hopperExtend();
			//oscillate = true;
		}
		if (operator16.getRawButton(4)) {
			//oscillate = false;
		}
		if (operator16.getRawButton(16)){
			baller.lowGoalReady();
		}
		if (operator16.getRawButton(14)){
			baller.lowGoalHopper();
		}
		if (operator16.getRawButton(15)){
			baller.lowGoalShoot();
		}
		if (operator16.getRawButton(10)){
			baller.lowGoalReverse();
		}
		if (operator16.getRawButton(13)){
			baller.lowGoalStop();
		}
	}
	public void operatorControls() {
		// System.out.println("HELLO");
		// if(operatorGamePad.getRawButton(1) && claw.canOpenPanel == true) {
		// //can only lower mills if the claw is in an upper position (same
		// situation as the pickup panel)
		// baller.lowerMills();
		// }

		if (operatorGamePad.getRawButton(1)) { // can only lower mills if the
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
		if (operatorGamePad.getRawButton(5) && claw.getOpenPanelStatus() == true) { // to
																					// open
																					// panel
																					// we
																					// need
																					// to
																					// make
																					// sure
																					// the
																					// claw
																					// is
																					// in
																					// an
																					// upper
																					// position
																					// to
																					// stay
																					// within
																					// legal
																					// volume
			// baller.deployPickUpPanel(); //opens up pickup panel
			baller.hopperRetract();
			System.out.println("deploy is allowed, opening pickup panel!");
		}
		//System.out.println("Cat");
		
		if (operatorGamePad.getRawButton(6)) { // always allowed to do this so
												// no need for extra conditions
			//System.out.println("Dog");
			baller.hopperExtend();
			SmartDashboard.putNumber("Operator Controls WOrking", 13);
			
			
			// baller.retractPickUpPanel(); //closes pickup panel
		}
		if (operatorGamePad.getRawButton(7)) {
			if (driveTrain.goToDistance(100, 100, 0.5, 20, 20, 0.3, 0.2)) {
				driveTrain.stopDriveMotors();
				driveTrain.resetGoToDistanceState();
			}
		}
		
		if(operatorGamePad.getRawButton(12)){
			driveTrain.ahrs.reset();
			driveTrain.leftEncoder.reset();
			driveTrain.rightEncoder.reset();
		}
		if(operatorGamePad.getRawButton(11)){
			driveTrain.pidTurn(90);
			if(driveTrain.pidTurn.isDone()){
				driveTrain.setState(DriveTrain.states.JOYSTICKDRIVE);
			}
		}

	}

	public void smartDashboard() {
		if (DEBUG) {

			SmartDashboard.putNumber("Right Encoder", driveTrain.rightEncoder.get());
			//SmartDashboard.putNumber("Shooter Speed", baller.getShooterSpeed());
			SmartDashboard.putNumber("Left Encoder", driveTrain.leftEncoder.get());
			SmartDashboard.putNumber("gyro", driveTrain.ahrs.getAngle());
			SmartDashboard.putNumber("gyro1", driveTrain.ahrs.getAngleAdjustment());
			SmartDashboard.putNumber("gyro2", driveTrain.ahrs.getRawGyroX());
			SmartDashboard.putNumber("gyro3", driveTrain.ahrs.getRawGyroZ());
			SmartDashboard.putNumber("gyro4", driveTrain.ahrs.getRawGyroY());
			SmartDashboard.putNumber("gyro5", driveTrain.ahrs.getRoll());
			SmartDashboard.putNumber("gyro6", driveTrain.ahrs.getPitch());
			SmartDashboard.putNumber("Compass Heading", driveTrain.ahrs.getCompassHeading());
			
			
			SmartDashboard.putNumber("Claw Encoder", claw.pivotMotor.getPulseWidthPosition());
			SmartDashboard.putNumber("Ball Pivot Encoder", baller.pivotMotor.getPulseWidthPosition());
			// SmartDashboard.putNumber("Ball Roller Encoder",
			// baller.intakeMotor.getPulseWidthPosition());
			// SmartDashboard.putNumber("Claw Encoder 2",
			// clawPivot.getEncPosition());
			SmartDashboard.putNumber("POV", driverGamePad.getPOV());
			SmartDashboard.putNumber("PDP 0 Current", pdp.getCurrent(0));
			SmartDashboard.putNumber("PDP 1 Current", pdp.getCurrent(1));
			SmartDashboard.putNumber("PDP 4 Current", pdp.getCurrent(4));
			SmartDashboard.putNumber("PDP 10 Current", pdp.getCurrent(10));
			SmartDashboard.putNumber("PDP 11 Current", pdp.getCurrent(11));
			SmartDashboard.putNumber("PDP 12 Current", pdp.getCurrent(12));
			SmartDashboard.putNumber("PDP 13 Current", pdp.getCurrent(13));
			SmartDashboard.putNumber("PDP 14 Current", pdp.getCurrent(14));
			SmartDashboard.putNumber("PDP 15 Current", pdp.getCurrent(15));
			
			SmartDashboard.putString("Auto Mode", autoModes[autoMode]);

		} else {

		}
	}

}
