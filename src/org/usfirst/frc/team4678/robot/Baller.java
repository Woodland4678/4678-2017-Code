package org.usfirst.frc.team4678.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Servo;

public class Baller {

	// PID Constants
	public static final double intakeP = 0.05;
	public static final double intakeI = 0.00;
	public static final double intakeD = 0.00;

	public static final double pivotP = 1.0;
	public static final double pivotI = 0.001;
	public static final double pivotD = 0.00;

	// Pivot Motor Constants
	public static final int INTAKE_PICKUP_HEIGHT = 3500;
	public static final int INTAKE_RELEASE_HEIGHT = 2100;
	public static final int INTAKE_ENCLOSED_HEIGHT = 1700;

	// Competition bot positions
	// public static final int INTAKE_PICKUP_HEIGHT = 3600;
	// public static final int INTAKE_RELEASE_HEIGHT = 2314;
	// public static final int INTAKE_ENCLOSED_HEIGHT = 1988;

	public static Servo windMillLift;
	public static VictorSP windMillSpin; // servos that are controlled by a
											// victor

	public static Servo pickUpPanelLeft;
	public static Servo pickUpPanelRight;

	// Intake Motor Constants
	public static final int PICKUPSPEED = 20000;
	public static final int RELEASESPEED = -22000;
	public static CANTalon pivotMotor;
	public static CANTalon intakeMotor;
	public static VictorSP agitator;
	public static Solenoid hopperPneumatic;

	public static boolean canLowerClaw = true; // used to determine if the claw
												// can lower and we stay within
												// the legal limit
	public static String pickupPosition = "enclosed"; // currently not used for
														// anything
	public static String panelState = "retracted"; // keeps track of panel
													// position used to make
													// sure the claw is not
													// deployed while this is
													// equal to "deployed"
	public static String millState = "retracted"; // keep track of panel
													// position used to make
													// sure the claw is not
													// deployed while this is
													// equal to "lower"

	public Baller(int pivotMotorID, int intakeMotorID, int windMillLiftID, int windMillSpinID, int PCMCanID,
			int hopperPCM) {
		pivotMotor = new CANTalon(pivotMotorID);
		intakeMotor = new CANTalon(intakeMotorID);
		pivotMotor.setPID(pivotP, pivotI, pivotD);
		intakeMotor.setPID(intakeP, intakeI, intakeD);
		pivotMotor.setAllowableClosedLoopErr(30);
		intakeMotor.setAllowableClosedLoopErr(200);
		pivotMotor.configMaxOutputVoltage(7);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
		agitator = new VictorSP(5);
		hopperPneumatic = new Solenoid(PCMCanID, hopperPCM);
		windMillLift = new Servo(windMillLiftID);
		windMillSpin = new VictorSP(windMillSpinID);
	}

	public void pickup() {
		pickupPosition = "Pick Up";
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_PICKUP_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(PICKUPSPEED);
	}

	public void release() {
		pickupPosition = "Release";
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_RELEASE_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(RELEASESPEED);
	}

	public void enclose() {
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);

		pivotMotor.set(INTAKE_ENCLOSED_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}
	public void stopDown(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_PICKUP_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}

	public void agitate(int status) {
		if (status == 1) {
			agitator.set(1);
			System.out.println("DOG");
		} else {
			agitator.set(0);
		}
	}

	public void hopperRetract() {
		hopperPneumatic.set(true);

	}

	public void hopperExtend() {
		hopperPneumatic.set(false);
	}

	public void lowerMills() {
		System.out.println("now in the lowermills function");
		millState = "lower";
		canLowerClaw = false;
		windMillLift.set(0.58); // position (between 0 - 1 I believe)
	}

	public void liftMills() {
		millState = "retracted";
		if (panelState == "retracted") {
			canLowerClaw = true; // if panel closed we can lower the claw
		}
		windMillLift.set(0); // position
	}

	public void spinMillOut() {
		if (millState == "lower") { // only want to spin the wind mills if they
									// are deployed
			windMillSpin.set(0.9); // speed value
		} else {
			windMillSpin.disable();
		}
	}

	public void spinMillIn() {
		if (millState == "lower") { // only want to spin the wind mills if they
									// are deployed
			windMillSpin.set(-0.9); // speed value
		} else {
			windMillSpin.disable();
		}

	}

	public void stopMills() {
		windMillSpin.disable();
	}

	public boolean getCanLowerClawStatus() {
		return canLowerClaw;
	} // accessor variable to tell us if we are allowed to lower claw based on
		// panel position

	public String getPickupPosition() {
		return pickupPosition;
	} // accessor variable to tell us where the ball pickup currently is

}
