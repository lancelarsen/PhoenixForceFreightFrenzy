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
    public static enum GondolaLiftDirection {
        EXTEND,
        RETRACT,
        HOLD
    }

    public static enum GondolaExtakeDirection {
        FORWARD,
        REVERSE,
        OFF    
    }

    public final static double TRIGGER_PRESSED_THRESH = 0.5;
    public final static double JOYSTICK_ACTIVE_THRESHOLD = 0.4;
    public final static double JOYSTICK_DEAD_ZONE = 0.05;

    public static final double TANK_DRIVE_INTAKE_SPEED = 1.0;

    public static final double FRONT_INTAKE_STOWED_POSITION = 0.5;
    public static final double FRONT_INTAKE_DEPLOYED_POSITION = 1;
    public static final double REAR_INTAKE_STOWED_POSITION = 1;
    public static final double REAR_INTAKE_DEPLOYED_POSITION = 0.5;
    public static final double INTAKE_ROLLER_SPEED = 1.0;

    public static final double DUCK_WHEEL_SPEED = -1.0;

    public static final double GONDOLA_LIFTER_DOWN_POSITION = 0;
    public static final double GONDOLA_LIFTER_UP_POSITION = 63;
    public static final double GONDOLA_LIFTER_SPEED = 0.5;
    public static final double GONDOLA_PLUNGER_RETRACTED_POSITION = 0.65;
    public static final double GONDOLA_PLUNGER_EXTENDED_POSITION = 0.2;
    public static final double GONDOLA_CONVEYOR_SPEED = -1.0;

    public final RevBlinkinLedDriver blinkin;
    public RevBlinkinLedDriver.BlinkinPattern blinkinPattern;

    public final DcMotorEx tankDrive;

    public final Servo frontIntakeDeployer;
    public final Servo rearIntakeDeployer;
    public final DcMotorEx frontIntake;
    public final DcMotorEx rearIntake;

    public final CRServo leftDuckWheel;
    public final CRServo rightDuckWheel;

    public final DcMotorEx gondolaLifter;
    public final Servo gondolaPlunger;
    public final CRServo gondolaConveyor;

    public BotAppendages(HardwareMap hardwareMap) {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        tankDrive = hardwareMap.get(DcMotorEx.class, "tankDrive");

        frontIntakeDeployer = hardwareMap.get(Servo.class, "frontIntakeDeployer");
        rearIntakeDeployer = hardwareMap.get(Servo.class, "rearIntakeDeployer");
        frontIntake = hardwareMap.get(DcMotorEx.class, "frontIntakeRoller");
        rearIntake = hardwareMap.get(DcMotorEx.class, "rearIntakeRoller");

        frontIntakeDeployer.setPosition(FRONT_INTAKE_STOWED_POSITION);
        rearIntakeDeployer.setPosition(REAR_INTAKE_STOWED_POSITION);

        leftDuckWheel = hardwareMap.get(CRServo.class, "leftDuckWheel");
        rightDuckWheel = hardwareMap.get(CRServo.class, "rightDuckWheel");

        gondolaLifter = hardwareMap.get(DcMotorEx.class, "gondolaLifter");
        gondolaPlunger = hardwareMap.get(Servo.class, "gondolaPlunger");
        gondolaConveyor = hardwareMap.get(CRServo.class, "gondolaConveyor");

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

        double maxPos = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_UP_POSITION);
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

        gondolaLifter.setPower(speed);
    }

    public void runGondolaExtake(GondolaExtakeDirection direction) {
        if (direction == GondolaExtakeDirection.FORWARD) {
            gondolaPlunger.setPosition(GONDOLA_PLUNGER_EXTENDED_POSITION);
        } else {
            gondolaPlunger.setPosition(GONDOLA_PLUNGER_RETRACTED_POSITION);
        }

        double speed = GONDOLA_CONVEYOR_SPEED;
        if (direction == GondolaExtakeDirection.REVERSE)
            speed *= -1;
        if (direction == GondolaExtakeDirection.OFF) 
            speed = 0;
        gondolaConveyor.setPower(speed);
    }
}
