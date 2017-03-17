package org.usfirst.frc.team4678.robot;

public class AutoState {
	public int desiredDriveEncoderVal;
	public int desiredAngle;
	public GearClaw.states desiredGearState;
	public Baller.IntakeStates desiredIntakeState;
	public Baller.PanelStates desiredPanelState;
	public Baller.WindmillStates desiredWindmillState;
	public Robot robot;

	public AutoState(int driveEncoder, int gyroAngle, GearClaw.states gearState, Robot crobot){
		desiredDriveEncoderVal = driveEncoder;
		desiredAngle = gyroAngle;
		desiredGearState = gearState;
		//desiredIntakeState = intakeState;
		//desiredPanelState = panelState;
		//desiredWindmillState = windmillState;
		robot = crobot;
	}
	
	public void runState(){
		if(desiredDriveEncoderVal == 0 && desiredAngle != 0){
			robot.driveTrain.pidTurn(desiredAngle);
		}else if(desiredAngle == 0 && desiredDriveEncoderVal != 0){
			robot.driveTrain.pidDrive(desiredDriveEncoderVal);
		}else{
			
		}
		robot.claw.setState(desiredGearState);
		robot.claw.stateMachine();
		
	}
	
	public boolean isStateDone(){
		boolean driveDone = false;
		boolean turnDone = false;
		boolean gearDone = false;
		if(desiredDriveEncoderVal == 0){
			if(robot.driveTrain.pidTurn.isDone()){
				turnDone = true;
				driveDone = true;
			}
		}
		
		if(desiredAngle == 0){
			if(robot.driveTrain.pidDrive.isDone()){
				turnDone = true;
				driveDone = true;
			}
		}
		
		if(robot.claw.currentState == desiredGearState){
			gearDone = true;
		}
		if(driveDone && turnDone && gearDone){
			return true;
		}else{
			return false;
		}
	}
	
}
