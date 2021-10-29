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
    private LinearOpMode opMode;

    private NanoClock nanoClock;
    private double startTime = -1;

    private ButtonToggle tankToggle = new ButtonToggle();

    public AppendagesTeleOp(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;

        nanoClock = NanoClock.system();
    }

    public void updateTankDrive() {
        enableTankDrive(tankToggle.isActive());

        tankToggle.update(opMode.gamepad1.a);
    }
}
