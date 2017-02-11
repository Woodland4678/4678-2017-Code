package org.usfirst.frc.team4678.robot;

import com.ctre.CANTalon;

public class Baller {

	//PID Constants
	public static final double intakeP = 0.05;
	public static final double intakeI = 0.00;
	public static final double intakeD = 0.00;
	
	public static final double pivotP = 0.5;
	public static final double pivotI = 0.00;
	public static final double pivotD = 0.00;
	
	//Pivot Motor Constants
	public static final int PICKUPHEIGHT = 3300;
	public static final int RELEASEHEIGHT = 1700;
	public static final int ENCLOSEDHEIGHT = 1700;
	
	//Intake Motor Constants
	public static final int PICKUPSPEED = 20000;
	public static final int RELEASESPEED = -22000;
	public static CANTalon pivotMotor;
	public static CANTalon intakeMotor;
	
	public Baller(int pivotMotorID, int intakeMotorID){
		pivotMotor = new CANTalon(pivotMotorID);
		intakeMotor = new CANTalon(intakeMotorID);
		pivotMotor.setPID(pivotP, pivotI, pivotD);
		intakeMotor.setPID(intakeP, intakeI, intakeD);
		pivotMotor.setAllowableClosedLoopErr(30);
		intakeMotor.setAllowableClosedLoopErr(200);
		pivotMotor.configMaxOutputVoltage(3);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
	}
	
	public void pickup(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(PICKUPHEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(PICKUPSPEED);
	}
	
	public void release(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(RELEASEHEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(RELEASESPEED);
	}
	
	public void enclose(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(ENCLOSEDHEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}
	
	
	
}
