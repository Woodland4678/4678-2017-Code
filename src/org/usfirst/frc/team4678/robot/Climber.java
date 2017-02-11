package org.usfirst.frc.team4678.robot;
import edu.wpi.first.wpilibj.*;

public class Climber {
	
	public static VictorSP climbMotor;
	
	public Climber(int pwmPort){
		climbMotor = new VictorSP(pwmPort);
	}
	
	public void climbFast(){
		climbMotor.set(1);
	}
	
	public void climbMedium(){
		climbMotor.set(0.5);
	}
	
	public void climbSlow(){
		climbMotor.set(0.2);
	}
	public void climbStop(){
		climbMotor.set(0);
	}
}
