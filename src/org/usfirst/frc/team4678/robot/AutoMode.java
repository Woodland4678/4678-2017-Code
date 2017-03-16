package org.usfirst.frc.team4678.robot;
import java.util.*;
public class AutoMode {

	public ArrayList<AutoState> autoStates = new ArrayList<AutoState>();
	public int currentState = 0;
	
	
	public AutoMode(ArrayList<AutoState> states){
		
		autoStates = states;
	}
	
	public void runMode(){
		autoStates.get(currentState).runState();
		if(autoStates.get(currentState).isStateDone()){
			if(currentState < ((autoStates.size()) -1)){
				currentState++;
			}
		}
	}
}
