package org.firstinspires.ftc.teamcode.appendages;

import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GameConstants;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.teleop.util.ButtonToggle;
import org.firstinspires.ftc.teamcode.opmodes.teleop.util.GamepadUtils;

public class AppendagesTeleOp extends BotAppendages {
    private static enum DriveDirection {
        FORWARD,
        REVERSE
    }

    private LinearOpMode opMode;

    private NanoClock nanoClock;
    private double startTime = -1;

    private DriveDirection lastDirectionDriven = DriveDirection.FORWARD;

    private boolean isTankDriveEnabled = false;

    private ButtonToggle frontIntakeToggle = new ButtonToggle();
    private ButtonToggle rearIntakeToggle = new ButtonToggle();

    private ButtonToggle duckWheelsToggle = new ButtonToggle();

    private ButtonToggle gondolaLifterAdjToggle = new ButtonToggle();

    public AppendagesTeleOp(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;

        nanoClock = NanoClock.system();
    }

    public void updateLights(AutoUtils.Alliance alliance) {
        if (startTime == -1) {
            startTime = nanoClock.seconds();
        }
        double activeTime = nanoClock.seconds() - startTime;

        if (80 < activeTime && activeTime < 90 && activeTime % 0.5 <= 0.25) {
            setBlinkinPattern(BlinkinPatterns.OFF);
        } else {
            if (isTankDriveEnabled) {
                setBlinkinPattern(BlinkinPatterns.TANK_DRIVE_ACTIVE_PATTERN);
            } else {
                if (alliance == AutoUtils.Alliance.BLUE) {
                    setBlinkinPattern(BlinkinPatterns.BLUE_BASE_PATTERN);
                } else {
                    setBlinkinPattern(BlinkinPatterns.RED_BASE_PATTERN);
                }
            }    
        }
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

            opMode.telemetry.addData("tankDriveSpeed", speed);
            opMode.telemetry.update();
            setTankDriveSpeed(speed);
            
            isTankDriveEnabled = false;
        } else if (isTankDriveEnabled) {
            setTankDriveSpeed(forwardDriveSpeed);
        } else {
            setTankDriveSpeed(0);
        }
    }

    public void updateIntake() {
        frontIntakeToggle.update(opMode.gamepad2.b);
        rearIntakeToggle.update(opMode.gamepad2.a);

        double speed = INTAKE_ROLLER_SPEED;
        if (isIntakeReversed()) {
            speed *= -1;
        }

        setFrontIntakeSpeed(frontIntakeToggle.isActive() ? -speed : 0);
        setRearIntakeSpeed(rearIntakeToggle.isActive() ? speed : 0);
    }

    private boolean isIntakeReversed() {
        return opMode.gamepad2.left_trigger > TRIGGER_PRESSED_THRESH;
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


        GondolaExtakeDirection gondolaDirection = GondolaExtakeDirection.OFF;
        if (opMode.gamepad2.x) {
            gondolaDirection = GondolaExtakeDirection.REVERSE;
        } else if (opMode.gamepad2.y) {
            gondolaDirection = GondolaExtakeDirection.FORWARD;
        }
        runGondolaExtake(gondolaDirection);
    }
}