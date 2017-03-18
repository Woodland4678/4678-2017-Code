package org.usfirst.frc.team4678.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

	public static VictorSP leftMotor;
	public static VictorSP rightMotor;
	public Encoder leftEncoder; // counts down when robot reverses (ball pickup
								// is back)
	public Encoder rightEncoder; // counts down when robot reverses (ball pickup
									// is back)
	
	public static SimPID pidTurn;
	public static SimPID pidDrive;
	public static SimPID encPidTurn;
	public static Compressor compressor;
	public static DoubleSolenoid shifter;
	public static Joystick driveGamePad;
	public static final int AXIS = 0; // 0 for Left Joystick on Controller, 2
										// for Right
	
	
	//PID CONSTANTS
	public static final double pDrive = 0.03;
	public static final double iDrive = 0.0075;
	public static final double dDrive = 0.02;
	public static final double epsDrive = 50;
	public static final double pTurn = 0.035;
	public static final double iTurn = 0;
	public static final double dTurn = 0.018;
	public static final double epsTurn = 3;
	
	public static final double pEncTurn = 0.05;
	public static final double iEncTurn = 0;
	public static final double dEncTurn = 0;
	public static final double epsEncTurn = 100;
	
	
	AHRS ahrs;
	// public static final int AXIS = 0; //0 for Left Joystick on Controller, 2
	// for Right
	int goToDistanceState = 0;
	int startingLeftDistance = 0;
	int startingRightDistance = 0;
	// go-to distance variables
	double targetLeft;
	double targetRight;
	double currentLeft;
	double currentRight;
	double currentLeftCentimeters;
	double currentRightCentimeters;
	double leftPercentThere;
	double rightPercentThere;
	double leftMotorMultiplier;
	double rightMotorMultiplier;
	double encoderClicksPerCentimeter = 7.2;
	double GO_TO_DISTANCE_CORRECTION_SPEED = 1.0;
	double powerOffset;
	double leftPower = 0;
	double rightPower = 0;

	public static enum states {
		JOYSTICKDRIVE, AUTO, DISABLED
	}

	public static states currentState = states.AUTO;

	public DriveTrain(int leftPWM, int rightPWM, int compressorID, int PCMCanID, int PCMForwardChannel,
			int PCMReverseChannel, Joystick gamePad, int rightEncChannelA, int rightEncChannelB, int leftEncChannelA,
			int leftEncChannelB) {
		leftMotor = new VictorSP(leftPWM);
		rightMotor = new VictorSP(rightPWM);
		ahrs = new AHRS(SPI.Port.kMXP);
		
		rightEncoder = new Encoder(rightEncChannelA, rightEncChannelB, false, EncodingType.k4X);
		rightEncoder.setDistancePerPulse(1.0);
		rightEncoder.setPIDSourceType(PIDSourceType.kRate);

		leftEncoder = new Encoder(leftEncChannelA, leftEncChannelB, false, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(1.0);
		leftEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		
		compressor = new Compressor(compressorID);
		shifter = new DoubleSolenoid(PCMCanID, PCMForwardChannel, PCMReverseChannel);
		compressor.setClosedLoopControl(true);
		driveGamePad = gamePad;
		
		pidTurn = new SimPID(pTurn, iTurn, iTurn, epsTurn);
		pidDrive = new SimPID(pDrive, iDrive, dDrive, epsDrive);
		encPidTurn = new SimPID(pEncTurn, iEncTurn, dEncTurn, epsEncTurn);
	}

	public void shiftUp() {
		shifter.set(DoubleSolenoid.Value.kReverse);
	}

	public void shiftDown() {
		shifter.set(DoubleSolenoid.Value.kForward);
	}

	public void stopDriveMotors() {
		leftMotor.set(0);
		rightMotor.set(0);
	}

	public void stateMachine() {
		switch (currentState) {
		case JOYSTICKDRIVE:
			joyStickDrive();
			break;
		case AUTO:

			break;
		case DISABLED:

			break;

		}

	}

	public double maintainSignSquare(double val) {
		if (val < 0) {
			return -(val * val);
		} else {
			return (val * val);
		}
	}

	public void joyStickDrive() {
		double gamePadY, gamePadX, leftPower, rightPower;
		gamePadX = maintainSignSquare(driveGamePad.getRawAxis(AXIS));
		gamePadY = maintainSignSquare(driveGamePad.getRawAxis(AXIS + 1));
		if (gamePadX < 0.05 && gamePadX > -0.05) {
			gamePadX = 0;
		}
		leftPower = gamePadY + 0.75 * gamePadX;
		rightPower = gamePadY - 0.75 * gamePadX;
		leftMotor.set(-leftPower);
		rightMotor.set(rightPower);

	}

	public void setState(states newState) {
		currentState = newState;
	}

	public boolean goToDistance(double rightCentimeters, double leftCentimeters, double power, int rampUpDistance,
			int rampDownDistance, double startingPower, double endingPower) {
		SmartDashboard.putNumber("Left Wheels Position", leftEncoder.get());
		SmartDashboard.putNumber("Right Wheels Position", rightEncoder.get());

		// --------------------------------------------------------------------------
		// -----------------------Reset variables if
		// necessary-----------------------
		// --------------------------------------------------------------------------

		// If this method is being called for the first time since it last
		// finished, you want to record the initial encoder values
		if (goToDistanceState == 0) {
			goToDistanceState++;
			startingLeftDistance = leftEncoder.get();
			startingRightDistance = rightEncoder.get();
			// Robot.logger.info("Drivetrain",
			// "goToDistance starting encoder values are " + getRightEncoder() +
			// ", " + getLeftEncoder());
		}


		// --------------------------------------------------------------------------
		// ---------------------Get target and current
		// distances---------------------
		// --------------------------------------------------------------------------

		// Get target distance in centimeters
		targetLeft = leftCentimeters * encoderClicksPerCentimeter; // used to be
																	// parameters
		targetRight = rightCentimeters * encoderClicksPerCentimeter; // used to
																		// be
																		// parameters
		System.out.println(
				"hi goToDistanceState=" + goToDistanceState + " " + startingLeftDistance + " " + startingRightDistance + " tleft="+targetLeft+" tright="+targetRight);

		// Get the current distance in centimeters
		currentLeft = Math.abs(leftEncoder.get() - startingLeftDistance);
		currentRight = Math.abs(rightEncoder.get() - startingRightDistance);
		currentLeftCentimeters = currentLeft / encoderClicksPerCentimeter; // used
																			// to
																			// use
																			// the
																			// parameter
		currentRightCentimeters = currentRight / encoderClicksPerCentimeter; // used
																				// to
																				// use
																				// the
																				// parameter
																				// (same
																				// name)

		// Find the percentage the left and right are to their target
		leftPercentThere = Math.abs(currentLeft / targetLeft);
		rightPercentThere = Math.abs(currentRight / targetRight);
		System.out.println("Percentages At " + leftPercentThere + ", " + rightPercentThere+" CurrentLeft="+currentLeft+" CurrentRight"+currentRight);
		System.out.println("Leftcm="+currentLeftCentimeters+" Rightcm="+currentRightCentimeters);

		// Initially set the powers to their default values
		leftMotorMultiplier = 1;
		rightMotorMultiplier = 1;

		// --------------------------------------------------------------------------
		// ----------------Adjust powers if one side has gone
		// farther----------------
		// --------------------------------------------------------------------------

		// Difference between how far the left and right have gone
		powerOffset = GO_TO_DISTANCE_CORRECTION_SPEED * Math.abs(leftPercentThere - rightPercentThere);

		// Only start adjusting the powers once the motors have gone 2 percent
		// of the target distance, to avoid calculation errors
		if (currentRight >= (targetRight * 0.02) && (currentLeft >= (targetLeft * 0.02))) {
			// If the right is closer than the left, increase the left power and
			// decrease the right power
			if (rightPercentThere > (leftPercentThere + 0.001)) {
				leftMotorMultiplier *= 1 + powerOffset;
				rightMotorMultiplier *= 1 - powerOffset;
			}

			// If the left is closer than the right, increase the right power,
			// and decrease the left power
			if ((rightPercentThere + 0.001) < leftPercentThere) {
				leftMotorMultiplier *= 1 - powerOffset;
				rightMotorMultiplier *= 1 + powerOffset;
			}
		}
		System.out.println("percentages at " + (int)(leftPercentThere * 100) + ", " + (int)(rightPercentThere * 100) + " Power Offset At " + (((int)(1000 * powerOffset)) / 1000.0));
		// --------------------------------------------------------------------------
		// -----------------------Flip the powers if
		// necessary-----------------------
		// --------------------------------------------------------------------------

		// We use the absolute values for setting the powers, so we have to flip
		// the powers based on what direction the robot is going
		if (targetRight < 0) {
			// If the robot is trying to go backwards and has not passed the
			// target
			if (rightEncoder.get() - startingRightDistance > targetRight) {
				rightMotorMultiplier *= -1;
			}
		} else {
			// If the robot is trying to go forwards and has passed the target
			if (rightEncoder.get() - startingRightDistance > targetRight) {
				rightMotorMultiplier *= -1;
			}
		}

		if (targetLeft < 0) {
			// If the robot is trying to go backwards and has not passed the
			// target
			if (leftEncoder.get() - startingLeftDistance > targetLeft) {
				leftMotorMultiplier *= -1;
			}
		} else {
			// If the robot is trying to go forwards and has passed the target
			if (leftEncoder.get() - startingLeftDistance > targetLeft) {
				leftMotorMultiplier *= -1;
			}
		}

		// --------------------------------------------------------------------------
		// -----------------------------Ramp Down
		// Speeds-----------------------------
		// --------------------------------------------------------------------------

		double rampDownPercentage = 1;
		if (currentRightCentimeters < rampUpDistance) {
			rampDownPercentage = ((currentRightCentimeters / rampUpDistance) * (1 - startingPower)) + startingPower;
			// Robot.logger.info("Drivetrain", "goToDistance ramping down " +
			// (int)(rampDownPercentage * 100) + "%");
		} else if (currentRightCentimeters > Math.abs(rightCentimeters) - rampDownDistance) {
			rampDownPercentage = (((Math.abs(rightCentimeters) - currentRightCentimeters) / rampDownDistance)
					* (1 - endingPower)) + endingPower;
			// Robot.logger.info("Drivetrain", "goToDistance ramping down " +
			// (int)(rampDownPercentage * 100) + "%");
		}

		// Robot.logger.debug("Drivetrain", "goToDistance target is " +
		// rightCentimeters + ", " + leftCentimeters + " current is " +
		// (-(int)((getRightEncoder() - startingRightDistance) /
		// Robot.encoderClicksPerCentimeter())) + ", " +
		// (-(int)((getLeftEncoder() - startingLeftDistance) /
		// Robot.encoderClicksPerCentimeter())));

		System.out.println("Left Power = "+leftMotorMultiplier * power * rampDownPercentage+" Right Power = "+rightMotorMultiplier * power * rampDownPercentage);
		leftMotor.set(leftMotorMultiplier * power * rampDownPercentage);
		rightMotor.set(rightMotorMultiplier * power * rampDownPercentage);

		// If the left and the right both have gone far enough stop the motors,
		// and reset the goToDistanceState so that the next time
		// the method is called, it will record the starting encoder values
		// again
		if (rightPercentThere >= 1 && leftPercentThere >= 1) {
			leftMotor.set(0);
			rightMotor.set(0);
			goToDistanceState = 0;
			// System.out.println("Drivetrain goToDistance at target");
			// System.out.println("Drivetrain goToDistance final encoder values
			// are "+ getRightEncoder() + ", " + getLeftEncoder());
			return true;
		}
		System.out.println(" return false here...");

		return false;
	}

	public void resetGoToDistanceState() {
		goToDistanceState = 0;
	}

	public boolean pidTurn(int angle){
		setState(DriveTrain.states.AUTO);
		pidTurn.setDesiredValue(angle);
		
		double xVal = pidTurn.calcPID(ahrs.getAngle());
		SmartDashboard.putNumber("X Val", xVal);
		double leftDrive = SimLib.calcLeftTankDrive(xVal, 0.0);
		double rightDrive = SimLib.calcRightTankDrive(xVal, 0.0);
		
			leftMotor.set(-leftDrive);
			rightMotor.set(rightDrive);

		
		SmartDashboard.putBoolean("Turn isDone", pidTurn.isDone());
		return pidTurn.isDone();
	}
	
	public boolean pidEncTurn(int encDiff){
		setState(DriveTrain.states.AUTO);
		encPidTurn.setDesiredValue(encDiff);
		
		double xVal = encPidTurn.calcPID(leftEncoder.get()-rightEncoder.get());
		SmartDashboard.putNumber("X Val", xVal);
		double leftDrive = SimLib.calcLeftTankDrive(xVal, 0.0);
		double rightDrive = SimLib.calcRightTankDrive(xVal, 0.0);
		
			leftMotor.set(leftDrive);
			rightMotor.set(-rightDrive);

		
		SmartDashboard.putBoolean("Turn isDone", encPidTurn.isDone());
		return encPidTurn.isDone();
	}
	
	public boolean pidDrive(int position){
		setState(DriveTrain.states.AUTO);
		pidDrive.setDesiredValue(position);
		double yVal = pidDrive.calcPID((leftEncoder.get()+rightEncoder.get())/2);
		double leftDrive = SimLib.calcLeftTankDrive(0, yVal);
		double rightDrive = SimLib.calcRightTankDrive(0,  yVal);
		
		if(pidDrive.isDone()){
			leftMotor.set(0);
			rightMotor.set(0);
		}else{
			leftMotor.set(leftDrive);
			rightMotor.set(-rightDrive);
		}
		
		SmartDashboard.putBoolean("Drive isDone", pidTurn.isDone());
		return pidDrive.isDone();
	}
}
