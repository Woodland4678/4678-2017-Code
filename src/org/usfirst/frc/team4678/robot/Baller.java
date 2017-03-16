package org.usfirst.frc.team4678.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;

public class Baller {

	// PID Constants
	public static final double intakeP = 0.05;
	public static final double intakeI = 0.00;
	public static final double intakeD = 0.00;
	
	public static final double pivotP = 1.0;
	public static final double pivotI = 0.0;
	public static final double pivotD = 0.00;

	// PID Constants
	//public static final double shooterP = 1;// 0.025;
	//public static final double shooterI = 0;//0.0001;
	//public static final double shooterD = 0;//0.075;
	//public static final double shooterF = 0;//0.0;

	// Pivot Motor Constants
	public static final int INTAKE_PICKUP_HEIGHT = 3300;
	public static final int INTAKE_RELEASE_HEIGHT = 2100;
	public static final int INTAKE_ENCLOSED_HEIGHT = 1700;
	public static final int INTAKE_LOWGOALREADY_HEIGHT = 1600;

	// Competition bot positions
	// public static final int INTAKE_PICKUP_HEIGHT = 3600;
	// public static final int INTAKE_RELEASE_HEIGHT = 2314;
	// public static final int INTAKE_ENCLOSED_HEIGHT = 1988;
	
	// Windmill Motor Constants
	public static final double WINDMILLSPINSPEED = 0.9f;
	public static final double WINDMILL_LOWERED = 0.58; //0-1
	public static final double WINDMILL_RAISED = 0;
	public static Servo windMillLift;
	public static VictorSP windMillSpin; // servos that are controlled by a
											// victor

	// Intake Motor Constants
	public static final int PICKUPSPEED = 20000;
	public static final int RELEASESPEED = -22000;
	public static final int LOWGOALREVERSE = 15000;
	public static final int LOWGOALSPEED = -30000;
	public static CANTalon pivotMotor;
	public static CANTalon intakeMotor;
	public static DoubleSolenoid hopperPneumatic;	

	// Shooter Motor Constants
	//public static final int SHOOTERSPEED =10000;//4200;
	//public static CANTalon shooterMotor;

	// Elevator Motor Constants
	//public static final double ELEVATORSPEED = -0.8;
	//public static VictorSP elevatorMotor;
	
	//Agitator Motor Constants
	public static final double AGITATORSPEED = -0.5;
	public static VictorSP agitator;
	
	public static enum IntakeStates {
		ENCLOSED, PICKUP, RELEASE
	}
	
	public static enum PanelStates {
		RETRACTED, DEPLOYED
	}
	
	public static enum WindmillStates {
		RETRACTED, DEPLOYED
	}

	public static boolean canLowerClaw = true; // used to determine if the claw
												// can lower and we stay within
												// the legal limit
	public static IntakeStates intakeState =
			IntakeStates.ENCLOSED; // currently not used for anything
	
	public static PanelStates panelState =
			PanelStates.RETRACTED; // keeps track of panel position
									// used to make sure the claw is not
									// deployed while this is equal to DEPLOYED
	
	public static WindmillStates millState =
			WindmillStates.RETRACTED; // keep track of panel position
										// used to make sure the claw is not
										// deployed while this is equal to
										// DEPLOYED

	public Baller(int pivotMotorID, int intakeMotorID, int windMillLiftID, int windMillSpinID,
			int PCMCanID, int PCMForwardChannel, int PCMReverseChannel) {
		pivotMotor = new CANTalon(pivotMotorID);
		intakeMotor = new CANTalon(intakeMotorID);
		pivotMotor.setPID(pivotP, pivotI, pivotD);
		intakeMotor.setPID(intakeP, intakeI, intakeD);
		pivotMotor.setAllowableClosedLoopErr(30);
		intakeMotor.setAllowableClosedLoopErr(200);
		pivotMotor.configMaxOutputVoltage(7);
		pivotMotor.setEncPosition(pivotMotor.getPulseWidthPosition());
		agitator = new VictorSP(5);
		hopperPneumatic = new DoubleSolenoid(PCMCanID, PCMForwardChannel, PCMReverseChannel);
		windMillLift = new Servo(windMillLiftID);
		windMillSpin = new VictorSP(windMillSpinID);
		//shooterMotor = new CANTalon(shooterMotorID);
		//shooterMotor.changeControlMode(TalonControlMode.Speed);
		//shooterMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		//shooterMotor.configNominalOutputVoltage(0.0f,  -0.0f);
		//shooterMotor.configMaxOutputVoltage(12.0f);
		//shooterMotor.setProfile(0);
		//shooterMotor.setPID(shooterP, shooterI, shooterD);
		//shooterMotor.setP(shooterP);
		//shooterMotor.setI(shooterI);
		//shooterMotor.setD(shooterD);
		//shooterMotor.setF(shooterF);
		//shooterMotor.setAllowableClosedLoopErr(100);
		//shooterMotor.configMaxOutputVoltage(12);
		//shooterMotor.setInverted(true);
		//elevatorMotor = new VictorSP(elevatorMotorID);
	}

