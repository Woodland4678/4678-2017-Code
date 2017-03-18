package org.usfirst.frc.team4678.robot;

public class AutoState {
	public int desiredDriveEncoderVal;
	public int desiredAngle;
	public GearClaw.states desiredGearState;
	public Baller.IntakeStates desiredIntakeState;
	public Baller.PanelStates desiredPanelState;
	public Baller.WindmillStates desiredWindmillState;
	public Robot robot;
	public int iterations = 0;
	public int minIterations = 0;
	
	
	public AutoState(int driveEncoder, int gyroAngle, GearClaw.states gearState, int minimumIterations, Robot crobot){
		desiredDriveEncoderVal = driveEncoder;
		desiredAngle = gyroAngle;
		desiredGearState = gearState;
		//desiredIntakeState = intakeState;
		//desiredPanelState = panelState;
		//desiredWindmillState = windmillState;
		robot = crobot;
		iterations = 0;
		minIterations = minimumIterations;
	}
	
	public void runState(){
		if(iterations == 0){
			resetSensors();
			System.out.println("Sensors Reset");
		}
		iterations++;
		if(desiredDriveEncoderVal == 0 && desiredAngle != 0){
			robot.driveTrain.pidTurn(desiredAngle, 1);
		}else if(desiredAngle == 0 && desiredDriveEncoderVal != 0){
			robot.driveTrain.pidDrive(desiredDriveEncoderVal, 0.5);
		}else if(desiredDriveEncoderVal == 0 && desiredAngle == 0){
			robot.driveTrain.leftMotor.set(0);
			robot.driveTrain.rightMotor.set(0);
		}
		robot.claw.setState(desiredGearState);
		robot.claw.stateMachine();
		
	}
	
	public boolean isStateDone(){
		boolean driveDone = false;
		boolean turnDone = false;
		boolean gearDone = false;
		boolean iterationsDone = false;
		if(desiredDriveEncoderVal == 0){
			if(robot.driveTrain.pidTurn.isDone() && iterations > 5){
				turnDone = true;
				driveDone = true;
			}
		}
		
		if(desiredAngle == 0){
			if(robot.driveTrain.pidDrive.isDone() && iterations > 5){
				turnDone = true;
				driveDone = true;
			}
		}
		
		if(robot.claw.currentState == desiredGearState){
			gearDone = true;
		}
		
		if(iterations >= minIterations){
			iterationsDone = true;
		}
		if(driveDone && turnDone && gearDone && iterationsDone){
			return true;
		}else{
			return false;
		}
	}
	
	public void resetSensors(){
		
		robot.resetSensors();
	}
	
}
