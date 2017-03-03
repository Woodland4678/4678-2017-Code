package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.*;

import com.ctre.CANTalon;
import com.ctre.CANTalon.*;

public class GearClaw {
	public static CANTalon pivotMotor;
	public static DoubleSolenoid clamp;
	// Practice bot positions
	public static final int CLAW_PICKUP_POS = 3930;
	public static final int CLAW_DOWN_POS = CLAW_PICKUP_POS + 70;
	public static final int CLAW_UP_POS = 3000;
	public static final int CLAW_SCORE_POS = 3310;
	public static final int CLAW_HOLD_POS = 3100;

	// Competition bot positions
	// public static final int CLAW_PICKUP_POS = 2741;
	// public static final int CLAW_DOWN_POS = CLAW_PICKUP_POS + 70;
	// public static final int CLAW_UP_POS = 1740;
	// public static final int CLAW_SCORE_POS = 2114;
	public static enum states {
		PICKUP, CLAMP, LIFT, READYTOSCORE, SCORE, HOLD, MANUAL
	}

	public static states currentState = states.MANUAL;
	public boolean canOpenPanel = true; // used to determine if we can legally
										// open the pickup panel

	public GearClaw(int PCMCanID, int PCMForwardChannel, int PCMReverseChannel, int CANTalonID, double TalonP,
			double TalonI, double TalonD) {
		clamp = new DoubleSolenoid(PCMCanID, PCMForwardChannel, PCMReverseChannel);
		pivotMotor = new CANTalon(CANTalonID);
		pivotMotor.setPID(TalonP, TalonI, TalonD);
		pivotMotor.configMaxOutputVoltage(5);
		pivotMotor.setAllowableClosedLoopErr(30);
		// clawPivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
		pivotMotor.reverseOutput(false);
	}

	public void extend() {
		clamp.set(DoubleSolenoid.Value.kForward);
	}

	public void retract() {
		clamp.set(DoubleSolenoid.Value.kReverse);
	}

	public void down() {
		// Enc PW Pos of 1920
		pivotMotor.changeControlMode(TalonControlMode.Position);

		pivotMotor.set(CLAW_DOWN_POS);

	}

	public void up() {
		// Enc PW Pos of 890
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(CLAW_UP_POS);
	}

	public void pickup() {
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(CLAW_PICKUP_POS);
	}

	public void score() {
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(CLAW_SCORE_POS);
	}

	public void hold() {
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(CLAW_HOLD_POS);

	}

	public void stateMachine() {
		switch (currentState) {
		case PICKUP:
			canOpenPanel = false; // if we are in pickup mode we cannot deploy
									// the pickup panel
			pickup();

			extend();
			break;
		case CLAMP:
			canOpenPanel = false; // if we are in clamp mode we cannot deploy
									// the pickup panel
			down();
			retract();
			break;
		case LIFT:
			up();
			retract();
			canOpenPanel = true; // if we are in lift mode the panel will be
									// able to deploy legally
			break;
		case READYTOSCORE:
			score();
			retract();
			canOpenPanel = true; // if we are in ready to score mode the panel
									// is allowed to deploy
			break;
		case SCORE:

			
			extend();
			canOpenPanel = true; // in score mode the panel is safe to deploy
			break;
		case HOLD:
			hold();
			break;
		case MANUAL:

			break;
		}

	}

	public void setState(states newState) {
		currentState = newState;
	}

	public boolean getOpenPanelStaus() {
		return canOpenPanel;
	}

}
