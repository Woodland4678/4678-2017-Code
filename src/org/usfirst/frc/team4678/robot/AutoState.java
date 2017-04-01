package org.usfirst.frc.team4678.robot;

public class AutoState {
	public int desiredDriveEncoderVal;
	public int desiredAngle;
	public GearClaw.states desiredGearState;
	public Baller.IntakeStates desiredIntakeState;
	public Baller.PanelStates desiredPanelState;
	public Baller.WindmillStates desiredWindmillState;
	public Baller.autoStates desiredBallAutoState;
	public Robot robot;
	public int iterations = 0;
	public int minIterations = 0;
	public boolean isPidDrive = false;
	public boolean isPidTurn = false;
	public boolean isGTD = false;
	public boolean isClaw = false;
	public boolean isBalling = false;
	public int leftCentimeters;
	public int rightCentimeters;
	public boolean toGoUp = false;
	public int gdist = 0;
	
	public int rampUpDistance = 15;
	public int rampDownDistance = 25;
	public double startingPower = 0.5;
	public double maxPower = 1;
	public double endingPower = 0.5;
	
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
	public AutoState(int rightCentimeters, int leftCentimeters,double power, int rampupdistance, int rampdowndistance, double startingpower, double endingpower, GearClaw.states gearState , Robot crobot){
		this.leftCentimeters = leftCentimeters;
		this.rightCentimeters = rightCentimeters;
		this.maxPower = power;
		this.rampUpDistance = rampupdistance;
		this.rampDownDistance = rampdowndistance;
		this.startingPower = startingpower;
		this.endingPower = endingpower;
		robot = crobot;
		isGTD = true;
		desiredGearState = gearState;
		isClaw = true;
	}
	
	public AutoState(int rightCentimeters, int leftCentimeters, Baller.autoStates desiredBallAutoState, Robot crobot){
		this.leftCentimeters = leftCentimeters;
		this.rightCentimeters = rightCentimeters;
		robot = crobot;
		isGTD = true;
		this.desiredBallAutoState = desiredBallAutoState;
		isBalling = true;
		
	}
	
	public AutoState(Baller.autoStates desiredBallAutoState, Robot robot){
		this.robot = robot;
		this.desiredBallAutoState = desiredBallAutoState;
		isBalling = true;
	}
	public AutoState(Baller.autoStates desiredBallAutoState,int minIterations, Robot robot){
		this.robot = robot;
		this.desiredBallAutoState = desiredBallAutoState;
		isBalling = true;
		iterations = 0;
		this.minIterations = minIterations;
	}
	
	public AutoState(GearClaw.states desiredClawState, Robot robot){
		this.desiredGearState = desiredClawState;
		this.robot = robot;
		isClaw = true;
	}
	
	public AutoState(){
		
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
			if(robot.driveTrain.goToDistance(rightCentimeters, leftCentimeters, maxPower, rampUpDistance, rampDownDistance, startingPower, endingPower)) { // rightCentimeters, leftCentimeters, power, rampUpDistance,
				gdist = 1; // end go to Distance when we get true // -260 and -330
				}	// rampDownDistance, startingPower, endingPower
		}
		
		if(isClaw){
			robot.claw.setState(desiredGearState);
		}
		
		if(isBalling){
			robot.baller.setAutoState(desiredBallAutoState);
		}
		
		robot.claw.stateMachine();
		robot.baller.autoStateMachine();
	}
	
	public boolean isStateDone(){
		boolean driveDone = false;
		boolean turnDone = false;
		boolean gearDone = false;
		boolean iterationsDone = false;
		boolean isGTDDone = false;
		boolean ballDone = false;
		
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
		
		if(!isBalling){
			ballDone = true;
		}else if(robot.baller.currentAutoState == desiredBallAutoState){
			ballDone = true;
		
			//robot.GOSCORE=  true;
		}
		
		if(iterations >= minIterations){
			iterationsDone = true;
		}
		if(driveDone && turnDone && gearDone && iterationsDone && isGTDDone && ballDone){
			return true;
		}else{
			return false;
		}
	}
	
	public void resetSensors(){
		
		robot.resetSensors();
	}
	
}
