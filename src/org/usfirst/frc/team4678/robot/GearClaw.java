package org.usfirst.frc.team4678.robot;

import edu.wpi.first.wpilibj.*;



import com.ctre.CANTalon;
import com.ctre.CANTalon.*;

public class GearClaw {
	public static CANTalon pivotMotor;
	public static DoubleSolenoid clamp;
	
	public static enum states{
		PICKUP, CLAMP, LIFT, READYTOSCORE ,SCORE, MANUAL
	}

	public static states currentState = states.MANUAL;
	
	public GearClaw(int PCMCanID, int PCMForwardChannel, int PCMReverseChannel, int CANTalonID, double TalonP, double TalonI, double TalonD){
		clamp = new DoubleSolenoid(PCMCanID, PCMForwardChannel, PCMReverseChannel);
		pivotMotor = new CANTalon(CANTalonID);
		pivotMotor.setPID(TalonP, TalonI, TalonD);
		pivotMotor.configMaxOutputVoltage(5);
		pivotMotor.setAllowableClosedLoopErr(30);
		//clawPivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
		pivotMotor.reverseOutput(true);
	}
	
	public void extend(){
		clamp.set(DoubleSolenoid.Value.kForward);
	}
	public void retract(){
		clamp.set(DoubleSolenoid.Value.kReverse);
	}
	public void down(){
		//Enc PW Pos of 1920
		pivotMotor.changeControlMode(TalonControlMode.Position);
		
		pivotMotor.set(4000);
		
	}
	public void up(){
		//Enc PW Pos of 890
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(3000);
	}
	
	public void pickup(){
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(3930);
	}
	public void score(){
		pivotMotor.changeControlMode(TalonControlMode.Position);
		pivotMotor.set(3350);
	}
	
	public void stateMachine(){
			switch(currentState){
			case PICKUP:
				pickup();
				extend();
				break;
			case CLAMP:
				down();
				retract();
				break;
			case LIFT:
				up();
				retract();
				break;
			case READYTOSCORE:
				score();
				retract();
				break;
			case SCORE:
				score();
				extend();
				break;
			case MANUAL:
				
				break;
			}
		
	}
	
	public void setState(states newState){
		currentState = newState;
	}
	
}
