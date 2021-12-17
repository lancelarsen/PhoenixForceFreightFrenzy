package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.MecanumTeleOp;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="DriveMotorTest", group="1")
public class DriveMotorTest extends LinearOpMode {

    private MecanumTeleOp drive;

    @Override
    public void runOpMode() {

        drive = new MecanumTeleOp(this);

        waitForStart();
        if (isStopRequested()) return;

        for (int i = 0; i < 4; i++) {
            drive.setMotorPowers(
                i == 0 ? 1 : 0,
                i == 1 ? 1 : 0,
                i == 2 ? 1 : 0,
                i == 3 ? 1 : 0
                );

            sleep(2000);
        }
    }
}