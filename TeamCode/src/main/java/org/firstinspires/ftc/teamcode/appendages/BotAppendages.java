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
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;
import org.firstinspires.ftc.teamcode.appendages.utils.MapUtil;
import org.firstinspires.ftc.teamcode.appendages.utils.MovingAverage;

import com.acmerobotics.roadrunner.util.NanoClock;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

@Config
public class BotAppendages {
    public static enum GondolaLiftDirection {
        EXTEND,
        RETRACT,
        HOLD
    }

    public final static double TRIGGER_PRESSED_THRESH = 0.5;
    public final static double JOYSTICK_ACTIVE_THRESHOLD = 0.4;
    public final static double JOYSTICK_DEAD_ZONE = 0.05;

    public static final double TANK_DRIVE_INTAKE_SPEED = 1.0;

    public static final double FRONT_INTAKE_STOWED_POSITION = 0.5;
    public static final double FRONT_INTAKE_DEPLOYED_POSITION = 1;
    public static final double REAR_INTAKE_STOWED_POSITION = 1;
    public static final double REAR_INTAKE_DEPLOYED_POSITION = 0.5;
    public static final double INTAKE_ROLLER_SPEED = 1;

    public static final double FRONT_GATE_DOWN_POSITION = 0.85;
    public static final double FRONT_GATE_UP_POSITION = 1;
    public static final double REAR_GATE_DOWN_POSITION = 0.85;
    public static final double REAR_GATE_UP_POSITION = 1;

    public static final double DUCK_WHEEL_SPEED = -1.0;

    public static final double GONDOLA_LIFTER_HIGH_POSITION = 110;
    public static final double GONDOLA_LIFTER_MIDDLE_POSITION = 48;
    public static final double GONDOLA_LIFTER_LOW_POSITION = 11.5;
    public static final double GONDOLA_LIFTER_DOWN_POSITION = 0;

    public static final double GONDOLA_LIFTER_SPEED = 1.0;
    public static final double GONDOLA_DEPLOYER_CLOSED_POSITION = 0.5;
    public static final double GONDOLA_DEPLOYER_OPEN_POSITION = 0.1;

    public static final double GONDOLA_BLOCK_PRESENT_THRESHOLD = 50;

    private NanoClock nanoClock;

    public final RevBlinkinLedDriver blinkin;
    public RevBlinkinLedDriver.BlinkinPattern blinkinPattern;

    public final DcMotorEx tankDrive;

    public final Servo frontIntakeDeployer;
    public final Servo rearIntakeDeployer;

    public final Servo frontGate;
    public final Servo rearGate;

    public final DcMotorEx frontIntake;
    public final DcMotorEx rearIntake;

    public final CRServo leftDuckWheel;
    public final CRServo rightDuckWheel;

    private final MovingAverage gondolaBlockMovingAverage = new MovingAverage(5);
    private double gondolaBlockAverageUpdated = 0;
    public final DistanceSensor gondolaBockSensor;

    public final DcMotorEx gondolaLifter;
    public final Servo gondolaDeployer;

    public BotAppendages(HardwareMap hardwareMap) {
        nanoClock = NanoClock.system();

        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        tankDrive = hardwareMap.get(DcMotorEx.class, "tankDrive");

        frontIntakeDeployer = hardwareMap.get(Servo.class, "frontIntakeDeployer");
        rearIntakeDeployer = hardwareMap.get(Servo.class, "rearIntakeDeployer");

        frontIntakeDeployer.setPosition(FRONT_INTAKE_STOWED_POSITION);
        rearIntakeDeployer.setPosition(REAR_INTAKE_STOWED_POSITION);

        frontGate = hardwareMap.get(Servo.class, "frontGate");
        rearGate = hardwareMap.get(Servo.class, "rearGate");

        rearGate.setDirection(Servo.Direction.REVERSE);
        frontGate.setPosition(FRONT_GATE_DOWN_POSITION);
        rearGate.setPosition(REAR_GATE_DOWN_POSITION);

        frontIntake = hardwareMap.get(DcMotorEx.class, "frontIntakeRoller");
        rearIntake = hardwareMap.get(DcMotorEx.class, "rearIntakeRoller");

        leftDuckWheel = hardwareMap.get(CRServo.class, "leftDuckWheel");
        rightDuckWheel = hardwareMap.get(CRServo.class, "rightDuckWheel");

        gondolaBockSensor = (DistanceSensor) hardwareMap.get(NormalizedColorSensor.class, "blockSensor");

        gondolaLifter = hardwareMap.get(DcMotorEx.class, "gondolaLifter");
        gondolaLifter.setDirection(DcMotor.Direction.REVERSE);
        gondolaDeployer = hardwareMap.get(Servo.class, "gondolaDeployer");

        gondolaLifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gondolaLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gondolaLifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        blinkin.setPattern(pattern);
        blinkinPattern = pattern;
    }