	public void pickup() {
		intakeState = IntakeStates.PICKUP;
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_PICKUP_HEIGHT);
		//intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		//intakeMotor.set(PICKUPSPEED);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		intakeMotor.set(0.3);
	}

	public void release() {
		intakeState = IntakeStates.RELEASE;
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_RELEASE_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(RELEASESPEED);
	}

	public void enclose() {
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);

		pivotMotor.set(INTAKE_ENCLOSED_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}
	public void lowGoalReady(){
		//System.out.println("fish");
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_LOWGOALREADY_HEIGHT);
	}
	public void lowGoalHopper(){
		hopperExtend();
	}
	public void lowGoalShoot(){
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(LOWGOALSPEED);
		pivotMotor.configMaxOutputVoltage(7);
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_RELEASE_HEIGHT);
		agitate(1);
	}
	public void lowGoalStop(){
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
		agitate(0);
	}
	public void lowGoalReverse(){
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(LOWGOALREVERSE);
	}
	
	public void stopDown(){
		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		pivotMotor.set(INTAKE_PICKUP_HEIGHT);
		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		intakeMotor.set(0);
	}

	public void agitate(double power) {
		agitator.set(power);
	}

//	public void shooterStart() {
//		//shooterMotor.setF(shooterF);
//		//shooterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
//		//shooterMotor.set(1);
//		
////		shooterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
////		shooterMotor.set(1.0);
//		
//		//elevatorSpeed(ELEVATORSPEED);
//		
//		//agitator
//		//agitator.set(AGITATORSPEED);
//		
//		//intake
//		//pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
//		//pivotMotor.set(INTAKE_RELEASE_HEIGHT);
//		//intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
//		//intakeMotor.set(15000);
//	}

//	public void shooterStop() {
//		//shooterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
//		shooterMotor.disable();
//		elevatorSpeed(0);
//		agitator.disable();
//		//intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
//		intakeMotor.disable();
	//}
	
//	public void elevatorSpeed(double spd) {
	//	elevatorMotor.set(spd); // Set the elevator motor speed
	//}


	public void hopperRetract() {
		panelState = PanelStates.RETRACTED;
		if (millState == WindmillStates.RETRACTED)
			canLowerClaw = true;
		hopperPneumatic.set(DoubleSolenoid.Value.kReverse);

	}

	public void hopperExtend() {
		panelState = PanelStates.DEPLOYED;
		canLowerClaw = false;
		hopperPneumatic.set(DoubleSolenoid.Value.kForward);
	}

	public void lowerMills() {
		System.out.println("now in the lowermills function");
		millState = WindmillStates.DEPLOYED;
		canLowerClaw = false;
		windMillLift.set(WINDMILL_LOWERED);
	}

	public void liftMills() {
		millState = WindmillStates.RETRACTED;
		if (panelState == PanelStates.RETRACTED) {
			canLowerClaw = true; // if panel closed we can lower the claw
		}
		windMillLift.set(WINDMILL_RAISED);
	}

	public void spinMillOut() {
		if (millState == WindmillStates.DEPLOYED) { // only want to spin the wind mills if they
									// are deployed
			windMillSpin.set(WINDMILLSPINSPEED); // speed value
		} else {
			windMillSpin.disable();
		}
	}

	public void spinMillIn() {
		if (millState == WindmillStates.DEPLOYED) { // only want to spin the wind mills if they
									// are deployed
			windMillSpin.set(-WINDMILLSPINSPEED); // speed value
		} else {
			windMillSpin.disable();
		}

	}

	public void stopMills() {
		windMillSpin.disable();
	}
	boolean atEnclosed = false;
	int cnt = 0;
//	public void printShooterSpeed(){
//		System.out.println(shooterMotor.getSpeed() + " " + shooterMotor.get() + "VOut=" +shooterMotor.getOutputVoltage()+" AOut=" +shooterMotor.getOutputCurrent());
//		System.out.println(shooterMotor.getClosedLoopError());
//	}
//	public void oscillate() {
//		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
//		intakeMotor.set(0);
//		pivotMotor.changeControlMode(CANTalon.TalonControlMode.Position);
//		if (cnt > 25) {
//			if (cnt > 50) {
//				cnt = 0;
//			}
//			pivotMotor.set(INTAKE_RELEASE_HEIGHT+200);
//		} else {
//			pivotMotor.set(INTAKE_RELEASE_HEIGHT-50);
//		}
//		cnt ++;
//		System.out.println(shooterMotor.getSpeed() + " " + shooterMotor.get());
//		agitator.set(AGITATORSPEED);
//		elevatorSpeed(ELEVATORSPEED);
//		if(shooterMotor.getSpeed()>=SHOOTERSPEED-4000){
//			
//			
//			elevatorSpeed(ELEVATORSPEED);
//			//agitator
//			agitator.set(AGITATORSPEED);
//		}
//		intakeMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
//		intakeMotor.set(3000);
//	}

	public boolean getCanLowerClawStatus() {
		return canLowerClaw;
	} // accessor variable to tell us if we are allowed to lower claw based on
		// panel position

	public IntakeStates getIntakePosition() {
		return intakeState;
	} // accessor variable to tell us where the ball pickup currently is
//	public double getShooterSpeed() {
//		return shooterMotor.getSpeed();
//	}

}
