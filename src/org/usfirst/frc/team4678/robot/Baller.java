package org.usfirst.frc.team4678.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

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
	public static final int RELEASEHEIGHT = 2000;
	public static final int ENCLOSEDHEIGHT = 1700;
	
	//Intake Motor Constants
	public static final int PICKUPSPEED = 20000;
	public static final int RELEASESPEED = -35000;
	public static CANTalon pivotMotor; 
	public static CANTalon intakeMotor;
	public static VictorSP agitator;
	public static Solenoid hopperPneumatic;
	public Baller(int pivotMotorID, int intakeMotorID, int hopperPCM, int PCMCanID){
		pivotMotor = new CANTalon(pivotMotorID);
		intakeMotor = new CANTalon(intakeMotorID);
		pivotMotor.setPID(pivotP, pivotI, pivotD);
		intakeMotor.setPID(intakeP, intakeI, intakeD);
		pivotMotor.setAllowableClosedLoopErr(30);
		intakeMotor.setAllowableClosedLoopErr(200);
		pivotMotor.configMaxOutputVoltage(3);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
		agitator = new VictorSP(5);
		hopperPneumatic = new Solenoid(PCMCanID, hopperPCM);
	}
	
	public void pickup(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(PICKUPHEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(PICKUPSPEED);
	}
	
	public void release(int inverse){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(RELEASEHEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(RELEASESPEED*inverse);
	}
	
	public void enclose(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		
		pivotMotor.set(ENCLOSEDHEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}
	
	public void agitate(int status){
		if(status == 1){
			agitator.set(1);
			System.out.println("DOG");
		}else{
			agitator.set(0);
		}
	}
	
	
	
}