    public RevBlinkinLedDriver.BlinkinPattern getBlinkinPattern() {
        return blinkinPattern;
    }

    public void setTankDriveSpeed(double speed) {
        tankDrive.setPower(speed);
    }

    public void setFrontGateUp(boolean up) {
        frontGate.setPosition(up ? FRONT_GATE_UP_POSITION : FRONT_GATE_DOWN_POSITION);
    }

    public void setRearGateUp(boolean up) {
        rearGate.setPosition(up ? REAR_GATE_UP_POSITION : REAR_GATE_DOWN_POSITION);
    }

    public void setFrontIntakeSpeed(double speed) {
        if (speed != 0)
            frontIntakeDeployer.setPosition(FRONT_INTAKE_DEPLOYED_POSITION);

        frontIntake.setPower(speed);
    }

    public void setRearIntakeSpeed(double speed) {
        if (speed != 0)
            rearIntakeDeployer.setPosition(REAR_INTAKE_DEPLOYED_POSITION);

        rearIntake.setPower(speed);
    }

    public void enableDuckWheels(boolean enable) {
        double speed = 0;
        if (enable) {
            speed = DUCK_WHEEL_SPEED;
        }

        leftDuckWheel.setPower(speed);
        rightDuckWheel.setPower(-speed);
    }

    public void setGondolaLiftDirection(GondolaLiftDirection direction) {

        double maxPos = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_HIGH_POSITION);
        double minPos = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_DOWN_POSITION);

        if (direction == GondolaLiftDirection.EXTEND && gondolaLifter.getCurrentPosition() >= maxPos)
            direction = GondolaLiftDirection.HOLD;

        if (direction == GondolaLiftDirection.RETRACT && gondolaLifter.getCurrentPosition() <= minPos)
            direction = GondolaLiftDirection.HOLD;

        double speed = GONDOLA_LIFTER_SPEED;
        if (direction == GondolaLiftDirection.RETRACT)
            speed *= -1;
        if (direction == GondolaLiftDirection.HOLD)
            speed = 0;

        if (speed != 0)
            gondolaLifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (gondolaLifter.getMode() == DcMotor.RunMode.RUN_USING_ENCODER)
            gondolaLifter.setPower(speed);
    }

    public void extakeGondola(boolean extake) {
        if (extake) {
            gondolaDeployer.setPosition(GONDOLA_DEPLOYER_OPEN_POSITION);
        } else {
            gondolaDeployer.setPosition(GONDOLA_DEPLOYER_CLOSED_POSITION);
        }
    }

    public void setGondalaPosition(double position) {
        int ticks = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, position);

        setGondalaPositionTicks(ticks);
    }

    public void setGondalaPositionTicks(double ticks) {
        int maxPos = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_HIGH_POSITION);
        int minPos = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_DOWN_POSITION);

        if (ticks > maxPos)
            ticks = maxPos;
        if (ticks < minPos)
            ticks = minPos;

        gondolaLifter.setTargetPosition((int) ticks);
        gondolaLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        gondolaLifter.setPower(1.0);
    }

    public double getBlockMeanDistance() {
        return gondolaBlockMovingAverage.getMean();
    }

    public boolean isBlockInGondola() {
        double distance = gondolaBockSensor.getDistance(DistanceUnit.CM);
        if (Double.isNaN(distance)) // --- Moving average doesn't like NaN, so set to far away value
            distance = 100.0;
        if (nanoClock.seconds() > gondolaBlockAverageUpdated + 0.05) {
            gondolaBlockAverageUpdated = nanoClock.seconds();
            gondolaBlockMovingAverage.addData(distance);
        }

        return gondolaBlockMovingAverage.getMean() < GONDOLA_BLOCK_PRESENT_THRESHOLD
                && gondolaBlockMovingAverage.isFull();
    }

    public boolean canAcceptBlock() {
        return !isBlockInGondola() && isGondolaRetracted();
    }

    public boolean isGondolaRetracted() {
        return gondolaLifter.getCurrentPosition() < GONDOLA_LIFTER_DOWN_POSITION + 100;
    }
}
