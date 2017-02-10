package org.usfirst.frc.team4678.robot;



import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;

public class DriveTrain {
	
	public static VictorSP leftMotor;
	public static VictorSP rightMotor; 
	public static Compressor compressor;
	public static DoubleSolenoid shifter;
	public static Joystick driveGamePad;
	public static final int AXIS = 0; //0 for Left Joystick on Controller, 2 for Right
	
	
	public static enum states{
		JOYSTICKDRIVE, AUTO, DISABLED
	}
	public static states currentState = states.AUTO;
	
	public DriveTrain(int leftPWM, int rightPWM, int compressorID, int PCMCanID, int PCMForwardChannel, int PCMReverseChannel, Joystick gamePad){
		leftMotor = new VictorSP(leftPWM);
		rightMotor = new VictorSP(rightPWM);
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
		leftPower = gamePadY + gamePadX;
		rightPower = gamePadY - gamePadX;
		leftMotor.set(-leftPower);
		rightMotor.set(rightPower);
		
		
	}
	

	
	public void setState(states newState){
		currentState = newState;
	}
}
