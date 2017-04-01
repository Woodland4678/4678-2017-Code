package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.*;

import com.ctre.CANTalon;
import com.ctre.CANTalon.*;


public class GearClaw {
	public static CANTalon pivotMotor;
	public static DoubleSolenoid clamp;
	public static AnalogInput gearLeft;
	public static AnalogInput gearRight;
	public static Boolean toGoUp = false;
	// Competition bot positions
	// public static final int CLAW_PICKUP_POS = 2741;
	
	// Practice bot positions
	public static final int CLAW_PICKUP_POS = 250; // 3780 practice
	public static final int CLAW_DOWN_POS = CLAW_PICKUP_POS - 150;
	public static final int CLAW_UP_POS = CLAW_PICKUP_POS + 930*3; // 2850 
	public static final int CLAW_SCORE_POS = CLAW_PICKUP_POS + 585*3; // 3150
	public static final int CLAW_HOLD_POS = CLAW_PICKUP_POS + 980*3; // 2950 

	// Before new gear system added.
	//public static final int CLAW_PICKUP_POS = 2712; // 3780 practice
	//public static final int CLAW_DOWN_POS = CLAW_PICKUP_POS + 70;
	//public static final int CLAW_UP_POS = CLAW_PICKUP_POS - 930; // 2850 
	//public static final int CLAW_SCORE_POS = CLAW_PICKUP_POS - 585; // 3150
	//public static final int CLAW_HOLD_POS = CLAW_PICKUP_POS - 830; // 2950 
	
	public static int[] leftGearValues = {0,0,0,0,0};
	public static int[] rightGearValues = {0,0,0,0,0};
	//public static int lastGearRightVal = 0;
	public static int iterator = 1000;

	public static enum states {
		PICKUP, CLAMP, LIFT, READYTOSCORE, SCORE, HOLD, MANUAL
	}

	public static states currentState = states.MANUAL;
	public boolean canOpenPanel = true; // used to determine if we can legally
										// open the pickup panel
	public static Robot robot;
	public GearClaw(int PCMCanID, int PCMForwardChannel, int PCMReverseChannel, int CANTalonID, double TalonP,
			double TalonI, double TalonD, int gearSensor1, int gearSensor2, Robot robot) {
		clamp = new DoubleSolenoid(PCMCanID, PCMForwardChannel, PCMReverseChannel);
		pivotMotor = new CANTalon(CANTalonID);
		pivotMotor.setPID(TalonP, TalonI, TalonD);
		pivotMotor.configMaxOutputVoltage(12); // Was 8
		pivotMotor.setAllowableClosedLoopErr(5);
		// clawPivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
		pivotMotor.reverseOutput(false);
		
		gearLeft = new AnalogInput(gearSensor1);
		gearRight = new AnalogInput(gearSensor2);
		
		this.robot = robot;
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
	
	public boolean clawIsInsideBumper() { // return true if claw is inside the bumper
		// testing shows that this takes place when claw position is lower than 3036 
		// which is CLAW_PICKUP_POS - 744
		if (pivotMotor.getPulseWidthPosition() < CLAW_PICKUP_POS - 744)
			return true;
		else
			return false;
	}

	public void stateMachine() { // called by Robot.java at 50Hz
		
		this.getGearValues();
		switch (currentState) {
		case PICKUP:
			canOpenPanel = false; // if we are in pickup mode we cannot deploy
			this.autoGearClamp();						// the pickup panel
			pickup();

			extend();
			break;
		case CLAMP:
			canOpenPanel = false; // if we are in clamp mode we cannot deploy
	waitThenClamp();	
			
			if(!robot.driverGamePad.getRawButton(robot.PICKUP_BTN) && toGoUp){
				this.setState(GearClaw.states.LIFT);
			}
			// the pickup panel
			down();
			retract();
			break;
		case LIFT:
			up();
			retract();
			toGoUp = false;
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
	public void autoGearClamp(){
		

		
		if(leftGearValues[0] > 1400 && leftGearValues[4]< 500 ){
			this.setState(GearClaw.states.CLAMP);
			
			iterator = 0;
			//this.setState(GearClaw.states.LIFT);
		}
		
		
		if(rightGearValues[0] > 1200 && rightGearValues[4] <600){
	this.setState(GearClaw.states.CLAMP);
			
			iterator = 0;
		}
		
	}
	
	public void getGearValues(){
		leftGearValues[0] = leftGearValues[1];
		leftGearValues[1] = leftGearValues[2];
		leftGearValues[2] = leftGearValues[3];
		leftGearValues[3] = leftGearValues[4];
		leftGearValues[4] = gearLeft.getValue();
		
		rightGearValues[0] = rightGearValues[1];
		rightGearValues[1] = rightGearValues[2];
		rightGearValues[2] = rightGearValues[3];
		rightGearValues[3] = rightGearValues[4];
		rightGearValues[4] = gearRight.getValue();
	}
	
	public void setState(states newState) {
		currentState = newState;  
	}

	public boolean getOpenPanelStatus() {
		return canOpenPanel;
	}
	
	public void waitThenClamp(){
		
		iterator++;
		if(iterator == 13){
			toGoUp = true;
			iterator = 1000;
		}
	}
	
	
	public boolean clawContained(){
		if(currentState == states.LIFT){
			return true;
		}else{
			return false;
		}
	}

}
