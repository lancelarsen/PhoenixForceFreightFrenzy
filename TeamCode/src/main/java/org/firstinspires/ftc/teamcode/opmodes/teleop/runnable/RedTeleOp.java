package org.firstinspires.ftc.teamcode.opmodes.teleop.runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleOp;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Red TeleOp", group="1")
public class RedTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        TeleOp teleOp = new TeleOp(this, AutoUtils.Alliance.RED);

        waitForStart();
        while (opModeIsActive()) {
            teleOp.run();
        }
    }
}
