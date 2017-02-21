package org.usfirst.frc.team4678.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;

public class Baller {

	//PID Constants
	public static final double intakeP = 0.05;
	public static final double intakeI = 0.00;
	public static final double intakeD = 0.00;
	
	public static final double pivotP = 1.0;
	public static final double pivotI = 0.001;
	public static final double pivotD = 0.00;
	
	//Pivot Motor Constants
	//Practice bot positions
//	public static final int INTAKE_PICKUP_HEIGHT = 3300;
//	public static final int INTAKE_RELEASE_HEIGHT = 1700;
//	public static final int INTAKE_ENCLOSED_HEIGHT = 1700;
	
	//Competition bot positions
	public static final int INTAKE_PICKUP_HEIGHT = 3600;
	public static final int INTAKE_RELEASE_HEIGHT = 2314;
	public static final int INTAKE_ENCLOSED_HEIGHT = 1988;
	
	//Intake Motor Constants
	public static final int PICKUPSPEED = 20000; //20000
	public static final int RELEASESPEED = -22000; //-22000
	public static CANTalon pivotMotor;
	public static CANTalon intakeMotor;
	
	public static Servo windMillLift;
	public static VictorSP windMillSpin;
	
	public Baller(int pivotMotorID, int intakeMotorID, int windMillLiftID, int windMillSpinID){
		pivotMotor = new CANTalon(pivotMotorID);
		intakeMotor = new CANTalon(intakeMotorID);
		windMillLift = new Servo(windMillLiftID);
		windMillSpin = new VictorSP(windMillSpinID);
		pivotMotor.setPID(pivotP, pivotI, pivotD);
		intakeMotor.setPID(intakeP, intakeI, intakeD);
		pivotMotor.setAllowableClosedLoopErr(30);
		intakeMotor.setAllowableClosedLoopErr(200);
		pivotMotor.configMaxOutputVoltage(7);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
	}
	
	public void pickup(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_PICKUP_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(PICKUPSPEED);
	}
	
	public void release(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_RELEASE_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(RELEASESPEED);
	}
	
	public void enclose(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_ENCLOSED_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}
	public void lowerMills(){
		windMillLift.set(0.58); //position (between 0 - 1 I believe)
	}
	public void liftMills(){
		windMillLift.set(0); //position
	}
	public void spinMillOut() {
		windMillSpin.set(0.9); //speed value 
	}
	public void spinMillIn() {
		windMillSpin.set(-0.9); //speed value
	}
	public void stopMills() {
		windMillSpin.disable();
	}
	
	
}
