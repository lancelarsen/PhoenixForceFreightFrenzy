package org.firstinspires.ftc.teamcode.appendages;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GameConstants;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;
import org.firstinspires.ftc.teamcode.appendages.utils.MapUtil;
import org.firstinspires.ftc.teamcode.appendages.utils.MovingAverage;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

@Config
public class BotAppendages {
    public final static double TRIGGER_PRESSED_THRESH = 0.5;
    public final static double JOYSTICK_DEAD_ZONE = 0.05;

    public final static double MIN_SHOOTER_TILTER_ANGLE = 0.1;
    public final static double MAX_SHOOTER_TILTER_ANGLE = 0.0;
    public final static double SHOOTER_TILTER_SHOOTING_ANGLE = 0.5;
    public final static double SHOOTER_TILTER_LOADING_ANGLE = 0.4;
    public final static double EXTENDED_SHOOTER_ARM_ANGLE = 0.7;
    public final static double RETRACTED_SHOOTER_ARM_ANGLE = 0.3;
    public final static double RING_SHOOTER_WHEEL_SPEED_HIGH_GOAL = 1725; //1750
    public final static double RING_SHOOTER_WHEEL_SPEED_POWER_SHOTS = 1500;

    public final static long SHOOTER_ARM_EXTEND_DELAY = 300;
    public final static long SHOOTER_ARM_RETRACT_DELAY = 300;

    public final static double ELEVATOR_NO_RING_DISTANCE_THRESH = 4.0;

    public final static double INTAKE_ROLLER_SPEED = -1.0;
    public final static double INTAKE_ELEVATOR_SPEED = -1.0;
    public final static double INTAKE_CONVEYOR_SPEED = -1.0;

    public final static double GOAL_LIFTER_SPEED = 0.9;
    public final static double DOWN_GOAL_LIFTER_POSITION = 0;
    public final static double MIDDLE_GOAL_LIFTER_POSITION = 20;
    public final static double UP_GOAL_LIFTER_POSITION = 40; // In inches
    public final static double OPEN_GOAL_GRABBER_ANGLE = 0.6;
    public final static double CLOSED_GOAL_GRABBER_ANGLE = 0.18;
    public final static double OPEN_GOAL_LOCK_ANGLE = 0.5;
    public final static double CLOSED_GOAL_LOCK_ANGLE = 0;

    public final static double RETRACTED_REACH_ARM_ANGLE = 0.8;
    public final static double SHOOTER_CLEARED_REACH_ARM_ANGLE = 0.8;
    public final static double EXTENDED_REACH_ARM_ANGLE = 0.45;

    public final RevBlinkinLedDriver blinkin;
    public RevBlinkinLedDriver.BlinkinPattern blinkinPattern;

    public final Servo shooterTilter;
    public final Servo shooterArm;
    public final DcMotorEx shooterWheel;
    public ShooterSpeed preShooterSpeed = ShooterSpeed.OFF;

    public final NormalizedColorSensor ringDetector;
    private final MovingAverage ringDistanceMovingAverage = new MovingAverage(3);
    private boolean preRingInElevator = false;
    protected volatile int numRingsInRobot = -1;

    public final DcMotor intakeRoller;
    public final DcMotor intakeElevator;
    public final CRServo intakeConveyor;
    private boolean elevatorEnabled;
    private boolean intakeEnabled;
    private Direction intakeDirection = Direction.FORWARD;

    public final DcMotorEx goalLifter;
    public final Servo goalGrabber;
    public final Servo goalLock;
    private Thread goalLockThread;
    private boolean goalGrabberLastOpen = false;

    public Servo reachArm;

    public enum Direction {
        FORWARD,
        REVERSE
    }

    public enum ShooterSpeed {
        HIGH_GOAL,
        POWERSHOTS,
        OFF
    }

    public enum ShooterAngle {
        SHOOTING,
        LOADING
    }

    public enum GoalLifterPosition {
        UP,
        MIDDLE,
        DOWN
    }

    public enum ReachArmPosition {
        RETRACTED,
        SHOOTER_CLEARED,
        EXTENDED
    }

    public BotAppendages(HardwareMap hardwareMap) {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        shooterTilter = hardwareMap.get(Servo.class, "shooterTilter");
        shooterArm = hardwareMap.get(Servo.class, "shooterArm");
        shooterWheel = hardwareMap.get(DcMotorEx.class, "shooterWheel");
        shooterWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooterWheel.setDirection(DcMotorEx.Direction.REVERSE);

        ringDetector = hardwareMap.get(NormalizedColorSensor.class, "ringDetector");
        ringDetector.setGain(2);

        intakeRoller = hardwareMap.get(DcMotor.class, "intakeRoller");
        intakeElevator = hardwareMap.get(DcMotor.class, "intakeElevator");
        intakeConveyor = hardwareMap.get(CRServo.class, "intakeConveyor");

        goalLifter = hardwareMap.get(DcMotorEx.class, "goalGrabberLifter");
        goalLifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        goalLifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        goalGrabber = hardwareMap.get(Servo.class, "leftGrabberServo");
        goalLock = hardwareMap.get(Servo.class, "goalLock");
        goalLock.setDirection(Servo.Direction.REVERSE);

        reachArm = hardwareMap.get(Servo.class, "reachArm");
        reachArm.setDirection(Servo.Direction.REVERSE);
        setReachArmPosition(ReachArmPosition.RETRACTED);
    }

