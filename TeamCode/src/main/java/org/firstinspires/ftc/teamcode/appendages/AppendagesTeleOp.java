package org.firstinspires.ftc.teamcode.appendages;

import java.text.NumberFormat;

import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.teleop.util.ButtonToggle;
import org.firstinspires.ftc.teamcode.opmodes.teleop.util.GamepadUtils;

public class AppendagesTeleOp extends BotAppendages {
    private static enum DriveDirection {
        FORWARD,
        REVERSE
    }

    private LinearOpMode opMode;
    private NumberFormat numberFormat;

    private NanoClock nanoClock;
    private double startTime = -1;

    private DriveDirection lastDirectionDriven = DriveDirection.FORWARD;

    private boolean isTankDriveEnabled = false;

    private ButtonToggle blockSensorOverride = new ButtonToggle();

    private ButtonToggle frontIntakeToggle = new ButtonToggle();
    private ButtonToggle rearIntakeToggle = new ButtonToggle();

    private ButtonToggle duckWheelsToggle = new ButtonToggle();

    private ButtonToggle gondolaLifterAdjToggle = new ButtonToggle();

    public AppendagesTeleOp(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        nanoClock = NanoClock.system();
    }

    public void updateLights(AutoUtils.Alliance alliance) {
        if (startTime == -1 && opMode.opModeIsActive()) {
            startTime = nanoClock.seconds();
        }
        double activeTime = nanoClock.seconds() - startTime;

        if (80 < activeTime && activeTime < 90 && activeTime % 0.5 <= 0.25) {
            setBlinkinPattern(BlinkinPatterns.OFF);
        } else if (115 < activeTime && activeTime < 120) {
            setBlinkinPattern(BlinkinPatterns.TELE_FIVE_SECONDS_LEFT_PATTERN);
        } else {
            if (isTankDriveEnabled) {
                setBlinkinPattern(BlinkinPatterns.TANK_DRIVE_ACTIVE_PATTERN);
            } else if (blockSensorOverride.isActive()) {
                setBlinkinPattern(BlinkinPatterns.BLOCK_SENSOR_DISABLED_PATTERN);
            } else if (!canAcceptBlock()) {
                setBlinkinPattern(BlinkinPatterns.BLOCK_IN_GONDOLA_PATTEN);
            } else {
                if (alliance == AutoUtils.Alliance.BLUE) {
                    setBlinkinPattern(BlinkinPatterns.BLUE_BASE_PATTERN);
                } else {
                    setBlinkinPattern(BlinkinPatterns.RED_BASE_PATTERN);
                }
            }
        }

        opMode.telemetry.addData("Game time", numberFormat.format(activeTime));
    }

    public void updateTankDrive(double forwardDriveSpeed) {
        if (forwardDriveSpeed < 0) {
            lastDirectionDriven = DriveDirection.REVERSE;
        } else if (forwardDriveSpeed > 0) {
            lastDirectionDriven = DriveDirection.FORWARD;
        }

        if (opMode.gamepad1.y) {
            isTankDriveEnabled = true;

            frontIntakeToggle.setActive(false);
            rearIntakeToggle.setActive(false);
        }

        if (frontIntakeToggle.isActive() || rearIntakeToggle.isActive()) {
            // tankDriveSpeed: 1.0 when intaking from front
            double speed = TANK_DRIVE_INTAKE_SPEED;
            if (lastDirectionDriven == DriveDirection.REVERSE) {
                speed *= -1;
            }
            if (isIntakeReversed()) {
                speed *= -1;
            }

            setTankDriveSpeed(speed);

            isTankDriveEnabled = false;
        } else if (isTankDriveEnabled) {
            setTankDriveSpeed(forwardDriveSpeed);
        } else {
            setTankDriveSpeed(0);
        }
    }

    public void updateIntake() {
        blockSensorOverride.update(opMode.gamepad1.right_trigger > TRIGGER_PRESSED_THRESH);

        frontIntakeToggle.update(opMode.gamepad2.b);
        rearIntakeToggle.update(opMode.gamepad2.a);

        boolean canAcceptBlock = canAcceptBlock() || blockSensorOverride.isActive();
        double speed = INTAKE_ROLLER_SPEED;
        if (!canAcceptBlock) {
            speed *= -1;
        }
        if (isIntakeReversed()) {
            speed *= -1;
        }

        setFrontIntakeSpeed(frontIntakeToggle.isActive() ? -speed : 0);
        setRearIntakeSpeed(rearIntakeToggle.isActive() ? speed : 0);

        setFrontGateUp(!canAcceptBlock || !frontIntakeToggle.isActive());
        setRearGateUp(!canAcceptBlock || !rearIntakeToggle.isActive());

        opMode.telemetry.addData("Block sensor distance",
                numberFormat.format(gondolaBockSensor.getDistance(DistanceUnit.CM)));
        opMode.telemetry.addData("Block sensor mean", numberFormat.format(getBlockMeanDistance()));
        opMode.telemetry.addData("Can accept block", canAcceptBlock);
        opMode.telemetry.update();
    }

    private boolean isIntakeReversed() {
        return opMode.gamepad2.left_trigger > TRIGGER_PRESSED_THRESH
                || opMode.gamepad1.left_trigger > TRIGGER_PRESSED_THRESH;
    }

    public void updateDuckWheels() {
        duckWheelsToggle.update(opMode.gamepad2.left_bumper);

        enableDuckWheels(duckWheelsToggle.isActive());
    }

    public void updateGondola() {
        boolean adjButtonPressed = opMode.gamepad2.right_stick_button;
        gondolaLifterAdjToggle.update(adjButtonPressed);
        if (!gondolaLifterAdjToggle.isActive() && adjButtonPressed) {
            gondolaLifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            gondolaLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (gondolaLifterAdjToggle.isActive()) {
            gondolaLifter.setPower(-opMode.gamepad2.right_stick_y);
        } else {
            if (opMode.gamepad2.right_bumper) {
                setGondolaLiftDirection(GondolaLiftDirection.EXTEND);
            } else if (opMode.gamepad2.right_trigger > TRIGGER_PRESSED_THRESH) {
                setGondolaLiftDirection(GondolaLiftDirection.RETRACT);
            } else {
                setGondolaLiftDirection(GondolaLiftDirection.HOLD);
            }
        }

        extakeGondola(opMode.gamepad2.x);
    }
}