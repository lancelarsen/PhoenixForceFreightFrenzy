package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.vison.RingVison;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="DriveMotorTest", group="1")
public class DriveMotorTest extends LinearOpMode {

    private RingVison vision;

    @Override
    public void runOpMode() {

        vision = new RingVision(hardwareMap);
        vision.init(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);

        waitForStart();
        while (isStopRequested()) {
            telemetry.addData("Zone", vision.getColorLevelIndex());
            telemetry.addData("Color level", getColorLevel());
        }
    }
}