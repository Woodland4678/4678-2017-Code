package org.usfirst.frc.team4678.robot;
import java.util.*;
public class AutoMode {

	public ArrayList<AutoState> autoStates = new ArrayList<AutoState>();
	public int currentState = 0;
	
	
	public AutoMode(ArrayList<AutoState> states){
		
		autoStates = states;
		currentState = 0;
	}
	
	public void runMode(){
		
		if(autoStates.get(currentState).isStateDone()){
			if(currentState < ((autoStates.size()) -1)){
				autoStates.get(currentState).robot.driveTrain.leftMotor.set(0);
				autoStates.get(currentState).robot.driveTrain.rightMotor.set(0);
				currentState++;
				
			}else{
				autoStates.get(currentState).robot.driveTrain.leftMotor.set(0);
				autoStates.get(currentState).robot.driveTrain.rightMotor.set(0);
				
			}
			
		}else{
			autoStates.get(currentState).runState();
		}
	}
}