    public void setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        blinkin.setPattern(pattern);
        blinkinPattern = pattern;
    }

    public RevBlinkinLedDriver.BlinkinPattern getBlinkinPattern() {
        return blinkinPattern;
    }

    public void setShooterTilterAngle(ShooterAngle shooterAngle) {
        switch (shooterAngle) {
            case SHOOTING:
                shooterTilter.setPosition(SHOOTER_TILTER_SHOOTING_ANGLE);
                break;
            case LOADING:
                shooterTilter.setPosition(SHOOTER_TILTER_LOADING_ANGLE);
                break;
        }
    }

    public void shooterOff() {
        setShooterSpeed(ShooterSpeed.OFF);
    }

    public void setShooterSpeed(ShooterSpeed speed) {
        if (speed != ShooterSpeed.OFF) {
            setReachArmPosition(ReachArmPosition.SHOOTER_CLEARED);
        }

        switch (speed) {
            case HIGH_GOAL:
                shooterWheel.setVelocity(RING_SHOOTER_WHEEL_SPEED_HIGH_GOAL);
                break;
            case POWERSHOTS:
                shooterWheel.setVelocity(RING_SHOOTER_WHEEL_SPEED_POWER_SHOTS);
                break;
            case OFF:
                shooterWheel.setVelocity(0);
                break;
        }
    }

    public void setShooterSpeed(double speed) {
        if (reachArm.getPosition() == RETRACTED_REACH_ARM_ANGLE) {
            setReachArmPosition(ReachArmPosition.SHOOTER_CLEARED);
        }

        shooterWheel.setVelocity(speed);
    }

    public void shootRings() {
        shootRings(GameConstants.MAX_RINGS_IN_ROBOT);
    }

    public void shootRings(int num) {
        //try {
        for (int i = 0; i < num; i++) {
            extendShooterArm(true);
            sleep(SHOOTER_ARM_EXTEND_DELAY);

            if (numRingsInRobot > 0) numRingsInRobot--;

            extendShooterArm(false);
            sleep(SHOOTER_ARM_RETRACT_DELAY);
        }
        //} catch (InterruptedException e) { return; };
    }

    public void extendShooterArm(boolean extended) {
        if (extended) {
            shooterArm.setPosition(EXTENDED_SHOOTER_ARM_ANGLE);
        } else {
            shooterArm.setPosition(RETRACTED_SHOOTER_ARM_ANGLE);
        }
    }

    public void updateRingsInElevator() {
        boolean ringInElevator = isRingInElevator();
        if (ringInElevator && ringInElevator != preRingInElevator) {
            if (numRingsInRobot < GameConstants.MAX_RINGS_IN_ROBOT) numRingsInRobot++;
        }
        preRingInElevator = ringInElevator;
    }

    public boolean isRingInElevator() {
        ringDistanceMovingAverage.addData(((DistanceSensor) ringDetector).getDistance(DistanceUnit.CM));

        return ringDistanceMovingAverage.getMean() > ELEVATOR_NO_RING_DISTANCE_THRESH;
    }

    public void enableElevator(boolean enabled) {
        elevatorEnabled = enabled;
        runIntake();
    }

    public void enableIntake(boolean enabled) {
        intakeEnabled = enabled;
        runIntake();
    }

    // Also sets direction of elevator
    public void setIntakeDirection(Direction direction) {
        intakeDirection = direction;
        runIntake();
    }

    private void runIntake() {
        double intakeRollerSpeed = 0;
        double intakeElevatorSpeed = 0;
        double intakeConveyorSpeed = 0;

        double direction = intakeDirection == Direction.FORWARD ? 1 : -1;

        if (elevatorEnabled) {
            intakeElevatorSpeed = INTAKE_ELEVATOR_SPEED * direction;
            intakeConveyorSpeed = INTAKE_CONVEYOR_SPEED * direction;
        }

        if (intakeEnabled) {
            intakeRollerSpeed = INTAKE_ROLLER_SPEED * direction;
        }

        intakeRoller.setPower(intakeRollerSpeed);
        intakeElevator.setPower(intakeElevatorSpeed);
        intakeConveyor.setPower(intakeConveyorSpeed);
    }

    public void setGoalLifterPosition(GoalLifterPosition position) {
        double lifterPosition = DOWN_GOAL_LIFTER_POSITION;
        switch (position) {
            case UP:
                lifterPosition = UP_GOAL_LIFTER_POSITION;
                break;
            case MIDDLE:
                lifterPosition = MIDDLE_GOAL_LIFTER_POSITION;
                break;
        }

        goalLifter.setTargetPosition(EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, lifterPosition));
        goalLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        goalLifter.setPower(GOAL_LIFTER_SPEED);
    }

    public void openGoalGrabber(boolean open) {
        if (goalGrabberLastOpen == open) return;
        goalGrabberLastOpen = open;

        if (goalLockThread != null) goalLockThread.interrupt();

        Runnable openTask = () -> {
            try {
                if (open) {
                    goalLock.setPosition(OPEN_GOAL_LOCK_ANGLE);
                    Thread.sleep(100);
                    goalGrabber.setPosition(OPEN_GOAL_GRABBER_ANGLE);
                } else {
                    goalGrabber.setPosition(CLOSED_GOAL_GRABBER_ANGLE);
                    Thread.sleep(400);
                    goalLock.setPosition(CLOSED_GOAL_LOCK_ANGLE);
                }
            } catch (InterruptedException e) {}
        };

        goalLockThread = new Thread(openTask);
        goalLockThread.start();
    }

    public void setReachArmPosition(ReachArmPosition position) {
        switch (position) {
            case RETRACTED:
                reachArm.setPosition(RETRACTED_REACH_ARM_ANGLE);
                break;
            case SHOOTER_CLEARED:
                reachArm.setPosition(SHOOTER_CLEARED_REACH_ARM_ANGLE);
                break;
            case EXTENDED:
                reachArm.setPosition(EXTENDED_REACH_ARM_ANGLE);
                break;
        }
    }
}
