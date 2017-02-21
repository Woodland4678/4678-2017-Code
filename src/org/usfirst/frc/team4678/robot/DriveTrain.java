package org.usfirst.frc.team4678.robot;



import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.VictorSP;

public class DriveTrain {
	
	public static VictorSP leftMotor;
	public static VictorSP rightMotor;
	public static Encoder leftEncoder;
	public static Encoder rightEncoder;
	public static Compressor compressor;
	public static DoubleSolenoid shifter;
	public static Joystick driveGamePad;
	public static final int AXIS = 0; //0 for Left Joystick on Controller, 2 for Right
	
	
	public static enum states{
		JOYSTICKDRIVE, AUTO, DISABLED
	}
	public static states currentState = states.AUTO;
	
	public DriveTrain(int leftPWM, int rightPWM, int compressorID, int PCMCanID, int PCMForwardChannel, int PCMReverseChannel, Joystick gamePad, int rightEncChannelA, int rightEncChannelB, int leftEncChannelA, int leftEncChannelB){
		leftMotor = new VictorSP(leftPWM);
		rightMotor = new VictorSP(rightPWM);
		
		rightEncoder = new Encoder(rightEncChannelA,rightEncChannelB,false, EncodingType.k4X);
		rightEncoder.setDistancePerPulse(1.0);
		rightEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		leftEncoder = new Encoder(leftEncChannelA,leftEncChannelB,false, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(1.0);
		leftEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		compressor = new Compressor(compressorID);
		shifter = new DoubleSolenoid(PCMCanID, PCMForwardChannel, PCMReverseChannel);
		compressor.setClosedLoopControl(true);
		driveGamePad = gamePad;
	}
	
	public void shiftUp(){
		shifter.set(DoubleSolenoid.Value.kReverse);
	}
	public void shiftDown(){
		shifter.set(DoubleSolenoid.Value.kForward);
	}
	

	
	public void stateMachine(){
		switch(currentState){
			case JOYSTICKDRIVE:
				joyStickDrive();
				break;
			case AUTO:
				
				break;
			case DISABLED:
				
				break;

		}
		
	}
	
	public double maintainSignSquare(double val){
		if(val < 0){
			return -(val*val);
		}else{
			return(val*val);
		}
	}
	public void joyStickDrive(){
		double gamePadY, gamePadX, leftPower, rightPower;
		gamePadX = maintainSignSquare(driveGamePad.getRawAxis(AXIS));
		gamePadY = maintainSignSquare(driveGamePad.getRawAxis(AXIS+1));
		if(gamePadX< 0.05 && gamePadX > -0.05){
			gamePadX = 0;
		}
		leftPower = gamePadY + 0.75*gamePadX;
		rightPower = gamePadY - 0.75*gamePadX;
		leftMotor.set(-leftPower);
		rightMotor.set(rightPower);
		
		
	}
	

	
	public void setState(states newState){
		currentState = newState;
	}
}
