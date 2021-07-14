package org.firstinspires.ftc.teamcode.opmodes.teleop.runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleOp;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Blue TeleOp", group="1")
public class BlueTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        TeleOp teleOp = new TeleOp(this, AutoUtils.Alliance.BLUE);

        waitForStart();
        while (opModeIsActive()) {
            teleOp.run();
        }
    }
}
