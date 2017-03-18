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
	public boolean isPidDrive = false;
	public boolean isPidTurn = false;
	public boolean isGTD = false;
	public boolean isClaw = false;
	public int leftCentimeters;
	public int rightCentimeters;
	public int gdist = 0;
	
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
		isClaw = true;
		
		if(driveEncoder != 0 ){
			isPidDrive = true;
		}else if(gyroAngle != 0){
			isPidTurn = true;
		}
	}
	
	public AutoState(int rightCentimeters, int leftCentimeters, Robot crobot){
		this.leftCentimeters = leftCentimeters;
		this.rightCentimeters = rightCentimeters;
		robot = crobot;
		isGTD = true;
	}
	public AutoState(int rightCentimeters, int leftCentimeters,GearClaw.states gearState , Robot crobot){
		this.leftCentimeters = leftCentimeters;
		this.rightCentimeters = rightCentimeters;
		robot = crobot;
		isGTD = true;
		desiredGearState = gearState;
		isClaw = true;
	}
	
	public void runState(){
		if(iterations == 0){
			resetSensors();
			System.out.println("Sensors Reset");
			robot.driveTrain.resetGoToDistanceState();
		}
		iterations++;
		if(isPidTurn){
			robot.driveTrain.pidTurn(desiredAngle, 1);
		}else if(isPidDrive){
			robot.driveTrain.pidDrive(desiredDriveEncoderVal, 0.5);
		}
		
		if (gdist == 0 && isGTD) {
			if(robot.driveTrain.goToDistance(rightCentimeters, leftCentimeters, 1, 15, 25, 0.50, 0.50)) { // rightCentimeters, leftCentimeters, power, rampUpDistance,
				gdist = 1; // end go to Distance when we get true // -260 and -330
				}	// rampDownDistance, startingPower, endingPower
		}
		
		if(isClaw){
			robot.claw.setState(desiredGearState);
		}
		
		robot.claw.stateMachine();
		
	}
	
	public boolean isStateDone(){
		boolean driveDone = false;
		boolean turnDone = false;
		boolean gearDone = false;
		boolean iterationsDone = false;
		boolean isGTDDone = false;
		
		if(isPidTurn){
			if(robot.driveTrain.pidTurn.isDone() && iterations > 5){
				turnDone = true;
				driveDone = true;
			}
		}
		
		if(isPidDrive){
			if(robot.driveTrain.pidDrive.isDone() && iterations > 5){
				turnDone = true;
				driveDone = true;
			}
		}
		
		if(!isPidDrive){
			driveDone = true;
		}
		
		if(!isPidTurn){
			turnDone = true;
		}
		
		if(!isGTD){
			isGTDDone = true;
		}
		
		
		if(gdist == 1){
			isGTDDone = true;
		}
		if(!isClaw){
			gearDone = true;
		}else if(robot.claw.currentState == desiredGearState){
			gearDone = true;
		}
		
		if(iterations >= minIterations){
			iterationsDone = true;
		}
		if(driveDone && turnDone && gearDone && iterationsDone && isGTDDone){
			return true;
		}else{
			return false;
		}
	}
	
	public void resetSensors(){
		
		robot.resetSensors();
	}
	
}
